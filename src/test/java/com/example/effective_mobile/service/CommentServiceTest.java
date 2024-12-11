package com.example.effective_mobile.service;

import com.example.effective_mobile.model.Comment;
import com.example.effective_mobile.model.Task;
import com.example.effective_mobile.model.User;
import com.example.effective_mobile.repository.CommentRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTest
{
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCreateComment_Success()
    {
        Long taskId = 1L;
        Long authorId = 2L;
        Comment comment = new Comment();
        comment.setMessage("Test comment");

        Task task = new Task();
        task.setId(taskId);

        User author = new User();
        author.setId(authorId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Comment createdComment = commentService.createComment(taskId, authorId, comment);

        assertNotNull(createdComment);
        assertEquals("Test comment", createdComment.getMessage());
        assertEquals(task, createdComment.getTask());
        assertEquals(author, createdComment.getAuthor());
        assertNotNull(createdComment.getCreatedAt());
        verify(commentRepository).save(comment);
    }

    @Test
    void testCreateComment_TaskNotFound()
    {
        Long taskId = 1L;
        Long authorId = 2L;
        Comment comment = new Comment();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        when(userRepository.findById(authorId)).thenReturn(Optional.of(new User()));

        assertThrows(EntityNotFoundException.class, () -> commentService.createComment(taskId, authorId, comment));
    }

    @Test
    void testCreateComment_UserNotFound()
    {
        Long taskId = 1L;
        Long authorId = 2L;
        Comment comment = new Comment();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(new Task()));
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.createComment(taskId, authorId, comment));
    }

    @Test
    void testGetCommentsByTask_Success()
    {
        Long taskId = 1L;
        Long authorId = 2L;
        Task task = new Task();
        User author = new User();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(commentRepository.findByTask(task)).thenReturn(List.of(new Comment()));

        List<Comment> comments = commentService.getCommentsByTask(taskId, authorId);

        assertNotNull(comments);
        assertFalse(comments.isEmpty());
        verify(commentRepository).findByTask(task);
    }

    @Test
    void testEditComment_Success() {
        Long commentId = 1L;
        Long taskId = 2L;
        Long authorId = 3L;

        Comment existingComment = new Comment();
        existingComment.setMessage("Old message");

        Comment updatedComment = new Comment();
        updatedComment.setMessage("New message");

        Task task = new Task();
        task.setId(taskId);
        task.setAuthor(new User());
        task.getAuthor().setId(authorId);

        User currentUser = new User();
        currentUser.setId(authorId);
        currentUser.setEmail("test@example.com");
        currentUser.setRole("USER");

        Authentication authentication = mockAuthentication(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(userRepository.findByEmail(currentUser.getEmail())).thenReturn(currentUser);
        when(taskRepository.findByAuthorIdAndId(authorId, taskId)).thenReturn(task);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Comment result = commentService.editComment(commentId, authorId, taskId, updatedComment);

        assertNotNull(result);
        assertEquals("New message", result.getMessage());
        verify(commentRepository).save(existingComment);
    }

    @Test
    void testDeleteComment_Success() {
        // Arrange
        Long commentId = 1L;
        Long taskId = 2L;
        Long authorId = 3L;

        User currentUser = new User();
        currentUser.setId(authorId);
        currentUser.setEmail("test@example.com");
        currentUser.setRole("ADMIN");

        Task task = new Task();
        task.setId(taskId);
        task.setAuthor(currentUser);

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setTask(task);

        // Mock security context and authentication
        Authentication authentication = mockAuthentication(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Mock repository calls
        when(userRepository.findByEmail(currentUser.getEmail())).thenReturn(currentUser);
        when(taskRepository.findByAuthorIdAndId(authorId, taskId)).thenReturn(task);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        assertDoesNotThrow(() -> commentService.deleteComment(commentId, authorId, taskId));

        // Assert
        verify(commentRepository).delete(comment);
    }

    private Authentication mockAuthentication(User user)
    {
        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(user.getEmail()); // Add this to stub username

        return authentication;
    }

}
