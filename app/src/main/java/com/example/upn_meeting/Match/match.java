package com.example.upn_meeting.Match;

import com.example.upn_meeting.Usuario.usuario;

import java.util.ArrayList;

public class match {

    private String id_usuario;
    private String nombre;
    private String imagen_perfil;
    private String recibo;
    private String entrega;
    private String favoritos, ultimoMensaje, ultimoEstado_conectado, ultimoVisto, childId;

    private ArrayList<usuario> usuario_lista = new ArrayList<usuario>();

    public match(String id_usuario, String nombre, String imagen_perfil, String recibo, String entrega, String favoritos, String ultimoMensaje, String ultimoEstado_conectado, String ultimoVisto, String childId) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.imagen_perfil = imagen_perfil;
        this.recibo = recibo;
        this.entrega = entrega;
        this.favoritos = favoritos;
        this.ultimoMensaje = ultimoMensaje;
        this.ultimoEstado_conectado = ultimoEstado_conectado;
        this.ultimoVisto = ultimoVisto;
        this.childId = childId;
    }

    public ArrayList<usuario> getUsuarioArrayList(){
        return  usuario_lista;
    }

    public void agregarUsuario_lista(usuario mUsuario){
        usuario_lista.add(mUsuario);
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

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }

    public String getUltimoEstado_conectado() {
        return ultimoEstado_conectado;
    }

    public void setUltimoEstado_conectado(String ultimoEstado_conectado) {
        this.ultimoEstado_conectado = ultimoEstado_conectado;
    }

    public String getUltimoVisto() {
        return ultimoVisto;
    }

    public void setUltimoVisto(String ultimoVisto) {
        this.ultimoVisto = ultimoVisto;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }
}
