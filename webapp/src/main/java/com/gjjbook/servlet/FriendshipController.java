package com.gjjbook.servlet;


import com.gjjbook.dao.DaoException;
import com.gjjbook.domain.Account;
import com.gjjbook.domain.Friend;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("loggedUser")
public class FriendshipController extends AbstractController {

    @RequestMapping(value = "/getFriendByPk")
    @ResponseBody
    public Friend getFriendByPk(@SessionAttribute("loggedUser") Account accountOne,
                                @RequestParam(value = "accountTwo") Integer accountTwoId) throws DaoException {
        return service.getFriendByPk(accountOne, service.getByPk(accountTwoId));
    }

    @RequestMapping(value = "/addFriend")
    @ResponseBody
    public boolean addFriend(@SessionAttribute("loggedUser") Account accountOne,
                             @RequestParam(value = "accountTwo") Integer accountTwo) throws DaoException {
        service.requestFriend(accountOne, service.getByPk(accountTwo));

        return true;
    }

    @RequestMapping(value = "/removeFriend")
    @ResponseBody
    public boolean removeFriend(@SessionAttribute("loggedUser") Account accountOne,
                                @RequestParam(value = "accountTwo") Integer accountTwo) throws DaoException {
        service.removeFriend(accountOne, service.getByPk(accountTwo));

        return true;
    }

    @RequestMapping(value = "/confirmFriend")
    @ResponseBody
    public boolean confirmFriend(@SessionAttribute("loggedUser") Account accountOne,
                                 @RequestParam(value = "accountTwo") Integer accountTwo) throws DaoException {
        service.acceptFriend(accountOne, service.getByPk(accountTwo));

        return true;
    }

    @RequestMapping(value = "/declineFriend")
    @ResponseBody
    public boolean declineFriend(@SessionAttribute("loggedUser") Account accountOne,
                                 @RequestParam(value = "accountTwo") Integer accountTwo) throws DaoException {
        service.declineFriend(accountOne, service.getByPk(accountTwo));

        return true;
    }
}
