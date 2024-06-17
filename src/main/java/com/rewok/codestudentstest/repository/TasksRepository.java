package com.rewok.codestudentstest.repository;

import com.rewok.codestudentstest.models.MyUser;
import com.rewok.codestudentstest.models.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TasksRepository extends JpaRepository<Tasks, Long> {
    Optional<MyUser> findByName(String username);
}
