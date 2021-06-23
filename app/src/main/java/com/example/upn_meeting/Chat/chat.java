package com.example.upn_meeting.Chat;

public class chat {
    private  String mensaje;
    private Boolean act_usuario;
    private Boolean visto;

    public chat(String mensaje, Boolean act_usuario, Boolean visto) {
        this.mensaje = mensaje;
        this.act_usuario = act_usuario;
        this.visto = visto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getAct_usuario() {
        return act_usuario;
    }

    public void setAct_usuario(Boolean act_usuario) {
        this.act_usuario = act_usuario;
    }

    public Boolean getVisto() {
        return visto;
    }

    public void setVisto(Boolean visto) {
        this.visto = visto;
    }
}
