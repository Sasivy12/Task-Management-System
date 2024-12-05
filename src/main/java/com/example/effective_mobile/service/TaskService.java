package com.example.effective_mobile.service;

import com.example.effective_mobile.model.Task;
import com.example.effective_mobile.model.User;
import com.example.effective_mobile.repository.TaskRepository;
import com.example.effective_mobile.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
            System.out.println("This user does not exist");
        }
    }

    public void deleteTask(Long userId, Long taskId)
    {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if(taskOptional.isPresent())
        {
            taskRepository.deleteById(taskId);
        }
        else
        {
            System.out.println("This task does not exist");
        }
    }

    public Task editTask(Long userId, Long taskId, Task updatedTask)
    {
        Task task = taskRepository.findByAuthorIdAndId(userId, taskId);
        if(task != null)
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
            throw new EntityNotFoundException("This task does not exist");
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
