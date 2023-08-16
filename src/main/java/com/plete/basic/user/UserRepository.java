package com.plete.basic.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {//SiteUser의 PK의 타입은 Long

    Optional<SiteUser> findByusername(String username);
}
