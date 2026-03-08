package com.diachenko.checker.repository;


import com.diachenko.checker.model.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*  health-checker
    19.02.2026
    @author DiachenkoDanylo
*/

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    @Query("""
                select u from AppUser u
                left join fetch u.monitoredUrls
                where u.username = :username
            """)
    Optional<AppUser> findByUsernameWithUrls(String username);

    @Query("""
                select u from AppUser u
                left join fetch u.monitoredUrls
                where u.id = :id
            """)
    Optional<AppUser> findByIdWithUrls(Long id);
}
