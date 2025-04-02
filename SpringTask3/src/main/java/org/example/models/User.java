package org.example.models;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
    private String id;
    private String login;
    private String password;
    private String role; // admin albo user

}