package com.example.effective_mobile.service;

import com.example.effective_mobile.model.Task;
import com.example.effective_mobile.model.User;
import com.example.effective_mobile.repository.TaskRepository;
import com.example.effective_mobile.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService
{
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public void createTask(Long user_id, Task task)
    {
        Optional<User> authorOptional = userRepository.findById(user_id);
        if(authorOptional.isPresent())
        {
            User author = authorOptional.get();
            task.setAuthor(author);
            taskRepository.save(task);
        }
        else
        {
            throw new EntityNotFoundException("User not found");
        }
    }

    public void deleteTask(Long userId, Long taskId)
    {
        String currentUserEmail = getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(currentUserEmail);

        if(currentUser == null)
        {
            throw new EntityNotFoundException("User not found");
        }

        Task task = taskRepository.findByAuthorIdAndId(userId, taskId);
        if (task == null)
        {
            throw new EntityNotFoundException("Task not found");
        }

        if(currentUser.getRole().equals("ADMIN") || task.getAuthor().getId().equals(currentUser.getId()))
        {
            taskRepository.delete(task);
        }
        else
        {
            throw new SecurityException("You do not have permission to edit this task");
        }
    }

    public Task editTask(Long userId, Long taskId, Task updatedTask)
    {
        String currentUserEmail = getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(currentUserEmail);

        if(currentUser == null)
        {
            throw new EntityNotFoundException("User not found");
        }

        Task task = taskRepository.findByAuthorIdAndId(userId, taskId);
        if (task == null)
        {
            throw new EntityNotFoundException("Task not found");
        }

        if(currentUser.getRole().equals("ADMIN") || task.getAuthor().getId().equals(currentUser.getId()))
        {
            task.setHeader(updatedTask.getHeader());
            task.setDescription(updatedTask.getDescription());
            task.setStatus(updatedTask.getStatus());
            task.setPriority(updatedTask.getPriority());

            taskRepository.save(task);

            return task;
        }
        else
        {
            throw new SecurityException("You do not have permission to edit this task");
        }
    }

    private String getCurrentUserEmail()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
        {
            return ((UserDetails) principal).getUsername();
        }
        else
        {
            return principal.toString();
        }
    }

    public List<Task> getAllTasks()
    {
        return taskRepository.findAll();
    }

    public Optional<Task> getASpecificTask(Long userId, Long taskId)
    {
        return taskRepository.findById(taskId);
    }
}
