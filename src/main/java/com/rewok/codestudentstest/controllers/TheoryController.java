package com.rewok.codestudentstest.controllers;

import com.rewok.codestudentstest.models.Tasks;
import com.rewok.codestudentstest.repository.TasksRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class TheoryController {

    private final TasksRepository tasksRepository;

    public TheoryController(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    @GetMapping("/in/theory")
    public ModelAndView showTests() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("theory");
        return modelAndView;
    }


    @GetMapping("/in/theory/task")
    public ModelAndView showTaskPage(@RequestParam("id") Long taskId) {
        // Найдите задачу по идентификатору из базы данных
        Optional<Tasks> tasksOptional = tasksRepository.findById(taskId);

        if (tasksOptional.isPresent()) {
            Tasks tasks = tasksOptional.get();

            // Создайте объект ModelAndView и передайте данные о задаче на страницу
            ModelAndView modelAndView = new ModelAndView("theory-task");
            modelAndView.addObject("theory", tasks);


            return modelAndView;
        } else {
            // Если задача не найдена, верните страницу с сообщением об ошибке или обработайте ее по своему усмотрению
            ModelAndView modelAndView = new ModelAndView("error_page");
            modelAndView.addObject("message", "Задача не найдена!");
            return modelAndView;
        }
    }
}
