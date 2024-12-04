package com.example.effective_mobile.repository;

import com.example.effective_mobile.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>
{

}
