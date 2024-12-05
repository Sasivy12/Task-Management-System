package com.example.effective_mobile.service;

import com.example.effective_mobile.model.Comment;
import com.example.effective_mobile.model.Task;
import com.example.effective_mobile.model.User;
import com.example.effective_mobile.repository.CommentRepository;
import com.example.effective_mobile.repository.TaskRepository;
import com.example.effective_mobile.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
        Optional<Task> task = taskRepository.findById(taskId);
        Optional<User> author = userRepository.findById(authorId);

        if(author.isPresent())
        {
            if(task.isPresent())
            {
                comment.setTask(task);
                comment.setAuthor(author);
                comment.setCreatedAt(LocalDate.now());

                return commentRepository.save(comment);
            }
            else
            {
                throw new EntityNotFoundException("Task not found");
            }
        }
        else
        {
            throw new EntityNotFoundException("User not found");
        }
    }

    public List<Comment> getCommentsByTask(Long taskId)
    {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if(taskOptional.isPresent())
        {
            Task task = taskOptional.get();
            return commentRepository.findByTask(task);
        }
        else
        {
            throw new EntityNotFoundException("Task not found");
        }
    }

    public List<Comment> getCommentsByUser(Long userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return commentRepository.findByAuthor(user);
    }

    public Comment editComment(Long commentId, Long authorId, String updatedMessage)
    {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!comment.getAuthor().getId().equals(authorId))
        {
            throw new SecurityException("You are not allowed to edit this comment");
        }

        comment.setMessage(updatedMessage);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Long authorId)
    {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!comment.getAuthor().getId().equals(authorId))
        {
            throw new SecurityException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }
}
