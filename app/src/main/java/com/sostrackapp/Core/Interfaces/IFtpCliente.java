package com.sostrackapp.Core.Interfaces;

public interface IFtpCliente {

    boolean ftpConnect(String host, String username, String password,
                              int port);

    public boolean ftpDisconnect();

    public String[] ftpPrintFilesList(String dir_path);

    public void ftpDescargarArchivo(String localFile, String hostFile);


}
