package com.gjjbook.servlet;

import com.gjjbook.dao.DaoException;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.DTO.AccountDTO;
import com.gjjbook.domain.Gender;
import com.gjjbook.domain.Phone;
import com.gjjbook.domain.PhoneType;
import com.gjjbook.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Controller
@SessionAttributes("loggedUser")
@MultipartConfig(maxFileSize = 1_617_721)
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private AccountService service;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/login_authenticate", method = RequestMethod.POST)
    public ModelAndView loginAuthenticate(@RequestAttribute(value = "email", required = false) String email,
                                          @RequestAttribute(value = "password", required = false) String password,
                                          @RequestAttribute(value = "path", required = false) String path,
                                          @RequestParam(value = "email", required = false) String paramEmail,
                                          @RequestParam(value = "password", required = false) String paramPassword,
                                          @RequestParam(value = "rememberMe", required = false) Object rememberMe,
                                          HttpServletResponse response) {
        boolean isEncrypted = true;
        ModelAndView modelAndView;

        if (email == null || password == null) {
            email = paramEmail;
            password = paramPassword;
            isEncrypted = false;
        }

        if (email == null || password == null) {
            modelAndView = new ModelAndView("/login");
            modelAndView.addObject("errMsg", "Please Enter Username and Password");
        } else {
            if (service.isPasswordMatch(email, password, isEncrypted)) {
                Account loggedUser = service.getByEmail(email);
                if (rememberMe != null) {
                    response.addCookie(new Cookie("email", email));
                    if (!isEncrypted) {
                        password = loggedUser.getPassword();
                    }
                    response.addCookie(new Cookie("password", password));
                }

                if (path == null) {
                    path = "/account?id=" + loggedUser.getId();
                }
                modelAndView = new ModelAndView("redirect:" + path);
                modelAndView.addObject("loggedUser", loggedUser);

                logger.info("Account with id=" + loggedUser.getId() + " logged in"); // TODO: 30.10.2017 add IP
            } else {
                modelAndView = new ModelAndView("/login");
                modelAndView.addObject("errMsg", "Email/password does not match");

                logger.info("Someone tried to login with no luck"); // TODO: 30.10.2017 add IP
            }

        }

        return modelAndView;
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ModelAndView showAccount(@RequestParam("id") Integer idParam,
                                    HttpServletResponse resp) throws IOException {
        ModelAndView modelAndView = new ModelAndView("accountView");

        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } else {
            Account account = service.getByPk(idParam);

            if (account == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return null;
            } else {
                modelAndView.addObject("account", account);
                modelAndView.addObject("avatar", service.convertByteAvatarToString(account.getAvatar()));
            }

            return modelAndView;
        }
    }

    @RequestMapping(value = "/editaccount", method = RequestMethod.GET)
    public ModelAndView editAccount(@RequestParam(value = "id") Integer idParam,
                                    @SessionAttribute("loggedUser") Account loggedUser,
                                    HttpServletResponse resp) throws IOException {
        ModelAndView modelAndView = new ModelAndView("editAccount");

        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else if (!idParam.equals(loggedUser.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You can't edit other accounts");
        } else {
            Account account = service.getByPk(idParam);
            if (account == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return null;
            } else {
                modelAndView.addObject("account", account);
            }

        }

        return modelAndView;
    }

    @InitBinder("account")
    public void customAccountModel(WebDataBinder binder) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        sdf.setLenient(true);
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
        binder.registerCustomEditor(LocalDate.class, "birthDate", new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                LocalDate localDate = (LocalDate) getValue();
                return localDate.toString();
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text));
            }
        });
        binder.registerCustomEditor(PhoneType.class, "type", new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                PhoneType phoneType = (PhoneType) getValue();
                return phoneType.name();
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(PhoneType.valueOf(text));
            }
        });
        binder.registerCustomEditor(Gender.class, "sex", new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                Gender gender = (Gender) getValue();
                return gender.name();
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(Gender.valueOf(text));
            }
        });
        binder.registerCustomEditor(byte[].class, "avatar", new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(value = "/updateaccount", method = RequestMethod.POST)
    public ModelAndView updateAccount(@SessionAttribute(value = "loggedUser") Account loggedUser,
                                      @ModelAttribute("account") Account account,
                                      HttpServletResponse resp) throws IOException, DaoException {
        if (!account.getId().equals(loggedUser.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You can't edit other accounts");
            return null;
        } else {
            if (account.getAvatar() == null || account.getAvatar().length == 0) {
                account.setAvatar(loggedUser.getAvatar());
            }

            for (Phone p : account.getPhones()) {
                p.setOwner(account);
            }

            service.update(account);

            return new ModelAndView("redirect:/account?id=" + account.getId());
        }
    }

    @RequestMapping(value = "/register")
    public ModelAndView register(@SessionAttribute(value = "loggedUser", required = false) Account loggedUser) {
        if (loggedUser == null) {
            return new ModelAndView("register");
        } else {
            return new ModelAndView("redirect:/logout");
        }
    }

    @RequestMapping(value = "/account_registration", method = RequestMethod.POST)
    public ModelAndView accountRegistration(@ModelAttribute("account") Account account) throws DaoException {
        service.update(account);

        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/search")
    public ModelAndView search(@RequestParam("q") String query) throws ServletException {
        ModelAndView modelAndView = new ModelAndView("searchResults");
        try (InputStreamReader is = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("search.properties"))) {
            Properties properties = new Properties();
            properties.load(is);
            int pageSize = Integer.parseInt(properties.getProperty("PAGE_SIZE"));
            long searchResultCount = service.getSearchResultCount(query);

            System.out.println(searchResultCount);

            List<AccountDTO> accounts = service.findByPartName(query, 1, pageSize);

            modelAndView.addObject("accounts", accounts);
            modelAndView.addObject("searchResultCount", searchResultCount);
            modelAndView.addObject("pageSize", pageSize);
            modelAndView.addObject("query", query);
        } catch (IOException e) {
            throw new ServletException(e);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/quickSearch")
    @ResponseBody
    public List<AccountDTO> quickSearch(@RequestParam("q") String query,
                                        @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (currentPage == null) {
            currentPage = 0;
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        return service.findByPartName(query, currentPage, pageSize);
    }
}
