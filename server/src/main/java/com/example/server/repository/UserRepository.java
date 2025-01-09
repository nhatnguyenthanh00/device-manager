package com.example.server.repository;

import com.example.server.model.BkavUser;
import com.example.server.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<BkavUser, UUID> {
    BkavUser findByUsername(String username);


    List<BkavUser> findBkavUserByRole(Role role);

}
