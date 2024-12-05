package com.example.effective_mobile.controller;

import com.example.effective_mobile.model.Task;
import com.example.effective_mobile.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public void createTask(@PathVariable("user_id") Long user_id, @RequestBody Task task)
    {
        taskService.createTask(user_id, task);
    }

    @DeleteMapping("/{user_id}/{task_id}")
    public void deleteTask(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId)
    {
        taskService.deleteTask(userId, taskId);
    }

    @PutMapping("/{user_id}/{task_id}")
    public void updateTask(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId, @RequestBody Task updatedTask)
    {
        taskService.editTask(userId, taskId, updatedTask);
    }

    @GetMapping("/{user_id}/{task_id}")
    public Optional<Task> getASpecificTask(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId)
    {
        return taskService.getASpecificTask(userId, taskId);
    }
}
