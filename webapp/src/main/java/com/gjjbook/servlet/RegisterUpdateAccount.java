package com.gjjbook.servlet;

import com.gjjbook.domain.Account;
import com.gjjbook.domain.Phone;
import com.gjjbook.domain.PhoneType;
import com.gjjbook.domain.Sex;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class RegisterUpdateAccount extends HttpServlet {
//    protected String password; // done: 11.08.2017 сделать сервлет стейтлесс

    protected void parseAccountData(HttpServletRequest req, Account account) throws ServletException {
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
        /*Map<String, String[]> paramsMap = req.getParameterMap();
        for (Map.Entry<String, String[]> entry : paramsMap.entrySet()) {
            switch (entry.getKey()) { // done: 11.08.2017 сделать через getParam("")
                case "name":
                    account.setName(entry.getValue()[0]);
                    break;
                case "middle_name":
                    System.out.println(entry.getValue()[0]);
                    account.setMiddleName(entry.getValue()[0]);
                    break;
                case "surname":
                    account.setSurName(entry.getValue()[0]);
                    break;
                case "gender":
                    account.setSex(Sex.valueOf(entry.getValue()[0]));
                    break;
                case "birth_date":
                    System.out.println(entry.getValue()[0]);
                    LocalDate ld = LocalDate.parse(entry.getValue()[0]);
                    System.out.println(ld.toString());
                    account.setBirthDate(LocalDate.parse(entry.getValue()[0]));
                    break;
                case "phone_type":
                    String[] types = entry.getValue();
                    for (String t : types) {
                        newPhones.add(new Phone(PhoneType.valueOf(t), null));
                    }
                    break;
                case "phone":
                    String[] numbers = entry.getValue();
                    if (numbers.length > 0) {
                        for (int i = 0; i < numbers.length; i++) {
                            newPhones.get(i).setNumber(numbers[i]);
                        }
                    }
                    break;
                case "home_address":
                    account.setHomeAddress(entry.getValue()[0]);
                    break;
                case "work_address":
                    account.setWorkAddress(entry.getValue()[0]);
                    break;
                case "email":
                    account.setEmail(entry.getValue()[0]);
                    break;
                case "icq":
                    account.setIcq(entry.getValue()[0]);
                    break;
                case "skype":
                    account.setSkype(entry.getValue()[0]);
                    break;
                case "additional_info":
                    account.setAdditionalInfo(entry.getValue()[0]);
                    break;
                case "password":
                    password = entry.getValue()[0];
                    break;
            }
        }
        account.setPhones(newPhones);*/
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
}
