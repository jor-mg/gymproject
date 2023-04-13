package com.example.gymproject.adapter.MenuPrincipalProductos;

public class MarcaModelo {

    private String idM;
    private String title_m;
    private int image_m;


    public MarcaModelo(String idM, String title_m, int image_m) {
        this.idM = idM;
        this.title_m = title_m;
        this.image_m = image_m;
    }

    public String getIdM() {
        return idM;
    }

    public void setIdM(String idM) {
        this.idM = idM;
    }

    public String getTitle_m() {
        return title_m;
    }

    public void setTitle_m(String title_m) {
        this.title_m = title_m;
    }

    public int getImage_m() {
        return image_m;
    }

    public void setImage_m(int image_m) {
        this.image_m = image_m;
    }
}
