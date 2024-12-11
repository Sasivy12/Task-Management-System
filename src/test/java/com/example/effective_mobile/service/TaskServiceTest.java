package com.example.effective_mobile.service;

import com.example.effective_mobile.model.Task;
import com.example.effective_mobile.model.User;
import com.example.effective_mobile.repository.TaskRepository;
import com.example.effective_mobile.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        task = new Task();
        task.setId(1L);
        task.setHeader("Task Header");
        task.setDescription("Task Description");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setRole("USER");
    }

    @BeforeEach
    void setUpSecurityContext() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    void testCreateTask_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.createTask(1L, task);

        verify(taskRepository, times(1)).save(task);
        assertEquals(user, task.getAuthor());
    }

    @Test
    void testCreateTask_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> taskService.createTask(1L, task));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testDeleteTask_Success() {
        // Arrange
        User author = new User();
        author.setId(1L);

        task.setAuthor(author); // Ensure the Task has an author

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(taskRepository.findByAuthorIdAndId(1L, 1L)).thenReturn(task);

        // Act
        taskService.deleteTask(1L, 1L);

        // Assert
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void testDeleteTask_TaskNotFound() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(taskRepository.findByAuthorIdAndId(1L, 1L)).thenReturn(null);

        SecurityContextHolder.setContext(securityContext);

        // Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class, () -> taskService.deleteTask(1L, 1L));
        assertEquals("Task not found", exception.getMessage());
    }


    @Test
    void testEditTask_Success() {
        // Arrange
        User author = new User();
        author.setId(1L);

        task.setAuthor(author); // Ensure the Task has an author

        Task updatedTask = new Task();
        updatedTask.setHeader("Updated Header");
        updatedTask.setDescription("Updated Description");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(taskRepository.findByAuthorIdAndId(1L, 1L)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task result = taskService.editTask(1L, 1L, updatedTask);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Header", result.getHeader());
        assertEquals("Updated Description", result.getDescription());
    }


    @Test
    void testEditTask_InsufficientPermissions() {
        // Arrange
        User anotherUser = new User();
        anotherUser.setId(2L);

        task.setAuthor(anotherUser);

        when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(taskRepository.findByAuthorIdAndId(1L, 1L)).thenReturn(task);

        SecurityContextHolder.setContext(securityContext);

        // Act & Assert
        Exception exception = assertThrows(SecurityException.class, () -> taskService.editTask(1L, 1L, task));
        assertEquals("You do not have permission to edit this task", exception.getMessage());
    }
}
