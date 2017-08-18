package com.gjjbook.servlet;

import com.gjjbook.AccountService;
import com.gjjbook.dao.DaoException;
import com.gjjbook.domain.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class Search extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountService service = (AccountService) req.getSession().getAttribute("accountService");
        List<Account> accounts;
        try {
            accounts = service.findByPartName(req.getParameter("q"));
        } catch (DaoException e) {
            throw new ServletException(e);
        }
        req.setAttribute("accounts", accounts);
        req.getServletContext().getRequestDispatcher("/WEB-INF/JSP/searchResults.jsp").forward(req, resp);
    }
}
