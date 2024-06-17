package com.rewok.codestudentstest.controllers;

import com.rewok.codestudentstest.models.Tasks;
import com.rewok.codestudentstest.repository.TasksRepository;
import com.rewok.codestudentstest.services.CodeExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;

@Controller
public class TasksController {

    private final TasksRepository tasksRepository;


    @Autowired
    public TasksController(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    //Задачи
    @GetMapping("/in/tasks")
    public ModelAndView showTasks() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("tasks");
        return modelAndView;
    }

    @GetMapping("/in/tasks/task")
    public ModelAndView showTaskPage(@RequestParam("id") Long taskId) {
        // Найдите задачу по идентификатору из базы данных
        Optional<Tasks> taskOptional = tasksRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            Tasks task = taskOptional.get();

            // Создайте объект ModelAndView и передайте данные о задаче на страницу
            ModelAndView modelAndView = new ModelAndView("task");
            modelAndView.addObject("task", task);


            return modelAndView;
        } else {
            // Если задача не найдена, верните страницу с сообщением об ошибке или обработайте ее по своему усмотрению
            ModelAndView modelAndView = new ModelAndView("error_page");
            modelAndView.addObject("message", "Задача не найдена!");
            return modelAndView;
        }
    }

    @PostMapping("/in/tasks/task/submit")
    @ResponseBody
    public String executeCode(@RequestBody String code, @RequestParam("testClass") String testClass, Principal principal) {
        String username = principal.getName(); // Получаем имя текущего пользователя
        // Здесь вызываем метод для выполнения кода и возвращаем результат
        String result = CodeExecutionService.executeCode(code, testClass, username);
        return result;
    }


}