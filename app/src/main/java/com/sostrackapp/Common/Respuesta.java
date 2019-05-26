package com.sostrackapp.Common;

public class Respuesta {

    public Boolean esValido;
    public String mensaje;

    public Respuesta(Boolean esValido, String mensaje) {
        this.esValido = esValido;
        this.mensaje = mensaje;
    }

    public Respuesta(Boolean esValido) {
        this.esValido = esValido;
    }

    public Respuesta() {
    }

    public Boolean getEsValido() {
        return esValido;
    }

    public void setEsValido(Boolean esValido) {
        this.esValido = esValido;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
