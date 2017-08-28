package com.gjjbook.servlet;

import com.gjjbook.AccountService;
import com.gjjbook.ServiceException;
import com.gjjbook.dao.DaoException;
import com.gjjbook.domain.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/updateavatar")
@MultipartConfig(maxFileSize = 1_617_721)
public class UpdateAvatar extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account account = (Account) req.getSession().getAttribute("loggedUser");
        Part filePart = req.getPart("avatar");
        if (filePart != null) {
            InputStream inputStream = filePart.getInputStream();
            byte[] image = getBytesArray(inputStream);

            AccountService service = (AccountService) req.getSession().getAttribute("accountService");
            try {
                service.setAvatar(account, image);
            } catch (ServiceException e) {
                throw new ServletException(e);
            }
        }

        resp.sendRedirect("/editaccount?id=" + account.getId());
    }

    private byte[] getBytesArray(InputStream inputStream) throws IOException {
        byte[] image;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int next = inputStream.read();
            while (next > -1) {
                bos.write(next);
                next = inputStream.read();
            }
            image = bos.toByteArray();
        }
        return image;
    }
}
