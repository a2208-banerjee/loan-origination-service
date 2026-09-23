package com.greenfield.loanorigination.repository;

import com.greenfield.loanorigination.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepoRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
