package com.sostrackapp.Entity;

public class Tiquete {
    private String Aerolinea;
    private String NumeroVuelo;

    public Tiquete(String aerolinea, String numeroVuelo) {
        Aerolinea = aerolinea;
        NumeroVuelo = numeroVuelo;
    }

    public String getAerolinea() {
        return Aerolinea;
    }

    public void setAerolinea(String aerolinea) {
        Aerolinea = aerolinea;
    }

    public String getNumeroVuelo() {
        return NumeroVuelo;
    }

    public void setNumeroVuelo(String numeroVuelo) {
        NumeroVuelo = numeroVuelo;
    }
}
