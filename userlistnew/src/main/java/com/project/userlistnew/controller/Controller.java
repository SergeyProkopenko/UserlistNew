package com.project.userlistnew.controller;

import com.project.userlistnew.model.User;
import com.project.userlistnew.repository.AutoRepository;
import com.project.userlistnew.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
                          @RequestParam String description)  {

        boolean foo = repository.addUser(name, surname, password, description, 1);
        if (!foo) {
            model.addAttribute("messageBad", "A person with this name already exists!");
            return "signup";
        }
        model.addAttribute("messageGood", "A person successfully added!");
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
    public String signIn(@RequestParam String name, @RequestParam String password, Model model) {
        String role = repository.signInUser(name, password);
        if(role.equals("none")) {
            model.addAttribute("message", "Uncorrected login or password");
            return "index";
        }
        return account(name, model);
    }

    private String account(String name, Model model) {

        model.addAttribute("cars", autoRepository.getAuto(name));
        model.addAttribute("allauto",autoRepository.getAllAuto());
        return "account";
    }

    @RequestMapping(value = "/account/add/{id}", method = RequestMethod.GET)
    public String addAuto (@PathVariable Integer id) {
        autoRepository.addCar(id);
        return "account";
    }

    @RequestMapping(value = "/account/del/{id}", method = RequestMethod.GET)
    public String removeAuto (@PathVariable Integer id) {
        autoRepository.removeCar(id);
        return "account";
    }
}
