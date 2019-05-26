package com.sostrackapp.Common;

import android.content.res.Resources;
import android.os.Environment;

import com.sostrackapp.R;

public class Constante {
    /*public static final String HOST_FTP = "files.000webhost.com";
    public static final String PASS_FTP = "WinstonR1045742017";
    public static final String USER_FTP = "puppycare";
    public static final String DIRECTORIO_SERVER = "/public_html/";*/
    public static final String HOST_FTP = "190.242.47.20";
    public static final String USER_FTP = "SP-IKUSIFIDSFTP";
    public static final String PASS_FTP = "4gtcptffids$1";
    public static final String DIRECTORIO_SERVER = "/";
    public static final String DIRECTORIO_LOCAL = Environment.getExternalStorageDirectory().getPath() + "/Sostrack/";
    public static final String FILENAME = "COSIP.DAT";
    public static final String LOCAL_FILE = DIRECTORIO_LOCAL + FILENAME;
    public static final String SERVER_FILE = DIRECTORIO_SERVER+FILENAME;
    public static final int EXTERNAL_WRITE_CODE = 101;
    public static final int CAMERA = 102;
    public final static int CODIGOBARRA = 0;
    public final static String LOGIN_URL = "http://sostrack.sosltda.com/ingreso/";
    public final static String URL_API = "http://sostrack.sosltda.com/";
    //public final static String LOGIN_URL = "https://restcountries.eu/rest/v2/all";


}
