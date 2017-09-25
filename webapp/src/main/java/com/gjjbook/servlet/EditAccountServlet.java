package com.gjjbook.servlet;

import com.gjjbook.domain.Account;
import com.gjjbook.service.AccountService;
import com.gjjbook.service.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/editaccount")
public class EditAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountService service = (AccountService) req.getSession().getAttribute("accountService");
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else if (!Integer.valueOf(idParam).equals(((Account) req.getSession().getAttribute("loggedUser")).getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You can't edit other accounts");
        } else {
            try {
                Account account = service.getByPk(Integer.valueOf(idParam));
                if (account == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Account not found");
                } else {
                    req.setAttribute("account", account);
                    req.setAttribute("avatar", service.getEncodedAvatar(account));
//                    req.setAttribute("password", service.getPassword(account));
                }
            } catch (ServiceException e) {
                throw new ServletException(e);
            }
            req.getServletContext().getRequestDispatcher("/WEB-INF/JSP/editAccount.jsp").forward(req, resp);
        }
    }
}
