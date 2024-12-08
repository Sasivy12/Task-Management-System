package com.example.effective_mobile.controller;

import com.example.effective_mobile.model.Comment;
import com.example.effective_mobile.model.Task;
import com.example.effective_mobile.service.CommentService;
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

    @Autowired
    private CommentService commentService;

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
    public void updateTask(@PathVariable("user_id") Long userId,
                           @PathVariable("task_id") Long taskId, @RequestBody Task updatedTask)
    {
        taskService.editTask(userId, taskId, updatedTask);
    }

    @GetMapping("/{user_id}/{task_id}")
    public Optional<Task> getASpecificTask(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId)
    {
        return taskService.getASpecificTask(userId, taskId);
    }

    @PostMapping("/{user_id}/{task_id}")
    public Comment createComment(@PathVariable("task_id") Long taskId,
                                 @PathVariable("user_id") Long authorId, @RequestBody Comment comment)
    {
        return commentService.createComment(taskId, authorId, comment);
    }

    @DeleteMapping("/{user_id}/{task_id}/{comment_id}")
    public void deleteComment(@PathVariable("comment_id") Long commentId,
                              @PathVariable("user_id") Long authorId, @PathVariable("task_id") Long taskId)
    {
        commentService.deleteComment(commentId, authorId, taskId);
    }

    @GetMapping("/{user_id}/{task_id}/comment")
    public List<Comment> getCommentByTask(@PathVariable("task_id") Long taskId,
                                          @PathVariable("user_id") Long authorId)
    {
        return commentService.getCommentsByTask(taskId, authorId);
    }

    @PutMapping("/{user_id}/{task_id}/{comment_id}")
    public void updateComment(@PathVariable("comment_id") Long commentId,
                              @PathVariable("user_id") Long authorId, @PathVariable("task_id") Long taskId,
                              @RequestBody Comment updatedComment)
    {
        commentService.editComment(commentId, authorId, taskId, updatedComment);
    }

    @GetMapping("/{user_id}/{task_id}/comments/byuser")
    public List<Comment> getCommentsByUser(@PathVariable("task_id") Long taskId,
                                           @PathVariable("user_id") Long authorId)
    {
        return commentService.getCommentsByUser(taskId, authorId);
    }
}
