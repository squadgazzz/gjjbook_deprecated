package com.gjjbook.servlet;

import com.gjjbook.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("loggedUser")
public class LoginController extends AbstractController {

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
// TODO: 04.11.2017 добавить логгирование в дао, эксепшены, сервис слой и тп
                logger.info("Someone tried to login with no luck"); // TODO: 30.10.2017 add IP
            }

        }

        return modelAndView;
    }
}
