package com.example.approdri;

import java.io.Serializable;

public class DatosArticulos implements Serializable {

    private String id_producto;
    private String nombreProducto, email;

    public DatosArticulos() {
    }

    public DatosArticulos(String id_producto, String nombreProducto, String email) {
        this.id_producto = id_producto;
        this.nombreProducto = nombreProducto;
        this.email = email;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
