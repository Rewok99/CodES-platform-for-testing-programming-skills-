package com.rewok.codestudentstest.controllers;

import com.rewok.codestudentstest.models.MyUser;
import com.rewok.codestudentstest.repository.UserRepository;
import com.rewok.codestudentstest.services.AppService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/in")
@AllArgsConstructor
public class AppController {
    private AppService service;
    private UserRepository repository;


    //Главная страница
    @GetMapping("/main")
    public ModelAndView welcome(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("main");
        return modelAndView;
    }

    //Регистрация
    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    //Метод для регистрации
    @PostMapping("/new-user")
    public ResponseEntity<String> addUser(@RequestBody MyUser user){
        if (repository.findByName(user.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует!");
        }
        user.setRoles("ROLE_USER");
        service.addUser(user);
        return ResponseEntity.ok("Пользователь сохранен!");
    }

    //Смена пароля
    @GetMapping("/change-password")
    public ModelAndView showChangePassword(Principal principal) {
        String username = principal.getName();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("username", username);

        // Получить объект Authentication для текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Получить роль пользователя из объекта Authentication
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        modelAndView.addObject("role", role); // Добавляем роль в ModelAndView

        modelAndView.setViewName("change-password");
        return modelAndView;
    }


    //Метод для смены пароля
    @PostMapping("/change-password")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ModelAndView changePassword(@RequestParam("currentPassword") String currentPassword,
                                       @RequestParam("newPassword") String newPassword,
                                       Principal principal) {
        // Получить имя текущего пользователя
        String username = principal.getName();

        // Вызвать метод смены пароля из сервиса
        service.changePassword(username, currentPassword, newPassword);

        // Создать объект ModelAndView для перенаправления
        ModelAndView modelAndView = new ModelAndView("redirect:/in/main");


        // Вернуть объект ModelAndView
        return modelAndView;
    }

    @GetMapping("/admin-menu")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ModelAndView showAdminMenu() {
        List<MyUser> users = repository.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("users", users);
        modelAndView.setViewName("adminMenu");
        return modelAndView;
    }






}
