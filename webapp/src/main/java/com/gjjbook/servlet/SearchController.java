package com.gjjbook.servlet;

import com.gjjbook.domain.Account;
import com.gjjbook.domain.DTO.AccountDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import java.util.List;

@Controller
public class SearchController extends AbstractController {

    @Value("${PAGE_SIZE}")
    private int pageSize;

    @Value("${AUTOCOMPLETE_SIZE}")
    private int autocompleteSize;

    @RequestMapping(value = "/search")
    public ModelAndView search(@RequestParam("q") String query) throws ServletException {
        ModelAndView modelAndView = new ModelAndView("searchResults");
        // done: 04.11.2017 чтобы спринг читал файл и инжектил, аннотация @Value, 2 проперти - размер автокомплита и страницы поиска
        long searchResultCount = service.getSearchResultCount(query);

        List<AccountDTO> accounts = service.findByPartName(query, 1, pageSize);

        modelAndView.addObject("accounts", accounts);
        modelAndView.addObject("searchResultCount", searchResultCount);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("query", query);

        return modelAndView;
    }

    @RequestMapping(value = "/quickSearch")
    @ResponseBody
    public List<AccountDTO> quickSearch(@RequestParam("q") String query,
                                        @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                        @RequestParam(value = "pageSize", required = false) Integer newPageSize) {
        if (newPageSize == null) {
            newPageSize = autocompleteSize;
        }

        if (currentPage == null) {
            currentPage = 1;
        }

        return service.findByPartName(query, currentPage, newPageSize);
    }

    @RequestMapping(value = "/friends")
    public ModelAndView showFriends(@RequestParam("id") Integer idParam,
                                    @SessionAttribute("loggedUser") Account loggedUser) {
        ModelAndView modelAndView;
        List<Account> friends;
        if (idParam.equals(loggedUser.getId())) {
            friends = service.findAccountFriends(loggedUser);
            modelAndView = new ModelAndView("showMyFriends");
        } else {
            friends = service.findAccountFriends(service.getByPk(idParam));
            modelAndView = new ModelAndView("showFriends");
        }
        modelAndView.addObject("accounts", friends);

        return modelAndView;
    }
}
