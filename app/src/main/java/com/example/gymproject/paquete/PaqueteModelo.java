package com.example.gymproject.paquete;

public class PaqueteModelo {

    private String titulo;
    private String precio;
    private String imagen;

    public PaqueteModelo(String titulo, String precio, String imagen) {
        this.titulo = titulo;
        this.precio = precio;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
