package com.example.practica2.dto;

import java.io.Serializable;

public class LoginResponseDTO implements Serializable {

    private static final long serialVersionUID = -669342368L;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
