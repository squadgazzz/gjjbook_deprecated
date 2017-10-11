package com.gjjbook.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        session.invalidate();
        Cookie emailCookie = new Cookie("email", "");
        Cookie passwordCookie = new Cookie("password", "");
        emailCookie.setMaxAge(0);
        passwordCookie.setMaxAge(0);
        response.addCookie(emailCookie);
        response.addCookie(passwordCookie);

        return new ModelAndView("redirect:" + request.getContextPath() + "/login");
    }
}
