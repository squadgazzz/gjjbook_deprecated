package com.gjjbook.servlet;

import com.gjjbook.domain.Account;
import com.gjjbook.domain.Phone;
import com.gjjbook.domain.PhoneType;
import com.gjjbook.service.AccountService;
import com.gjjbook.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("loggedUser")
@MultipartConfig(maxFileSize = 1_617_721)
public class MainController {

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
                                          HttpServletResponse response, HttpServletRequest request) throws ServletException {
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
            try {
                if (service.isPasswordMatch(email, password, isEncrypted)) {
                    Account loggedUser = service.getByEmail(email);
                    if (rememberMe != null) {
                        response.addCookie(new Cookie("email", email));
                        if (!isEncrypted) {
                            password = service.getPassword(loggedUser);
                        }
                        response.addCookie(new Cookie("password", password));
                    }

                    if (path == null) {
                        path = "/account?id=" + loggedUser.getId();
                    }
                    modelAndView = new ModelAndView("redirect:" + request.getContextPath() + path);
                    modelAndView.addObject("loggedUser", loggedUser);
                } else {
                    modelAndView = new ModelAndView("/login");
                    modelAndView.addObject("errMsg", "Email/password does not match");
                }
            } catch (ServiceException e) {
                throw new ServletException(e);
            }
        }

        return modelAndView;
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ModelAndView showAccount(@RequestParam("id") Integer idParam,
                                    HttpServletResponse resp) throws IOException, ServletException {
        ModelAndView modelAndView = new ModelAndView("accountView");

        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        } else {
            try {
                Account account = service.getByPk(idParam);
                if (account == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return null;
                } else {
                    modelAndView.addObject("account", account);
                    modelAndView.addObject("avatar", service.getEncodedAvatar(account));
                }
            } catch (ServiceException e) {
                throw new ServletException(e);
            }

            return modelAndView;
        }
    }

    @RequestMapping(value = "/editaccount", method = RequestMethod.GET)
    public ModelAndView editAccount(@RequestParam(value = "id") Integer idParam,
                                    @SessionAttribute("loggedUser") Account loggedUser,
                                    HttpServletResponse resp) throws IOException, ServletException {
        ModelAndView modelAndView = new ModelAndView("editAccount");

        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else if (!idParam.equals(loggedUser.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You can't edit other accounts");
        } else {
            try {
                Account account = service.getByPk(idParam);
                if (account == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return null;
                } else {
                    modelAndView.addObject("account", account);
                    modelAndView.addObject("avatar", service.getEncodedAvatar(account));
                }
            } catch (ServiceException e) {
                throw new ServletException(e);
            }
        }

        return modelAndView;
    }

    @InitBinder("account")
    public void customAccountModel(WebDataBinder binder) {
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
//        binder.registerCustomEditor(ArrayList.class, "phones", new CustomCollectionEditor(ArrayList.class));
        binder.registerCustomEditor(byte[].class, "avatar", new ByteArrayMultipartFileEditor());
    }

    // done: 06.10.2017 с аттрибутом enctype="multipart/form-data" не работает, пришлось добавить MultipartHttpServletRequest
    // donegi: 09.10.2017 попробовать запустить через tomcat cmd
    // done: 09.10.2017 починить аватарки в поиске
    @RequestMapping(value = "/updateaccount", method = RequestMethod.POST)
    public ModelAndView updateAccount(@SessionAttribute(value = "loggedUser") Account loggedUser,
                                      @ModelAttribute("account") Account account,
                                      HttpServletResponse resp,
                                      MultipartHttpServletRequest request) throws IOException, ServletException {
        if (!account.getId().equals(loggedUser.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You can't edit other accounts");
            return null;
        } else {
            // done: 09.10.2017 переделать инпуты на jsp и телефоны собрать через ModelAttribute
            if (account.getAvatar() == null || account.getAvatar().length == 0) {
                account.setAvatar(loggedUser.getAvatar());
            }

            try {
                service.update(account);
            } catch (ServiceException e) {
                throw new ServletException(e);
            }

            return new ModelAndView("redirect:" + request.getContextPath() + "/account?id=" + account.getId());
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register() {
        return new ModelAndView("/register");
    }

    @RequestMapping(value = "/account_registration", method = RequestMethod.POST)
    public ModelAndView accountRegistration(@ModelAttribute("account") Account account,
                                            HttpServletRequest request) throws ServletException {
        try {
            account.setPhones(getNewPhones(request));
            service.create(account);
        } catch (ServiceException e) {
            throw new ServletException(e);
        }

        return new ModelAndView("redirect:" + request.getContextPath() + "/login");
    }

    @RequestMapping(value = "/search")
    public ModelAndView search(@RequestParam("q") String query) throws ServletException {
        ModelAndView modelAndView = new ModelAndView("/searchResults");
        try {
            List<Account> accounts = service.findByPartName(query);
            Map<Integer, String> encodedAvatars = new HashMap<>();
            for (Account a : accounts) {
                encodedAvatars.put(a.getId(), service.getEncodedAvatar(a));
            }
            modelAndView.addObject("accounts", accounts);
            modelAndView.addObject("encodedAvatars", encodedAvatars);
        } catch (ServiceException e) {
            throw new ServletException(e);
        }

        return modelAndView;
    }

    private List<Phone> getNewPhones(HttpServletRequest req) throws ServletException {
        List<Phone> newPhones = new LinkedList<>();
        String[] phoneTypes = req.getParameterValues("type");
        String[] phoneNumbers = req.getParameterValues("number");
        if (phoneNumbers.length != phoneTypes.length) {
            throw new ServletException("Phone types count doesn't match numbers count");
        }
        for (int i = 0; i < phoneNumbers.length; i++) {
            newPhones.add(new Phone(PhoneType.valueOf(phoneTypes[i]), phoneNumbers[i]));
        }

        return newPhones;
    }
}
