package com.gjjbook.servlet;

import com.gjjbook.service.AccountService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public abstract class AbstractServlet extends HttpServlet {

    protected AccountService accountService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        accountService = applicationContext.getBean(AccountService.class);
    }
}
