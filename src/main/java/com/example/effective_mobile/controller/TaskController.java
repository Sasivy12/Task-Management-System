package com.example.effective_mobile.controller;

import com.example.effective_mobile.model.Task;
import com.example.effective_mobile.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController
{
    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public List<Task> getAllTasks()
    {
        return taskService.getAllTasks();
    }

    @PostMapping("/{user_id}")
    public void createTask(@PathVariable  Long user_id, @RequestBody Task task)
    {
        taskService.createTask(user_id, task);
    }
}
