package com.example.sportshop.web.controllers;

import com.example.sportshop.domain.model.service.UserServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController extends BaseController{

    private final ModelMapper modelMapper;

    public HomeController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView index(HttpSession session){
        if (session.getAttribute("user") == null) {
            return super.view("home/index");
        }
        return super.view("home/home");
    }

    @GetMapping("/home")
    public String home(HttpSession httpSession){
        UserServiceModel user =this.modelMapper.map(httpSession.getAttribute("user"),UserServiceModel.class);
        httpSession.setAttribute("name", user.getUsername());
//        boolean userIsAdmin = user.getRole().getAuthority().equals("ADMIN");
//        httpSession.setAttribute("userIsAdmin",userIsAdmin);
        httpSession.setAttribute("user",user);
        return "home/home";
    }

}
