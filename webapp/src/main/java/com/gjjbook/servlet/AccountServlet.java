package com.gjjbook.servlet;

import com.gjjbook.domain.Account;
import com.gjjbook.service.AccountService;
import com.gjjbook.service.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/account")
public class AccountServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountService service = (AccountService) req.getSession().getAttribute("accountService");
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            try {
                Account account = service.getByPk(Integer.valueOf(idParam));
                if (account == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                } else {
                    req.setAttribute("account", account);
                    req.setAttribute("avatar", service.getEncodedAvatar(account));
                }
            } catch (ServiceException e) {
                throw new ServletException(e);
            }
            req.getServletContext().getRequestDispatcher("/WEB-INF/JSP/account.jsp").forward(req, resp);
        }
    }
}
