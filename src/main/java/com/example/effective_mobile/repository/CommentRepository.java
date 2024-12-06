package com.example.effective_mobile.repository;

import com.example.effective_mobile.model.Comment;
import com.example.effective_mobile.model.Task;
import com.example.effective_mobile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>
{

    List<Comment> findByTask(Task task);

    List<Comment> findByAuthor(Optional<User> user);
}
