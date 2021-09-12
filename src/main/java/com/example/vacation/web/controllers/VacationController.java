package com.example.vacation.web.controllers;

import com.example.vacation.service.UserService;
import com.example.vacation.service.VacationService;
import com.example.vacation.domain.model.binding.VacationAddBindingModel;
import com.example.vacation.domain.model.binding.VacationEditBindingModel;
import com.example.vacation.domain.model.service.UserServiceModel;
import com.example.vacation.domain.model.service.VacationServiceModel;
import com.example.vacation.domain.model.view.UserProfileViewModel;
import com.example.vacation.domain.model.view.VacationViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vacations")
public class VacationController extends BaseController{

    private final ModelMapper modelMapper;
    private final VacationService vacationService;
    private final UserService userService;

    public VacationController(ModelMapper modelMapper, VacationService vacationService, UserService userService) {
        this.modelMapper = modelMapper;
        this.vacationService = vacationService;
        this.userService = userService;
    }

//    @GetMapping("/vacation")
//    public String vacationGet(Model model, HttpSession session){
//        UserServiceModel user =this.modelMapper.map(session.getAttribute("user"),UserServiceModel.class);
//
//        List<VacationViewModel> vacations = this.vacationService.getAllVacations()
//                .stream()
//                .map(vacation -> {
//                    VacationViewModel vacationViewModel = this.modelMapper.map(vacation, VacationViewModel.class);
//
//                    return vacationViewModel;
//                }).collect(Collectors.toList());
//
//        vacations = vacations.stream()
//                .filter(v-> v.getUsername().equals(user.getUsername()))
//                .collect(Collectors.toList());
//
//        model.addAttribute("vacations", vacations);
//        session.setAttribute("name", user.getUsername());
//        return "vacation";
//
//    }

    @PostMapping("/vacation")
    public String vacationPost(Model model, HttpSession session,
                               @ModelAttribute("userProfileViewModel") UserProfileViewModel userProfileViewModel){
        UserServiceModel user =this.modelMapper.map(session.getAttribute("user"),UserServiceModel.class);

        UserServiceModel user1 = this.modelMapper.map(userProfileViewModel,UserServiceModel.class);

        List<VacationViewModel> vacations = this.vacationService.getAllVacations()
                .stream()
                .map(vacation -> {
                    VacationViewModel vacationViewModel = this.modelMapper.map(vacation, VacationViewModel.class);
                    return vacationViewModel;
                }).collect(Collectors.toList());

        vacations = vacations.stream()
                .filter(v-> v.getUsername().equals(user1.getUsername()))
                .collect(Collectors.toList());

        model.addAttribute("vacations", vacations);
        session.setAttribute("name", user.getUsername());
        return "vacation";

    }

    @GetMapping("/vacations")
    public String vacationsGet(Model model, HttpSession session){
        UserServiceModel user =this.modelMapper.map(session.getAttribute("user"),UserServiceModel.class);

        List<VacationViewModel> vacations = this.vacationService.getAllVacations()
                .stream()
                .map(vacation -> {
                    VacationViewModel vacationViewModel = this.modelMapper.map(vacation, VacationViewModel.class);

                    return vacationViewModel;
                }).collect(Collectors.toList());

        model.addAttribute("vacations", vacations);
        session.setAttribute("name", user.getUsername());
        return "vacations";

    }

    @GetMapping("/add")
    public String addVacationGet(Model model, HttpSession session){
        if(!model.containsAttribute("vacationAddBindingModel")){
            model.addAttribute("vacationAddBindingModel",new VacationAddBindingModel());
            List<UserServiceModel> users = this.userService.getAllUsers();
            List<String> usernames = this.userService.getAllUsernamesStrings(users);
            model.addAttribute("usernames",usernames);
        }

        if(session.getAttribute("user") == null){
            return "home/index";
        }
        return "vacation-add";

    }

    @PostMapping("/add")
    public String addVacationPost(@Valid @ModelAttribute("vacationAddBindingModel") VacationAddBindingModel vacationAddBindingModel,
                             @NotNull BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             HttpSession httpSession){

        if (httpSession.getAttribute("user") == null) {
            return "redirect:/";
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("vacationAddBindingModel", vacationAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.vacationAddBindingModel", bindingResult);
            return "vacation-add";
        }

        VacationServiceModel vacation = this.modelMapper.map(vacationAddBindingModel, VacationServiceModel.class);
        UserServiceModel user = this.userService.findByUsername(vacation.getUsername());
        if(user == null){
             return "redirect:/vacations/add";
         }

        this.vacationService.addVacation(vacation);
        return "redirect:/";
    }

    @GetMapping("/edit-vacation")
    public ModelAndView editVacationGet(HttpSession session,ModelAndView modelAndView,@Valid @ModelAttribute("vacationEditBindingModel") VacationEditBindingModel vacationEditBindingModel) {
        UserServiceModel userServiceModel = (UserServiceModel)(session.getAttribute("user"));

       VacationServiceModel vacationServiceModel = this.vacationService.getByUsername(userServiceModel.getUsername());
       vacationServiceModel.setStartDate(userServiceModel.getStartDate());
       vacationServiceModel.setEndDate(userServiceModel.getEndDate());

        vacationEditBindingModel = this.modelMapper.map(vacationServiceModel,VacationEditBindingModel.class);


//       modelAndView.addObject("vacationViewModel", vacationViewModel);
       modelAndView.addObject("vacationEditBindingModel", vacationEditBindingModel);

       return super.view("/edit-vacation", modelAndView);
    }

    @PostMapping("/edit-vacation")
    public ModelAndView editVacationPost(HttpSession session, ModelAndView modelAndView, @ModelAttribute VacationEditBindingModel model) {
       VacationServiceModel newVacation = this.modelMapper.map(model, VacationServiceModel.class);
        this.vacationService.editVacation(newVacation);
        return super.redirect("/vacations/vacations");
    }

    @GetMapping("/details")
    public ModelAndView details(@RequestParam("id")String id, ModelAndView modelAndView){
        modelAndView.addObject("vacation",this.vacationService.findById(id));
        modelAndView.setViewName("vacation-details");
        return modelAndView;
    }

    @PostMapping("/details")
    public ModelAndView detailsPost(@ModelAttribute VacationEditBindingModel model){
        VacationServiceModel newVacation = this.modelMapper.map(model, VacationServiceModel.class);
        this.vacationService.editVacation(newVacation);
        return super.redirect("/vacations/vacations");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")String id){
        this.vacationService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/vacation")
    public String userVacations(@RequestParam("id")String id, Model model){
        model.addAttribute("user",this.userService.getById(id));
//        modelAndView.setViewName("vacation");
//        return modelAndView;

        UserServiceModel user =this.modelMapper.map(this.userService.getById(id),UserServiceModel.class);

        List<VacationViewModel> vacations = this.vacationService.getAllVacations()
                .stream()
                .filter(v -> v.getUsername().equals(user.getUsername()))
                .map(vacation -> {
                    VacationViewModel vacationViewModel = this.modelMapper.map(vacation, VacationViewModel.class);

                    return vacationViewModel;
                }).collect(Collectors.toList());

        model.addAttribute("vacations", vacations);
        return "vacation";

    }

}
