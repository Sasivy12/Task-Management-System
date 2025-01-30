package com.example.effective_mobile.controller;

import com.example.effective_mobile.model.Comment;
import com.example.effective_mobile.model.Task;
import com.example.effective_mobile.service.CommentService;
import com.example.effective_mobile.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/task")
@Tag(name = "Task Management", description = "APIs for managing tasks and comments")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private CommentService commentService;

    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    @GetMapping("/")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation(summary = "Create a task", description = "Create a new task for a specific user")
    @PostMapping("/{user_id}")
    public void createTask(
            @Parameter(description = "User ID", required = true) @PathVariable("user_id") Long userId,
            @RequestBody Task task) {
        taskService.createTask(userId, task);
    }

    @Operation(summary = "Delete a task", description = "Delete a task by ID for a specific user")
    @DeleteMapping("/{user_id}/{task_id}")
    public void deleteTask(
            @Parameter(description = "User ID", required = true) @PathVariable("user_id") Long userId,
            @Parameter(description = "Task ID", required = true) @PathVariable("task_id") Long taskId) {
        taskService.deleteTask(userId, taskId);
    }

    @Operation(summary = "Update a task", description = "Update an existing task for a user")
    @PutMapping("/{user_id}/{task_id}")
    public void updateTask(
            @Parameter(description = "User ID", required = true) @PathVariable("user_id") Long userId,
            @Parameter(description = "Task ID", required = true) @PathVariable("task_id") Long taskId,
            @RequestBody Task updatedTask) {
        taskService.editTask(userId, taskId, updatedTask);
    }

    @Operation(summary = "Get a specific task", description = "Retrieve a specific task by ID for a user")
    @GetMapping("/{user_id}/{task_id}")
    public Optional<Task> getASpecificTask(
            @Parameter(description = "User ID", required = true)@PathVariable("user_id") Long userId,
            @Parameter(description = "Task ID", required = true) @PathVariable("task_id") Long taskId) {
        return taskService.getASpecificTask(userId, taskId);
    }

    @Operation(summary = "Create a comment", description = "Add a comment to a task")
    @PostMapping("/{user_id}/{task_id}")
    public Comment createComment(
            @Parameter(description = "User ID", required = true) @PathVariable("task_id") Long authorId,
            @Parameter(description = "Task ID", required = true) @PathVariable("user_id") Long taskId,
            @RequestBody Comment comment) {
        return commentService.createComment(authorId, taskId, comment);
    }

    @Operation(summary = "Delete a comment", description = "Delete a comment by ID")
    @DeleteMapping("/{user_id}/{task_id}/{comment_id}")
    public void deleteComment(
            @Parameter(description = "User ID", required = true) @PathVariable("comment_id") Long authorId,
            @Parameter(description = "Task ID", required = true) @PathVariable("user_id") Long taskId,
            @Parameter(description = "Comment ID", required = true) @PathVariable("task_id") Long commentId) {
        commentService.deleteComment(authorId, taskId, commentId);
    }

    @Operation(summary = "Get comments by task", description = "Retrieve all comments for a specific task")
    @GetMapping("/{user_id}/{task_id}/comment")
    public List<Comment> getCommentByTask(
            @Parameter(description = "User ID", required = true) @PathVariable("task_id") Long authorId,
            @Parameter(description = "User ID", required = true) @PathVariable("user_id") Long taskId) {
        return commentService.getCommentsByTask(authorId, taskId);
    }

    @Operation(summary = "Update a comment", description = "Update an existing comment for a task")
    @PutMapping("/{user_id}/{task_id}/{comment_id}")
    public void updateComment(
            @Parameter(description = "User ID", required = true) @PathVariable("comment_id") Long authorId,
            @Parameter(description = "Task ID", required = true) @PathVariable("user_id") Long taskId,
            @Parameter(description = "Comment ID", required = true) @PathVariable("task_id") Long commentId,
            @RequestBody Comment updatedComment) {
        commentService.editComment(authorId, taskId, commentId, updatedComment);
    }

    @Operation(summary = "Get comments by user", description = "Retrieve all comments made by a user for a task")
    @GetMapping("/{user_id}/{task_id}/comments/byuser")
    public List<Comment> getCommentsByUser(
            @Parameter(description = "User ID", required = true) @PathVariable("task_id") Long authorId,
            @Parameter(description = "Task ID", required = true) @PathVariable("user_id") Long taskId) {
        return commentService.getCommentsByUser(authorId, taskId);
    }
}
