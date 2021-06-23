package com.example.upn_meeting.Tarjeta;

public class tarjeta {

    private String id_usuario;
    private String nombre;
    private String imagen_perfil;
    private String recibo;
    private String entrega;
    private String favoritos;


    public tarjeta(String id, String nombre, String imagen, String recibir, String dar, String fav) {
        this.id_usuario = id;
        this.nombre = nombre;
        imagen_perfil = imagen;
        this.recibo = recibir;
        this.entrega = dar;
        favoritos = fav;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen_perfil() {
        return imagen_perfil;
    }

    public void setImagen_perfil(String imagen_perfil) {
        this.imagen_perfil = imagen_perfil;
    }

    public String getRecibo() {
        return recibo;
    }

    public void setRecibo(String recibo) {
        this.recibo = recibo;
    }

    public String getEntrega() {
        return entrega;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }

    public String getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(String favoritos) {
        this.favoritos = favoritos;
    }
}
