package ru.ism.module;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private Role role = Role.USER;
    private int balance = 0;
    private int monthLimit = 10000;

    public User(String email, String name, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
