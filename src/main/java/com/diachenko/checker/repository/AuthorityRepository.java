package com.diachenko.checker.repository;


/*  health-checker
    26.02.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByAuthority(String authority);
}
