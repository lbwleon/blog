package xyz.isnull.blog.core.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class LoginCotroller {

    @RequestMapping({"/login"})
    public String login(){
        return "login";
    }

    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal Principal principal, Model model){
        model.addAttribute("name",principal.getName());
        return "admin/index";
    }

    @RequestMapping({"/index", "/"})
    public String index(){
        return "redirect:/pages/index";
    }

}
