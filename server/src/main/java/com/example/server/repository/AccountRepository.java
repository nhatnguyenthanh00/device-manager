package com.example.server.repository;

import com.example.server.model.BkavUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<BkavUser, UUID> {
    BkavUser findByUsername(String username);
}
