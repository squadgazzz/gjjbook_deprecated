package com.gjjbook.servlet;

import com.gjjbook.dao.DaoException;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.Phone;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.time.LocalDate;

@Controller
@SessionAttributes("loggedUser")
@MultipartConfig(maxFileSize = 1_617_721)
public class AccountController extends AbstractController {

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
        binder.registerCustomEditor(byte[].class, "avatar", new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(value = "/updateaccount", method = RequestMethod.POST)
    public ModelAndView updateAccount(@SessionAttribute(value = "loggedUser") Account loggedUser,
                                      @ModelAttribute("account") Account account,
                                      HttpServletResponse resp, HttpServletRequest req) throws IOException, DaoException {
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

            Account updatedAccount = service.update(account);
            ModelAndView modelAndView = new ModelAndView("redirect:/account?id=" + account.getId());
            modelAndView.addObject("loggedUser", updatedAccount);

            return modelAndView;
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
}
