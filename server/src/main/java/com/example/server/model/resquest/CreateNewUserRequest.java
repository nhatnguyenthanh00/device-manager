package com.example.server.model.resquest;

import lombok.Data;

@Data
public class CreateNewUserRequest {
    String name ;
    String username ;
    String password ;
    String gender ;
}
