package com.example.vacation.web.controllers;

import com.example.vacation.domain.model.binding.UserEditBindingModel;
import com.example.vacation.domain.model.binding.UserLoginBindingModel;
import com.example.vacation.service.UserService;
import com.example.vacation.domain.model.binding.UserRegisterBindingModel;
import com.example.vacation.domain.model.service.UserServiceModel;
import com.example.vacation.domain.model.view.UserEditViewModel;
import com.example.vacation.domain.model.view.UserProfileViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UsersController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UsersController(UserService userService, ModelMapper mapper) {
        this.userService = userService;
        this.modelMapper = mapper;
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("userLoginBindingModel")) {
            model.addAttribute("userLoginBindingModel", new UserLoginBindingModel());
        }
        return "users/login";
    }


    @PostMapping("/login")
    public String loginConfirm(@Valid @ModelAttribute("userLoginBindingModel")
                                       UserLoginBindingModel userLoginBindingModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userLoginBindingModel", userLoginBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userLoginBindingModel", bindingResult);
            return "users/login";
        }

        UserServiceModel user = this.userService.findByUsername(userLoginBindingModel.getUsername());

        if (user == null || !this.userService.passwordsAreEqual(userLoginBindingModel.getPassword(), user.getUsername())) {

            redirectAttributes.addFlashAttribute("userLoginBindingModel", userLoginBindingModel);
            redirectAttributes.addFlashAttribute("notFound", true);
            return "users/login";
        }

        httpSession.setAttribute("user", user);

        return "home/home";
    }

    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("userRegisterBindingModel")) {
            model.addAttribute("userRegisterBindingModel", new UserRegisterBindingModel());
        }

        return "users/register";
    }


    @PostMapping("/register")
    public String registerConfirm(@Valid @ModelAttribute("userRegisterBindingModel")
                                          UserRegisterBindingModel userRegisterBindingModel,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() || !userRegisterBindingModel.getPassword()
                .equals(userRegisterBindingModel.getConfirmPassword())) {

            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);

            return "redirect:/users/register";
        }

        this.userService.register(this.modelMapper
                .map(userRegisterBindingModel, UserServiceModel.class));

        return "redirect:/users/login";

    }

    @GetMapping("/profile")
    public ModelAndView profile(ModelAndView modelAndView){
        modelAndView
                .addObject("model",UserProfileViewModel.class);

        List<UserProfileViewModel> users = this.userService.getAllUsers().stream()
                .map(user -> this.modelMapper.map(user, UserProfileViewModel.class)).collect(Collectors.toList());

        modelAndView.addObject("users",users);
        return super.view("users/profile", modelAndView);
    }

    @GetMapping("/edit-profile")
    public ModelAndView editProfile(HttpSession session,ModelAndView modelAndView, @ModelAttribute(name = "model") UserEditViewModel model) {
        UserServiceModel userServiceModel = (UserServiceModel)(session.getAttribute("user"));
        model = this.modelMapper.map(userServiceModel, UserEditViewModel.class);
        model.setPassword(null);
        modelAndView.addObject("model", model);

        return super.view("users/edit-profile", modelAndView);
    }

    @PostMapping("/edit-profile")
    public ModelAndView editProfileConfirm(HttpSession session, ModelAndView modelAndView, @ModelAttribute UserEditBindingModel model) {
        if (!model.getPassword().equals(model.getConfirmPassword())){
            return super.view("users/edit-profile");
        }

        UserServiceModel newUser = this.userService.editUserProfile(this.modelMapper.map(model, UserServiceModel.class), model.getOldPassword());
        session.setAttribute("user",newUser);
        return super.redirect("/users/profile");
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:/";
    }
}