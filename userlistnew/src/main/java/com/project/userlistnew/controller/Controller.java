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

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ModelAndView signIn(Model model,
                               @RequestParam String name,
                               @RequestParam String password) {

        String role = repository.signInUser(name, password);

        ModelAndView map = new ModelAndView("index");

        if(role.equals("admin") || role.equals("user"))
            return account(name, role);

        return map;
    }

    private ModelAndView account(String name, String role) {
        ModelAndView map = new ModelAndView("account");

        map.addObject("cars", autoRepository.getAuto(name));
        map.addObject("allauto",autoRepository.getAllAuto());

        return map;
    }

    @RequestMapping(value = "/signin/add/{id}", method = RequestMethod.GET)
    public ModelAndView addAuto (@PathVariable Integer id) {

        ModelAndView map = new ModelAndView("/signin");
        autoRepository.addCar(id);
        return map;
    }

    @RequestMapping(value = "/signin/del/{id}", method = RequestMethod.GET)
    public ModelAndView removeAuto (@PathVariable Integer id) {
        ModelAndView map = new ModelAndView("/signin");
        autoRepository.removeCar(id);
        return map;
    }
}
