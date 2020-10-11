package com.example.approdri;

import java.io.Serializable;

public class DatosOpciones implements Serializable {

    private String descripcion;
    private int imagenOpcion;


    public DatosOpciones() {
    }

    public DatosOpciones(String descripcion, int imagenOpcion) {
        this.descripcion = descripcion;
        this.imagenOpcion = imagenOpcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getImagenOpcion() {
        return imagenOpcion;
    }

    public void setImagenOpcion(int imagenOpcion) {
        this.imagenOpcion = imagenOpcion;
    }
}
