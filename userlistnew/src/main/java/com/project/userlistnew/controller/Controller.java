package com.project.userlistnew.controller;

import com.project.userlistnew.model.User;
import com.project.userlistnew.repository.AutoRepository;
import com.project.userlistnew.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UserRepository repository;

    @Autowired
    private AutoRepository autoRepository;

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String startPage() {
        return "index";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signUp() {
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String newUser(Model model,
                          @RequestParam String name,
                          @RequestParam String surname,
                          @RequestParam String password,
                          @RequestParam String passwordConfirm,
                          @RequestParam String description)  {

        String result = repository.addUser(name, surname, password, passwordConfirm, description, 1);

        if (result.equals("Passwords don't match!")) {
            model.addAttribute("messageBad", "Passwords don't match!");
            return "signup";
        }

        if (result.equals("Short password!")) {
            model.addAttribute("messageBad", "Short password!");
            return "signup";
        }

        if(result.equals("Uncorrected password")) {
            model.addAttribute("messageBad", "Uncorrected password!");
        }


        if (result.equals("User already exists!")) {
            model.addAttribute("messageBad", "A person with this name already exists!");
            return "signup";
        }

        if(result.equals("Success")) {
            model.addAttribute("messageGood", "A person successfully added!");
            return "signup";
        }
        return "signup";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView getUsers() {
        ModelAndView map = new ModelAndView("users");
        List<User> list = repository.getAllUsers();
        map.addObject("users", list);
        return map;
    }

    @RequestMapping(value = "users/del/{id}", method = RequestMethod.GET)
    public String delUser(@PathVariable Integer id) {
        repository.removeUser(id);
        return "redirect:/users";
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public ModelAndView signIn(@RequestParam String name, @RequestParam String password, Model model) {
        String role = repository.signInUser(name, password);
        if(role.equals("none")) {
            ModelAndView map1 = new ModelAndView("index");
            map1.addObject("message", "Uncorrected login or password");
            return map1;
        } else {
            ModelAndView map2 = new ModelAndView("account");
            map2.addObject("cars", autoRepository.getAuto(name));
            map2.addObject("allauto",autoRepository.getAllAuto());
            return map2;
        }
    }

    @RequestMapping(value = "/account/add/{id}", method = RequestMethod.GET)
    public String addAuto (@PathVariable Integer id) {
        autoRepository.addCar(id);
        return "redirect:/account";
    }

    @RequestMapping(value = "/account/del/{id}", method = RequestMethod.GET)
    public String removeAuto (@PathVariable Integer id) {
        autoRepository.removeCar(id);
        return "redirect:/account";
    }

    @GetMapping(value = "/addcartodatabase")
    public String addCar () {
        return "addcar";
    }

    @PostMapping(value = "/sendcar")
    public String sendCar(@RequestParam String namecar, @RequestParam String imgcar) {

        autoRepository.sendCar(namecar, imgcar);

        return "addcar";
    }
}
