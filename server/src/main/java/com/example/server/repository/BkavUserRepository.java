package com.example.server.repository;

import com.example.server.model.entity.BkavUser;
import com.example.server.utils.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BkavUserRepository extends JpaRepository<BkavUser, UUID> {
    BkavUser findByUsername(String username);

    Page<BkavUser> findBkavUserByNameContainsIgnoreCase(String name, Pageable pageable);

//    BkavUser updateBkavUserById
    Page<BkavUser> findBkavUserByGenderAndNameContainingIgnoreCase(Gender gender, String name, Pageable pageable);

}
