package com.gjjbook.servlet;

import com.gjjbook.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AbstractController {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    @Autowired
    protected AccountService service;
}
