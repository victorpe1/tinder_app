package com.example.upn_meeting.Usuario;

public class usuario {
    private String id, nombre, telef, noti;
    private Boolean aux = false;

    public usuario(String id) {
        this.id = id;
    }

    public usuario(String nombre, String telef, String noti) {
        this.nombre = nombre;
        this.telef = telef;
        this.noti = noti;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelef() {
        return telef;
    }

    public void setTelef(String telef) {
        this.telef = telef;
    }

    public String getNoti() {
        return noti;
    }

    public void setNoti(String noti) {
        this.noti = noti;
    }

    public Boolean getAux() {
        return aux;
    }

    public void setAux(Boolean aux) {
        this.aux = aux;
    }
}
