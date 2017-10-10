package xyz.isnull.blog.core.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageErrorController implements ErrorController {


    @RequestMapping(value="/error")
    public String handleError(){
        return "403";
    }

    @Override
    public String getErrorPath() {
        return "403";
    }
}
