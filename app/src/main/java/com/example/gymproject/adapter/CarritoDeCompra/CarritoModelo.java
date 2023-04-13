package com.example.gymproject.adapter.CarritoDeCompra;

public class CarritoModelo {

    private String idCarritoTemp;
    private String nProducto;
    private double precioProducto;
    private int cantidadProducto;
    private String imagenProducto;


    public CarritoModelo(String idCarritoTemp,String nProducto, double precioProducto, int cantidadProducto, String imagenProducto) {

        this.idCarritoTemp = idCarritoTemp;
        this.nProducto = nProducto;
        this.precioProducto = precioProducto;
        this.cantidadProducto = cantidadProducto;
        this.imagenProducto = imagenProducto;

    }



    public String getIdCarritoTemp() {
        return idCarritoTemp;
    }

    public void setIdCarritoTemp(String idCarritoTemp) {
        this.idCarritoTemp = idCarritoTemp;
    }

    public String getnProducto() {
        return nProducto;
    }

    public void setnProducto(String nProducto) {
        this.nProducto = nProducto;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(int precioProducto) {
        this.precioProducto = precioProducto;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }
    public void agregarCantidad() {
        this.cantidadProducto++;
    }


    public void disminuirCantidad() {
        this.cantidadProducto--;
    }

    public String getImagenProducto() {
        return imagenProducto;
    }

    public double getTotal() {
        return this.cantidadProducto * this.precioProducto;
    }

    public void setImagenProducto(String imagenProducto) {
        this.imagenProducto = imagenProducto;
    }


}
