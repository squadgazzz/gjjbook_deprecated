package com.gjjbook.servlet;

import com.gjjbook.domain.Account;
import com.gjjbook.domain.Phone;
import com.gjjbook.domain.PhoneType;
import com.gjjbook.domain.Sex;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@MultipartConfig(maxFileSize = 1_617_721)
public abstract class RegisterUpdateAccount extends AbstractServlet {

    protected void parseAccountData(HttpServletRequest req, Account account) throws ServletException, IOException {
        Part filePart = req.getPart("avatar");
        if (filePart.getSize() != 0) {
            byte[] image;
            try (InputStream inputStream = filePart.getInputStream()) {
                image = getBytesArray(inputStream);
            }
            account.setAvatar(image);
        }

        account.setName(req.getParameter("name"));
        account.setMiddleName(req.getParameter("middle_name"));
        account.setSurName(req.getParameter("surname"));
        account.setSex(Sex.valueOf(req.getParameter("gender")));
        account.setBirthDate(LocalDate.parse(req.getParameter("birth_date")));
        account.setPhones(getNewPhones(req));
        account.setHomeAddress(req.getParameter("home_address"));
        account.setWorkAddress(req.getParameter("work_address"));
        account.setEmail(req.getParameter("email"));
        account.setIcq(req.getParameter("icq"));
        account.setSkype(req.getParameter("skype"));
        account.setAdditionalInfo(req.getParameter("additional_info"));
        account.setPassword(req.getParameter("password"));
    }

    private List<Phone> getNewPhones(HttpServletRequest req) throws ServletException {
        List<Phone> newPhones = new LinkedList<>();
        String[] phoneTypes = req.getParameterValues("phone_type");
        String[] phoneNumbers = req.getParameterValues("phone");
        if (phoneNumbers.length != phoneTypes.length) {
            throw new ServletException("Phone types count doesn't match numbers count");
        }
        for (int i = 0; i < phoneNumbers.length; i++) {
            newPhones.add(new Phone(PhoneType.valueOf(phoneTypes[i]), phoneNumbers[i]));
        }

        return newPhones;
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
