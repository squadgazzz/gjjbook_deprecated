package com.gjjbook.servlet;

import com.gjjbook.AccountService;
import com.gjjbook.dao.DaoException;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.Phone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/gjjbook/accounts")
public class AccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountService service = null;
        try {
            service = new AccountService();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        StringBuilder sb = getAllAccountsTable(service);

        resp.getOutputStream().write(sb.toString().getBytes());
    }

    private StringBuilder getAllAccountsTable(AccountService service) {
        List<Account> accounts = null;
        try {
            accounts = service.getAll();
        } catch (DaoException e) {
            e.printStackTrace();
        } finally {
            try {
                service.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        StringBuilder sb = new StringBuilder();
        if (accounts != null && accounts.size() > 0) {
            sb.append("<table border = 1>");
            sb.append("<tr><th>Name</th><th>Surname</th><th>Phones</th><th>Home address</th></tr>");
            for (Account acc : accounts) {
                sb.append("<tr>");
                sb.append("<td>").append(acc.getName()).append("</td>");
                sb.append("<td>").append(acc.getSurName()).append("</td>");

                List<Phone> phones = acc.getPhones();
                if (phones != null && phones.size() > 0) {
                    sb.append("<td>");
                    for (Phone p : phones) {
                        sb.append(p.getType().name()).append(": ").append(p.getCountryCode()).append(p.getNumber()).append("<br/>");
                    }
                    sb.append("</td>");
                }
                sb.append("<td>").append(acc.getHomeAddress()).append("</td>");

                sb.append("</tr>");
            }
            sb.append("</table>");
        } else {
            sb.append("There're no accounts :(");
        }
        return sb;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
