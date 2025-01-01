package com.example.effective_mobile.service;

import com.example.effective_mobile.exception.CommentNotFoundException;
import com.example.effective_mobile.exception.TaskNotFoundException;
import com.example.effective_mobile.exception.UserNotFoundException;
import com.example.effective_mobile.model.Comment;
import com.example.effective_mobile.model.Task;
import com.example.effective_mobile.model.User;
import com.example.effective_mobile.repository.CommentRepository;
import com.example.effective_mobile.repository.TaskRepository;
import com.example.effective_mobile.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService
{

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;


    public Comment createComment(Long taskId, Long authorId, Comment comment)
    {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        Optional<User> author = userRepository.findById(authorId);

        if(author.isPresent())
        {
            if(taskOptional.isPresent())
            {
                Task task = taskOptional.get();
                comment.setTask(task);
                comment.setAuthor(author.get());
                comment.setCreatedAt(LocalDate.now());

                return commentRepository.save(comment);
            }
            else
            {
                throw new TaskNotFoundException("Task not found");
            }
        }
        else
        {
            throw new UserNotFoundException("User not found");
        }
    }

    public List<Comment> getCommentsByTask(Long taskId, Long authorId)
    {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        Optional<User> author = userRepository.findById(authorId);

        if(author.isPresent())
        {
            if(taskOptional.isPresent())
            {
                Task task = taskOptional.get();
                return commentRepository.findByTask(task);
            }
            else
            {
                throw new TaskNotFoundException("Task not found");
            }
        }
        else
        {
            throw new UserNotFoundException("User not found");
        }
    }

    public List<Comment> getCommentsByUser(Long taskId, Long authorId)
    {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return commentRepository.findByTaskAndAuthor(task, author);

    }

    public Comment editComment(Long commentId, Long authorId, Long taskId, Comment updatedComment)
    {
        String currentUserEmail = getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(currentUserEmail);

        if(currentUser == null)
        {
            throw new UserNotFoundException("User not found");
        }

        Task task = taskRepository.findByAuthorIdAndId(authorId, taskId);
        if (task == null)
        {
            throw new TaskNotFoundException("Task not found");
        }

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if(commentOptional.isEmpty())
        {
            throw new CommentNotFoundException("Comment not found");
        }

        Comment comment = commentOptional.get();
        if(currentUser.getRole().equals("ADMIN") || task.getAuthor().getId().equals(currentUser.getId()))
        {
            comment.setMessage(updatedComment.getMessage());
            return commentRepository.save(comment);
        }
        else
        {
            throw new SecurityException("You do not have permission to edit this comment");
        }
    }

    public void deleteComment(Long commentId, Long authorId, Long taskId)
    {
        String currentUserEmail = getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(currentUserEmail);

        if(currentUser == null)
        {
            throw new UserNotFoundException("User not found");
        }

        Task task = taskRepository.findByAuthorIdAndId(authorId, taskId);
        if (task == null)
        {
            throw new TaskNotFoundException("Task not found");
        }

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if(commentOptional.isEmpty())
        {
            throw new CommentNotFoundException("Comment not found");
        }

        Comment comment = commentOptional.get();
        if(currentUser.getRole().equals("ADMIN") || task.getAuthor().getId().equals(currentUser.getId()))
        {
            commentRepository.delete(comment);
        }
        else
        {
            throw new SecurityException("You do not have permission to edit this comment");
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

}