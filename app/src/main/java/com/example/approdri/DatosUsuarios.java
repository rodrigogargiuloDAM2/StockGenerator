package com.example.approdri;

import java.io.Serializable;

public class DatosUsuarios implements Serializable {

    private String email;


    public DatosUsuarios() {
    }

    public DatosUsuarios(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
