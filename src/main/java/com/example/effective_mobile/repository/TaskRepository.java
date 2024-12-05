package com.example.effective_mobile.repository;

import com.example.effective_mobile.model.Task;
import com.example.effective_mobile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>
{
    Task findByAuthorAndId(User user, Long taskId);
}
