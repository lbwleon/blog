package xyz.isnull.blog.core.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = Logger.getLogger(AdminController.class);

    //后台主页面
    @RequestMapping("/index")
    public String index(){

        return "admin/index";
    }
}
