package com.sostrackapp.Core.Interfaces;

import com.sostrackapp.Common.Respuesta;

public interface IAuth {

    Respuesta SendLogin(String usuario, String contrasena);
}
