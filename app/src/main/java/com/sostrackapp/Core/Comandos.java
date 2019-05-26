package com.sostrackapp.Core;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.sostrackapp.Entity.Tiquete;
import com.sostrackapp.Entity.ViajeDat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.sostrackapp.Common.Constante.LOCAL_FILE;

public class Comandos {

    private static Comandos instancia;

    private Comandos(){}

    public static synchronized Comandos getInstance() {
        if (instancia == null) {
            instancia = new Comandos();
        }
        return instancia;
    }


    public Boolean EsValido(List<ViajeDat> viajesData, String infoTiquete){
        boolean res = false;
        for (int i = 0; i < viajesData.size() ; i++) {
            //String vuelo = viajesData.get(i).getCompania_handling().substring(0,2);
            String vuelo = "";
            String compania = viajesData.get(i).getCompania_handling().trim();
            if(compania.equals("JBU")){
                vuelo = "B6";
            }

            else if(compania.equals("DAL")){
                vuelo = "DL";
            }

            else if(compania.equals("TSC")){
                vuelo = "TS";
            }

            else if(compania.equals("ACA")){
                vuelo = "AC";
            }

            else if(compania.equals("NKS")){
                vuelo = "NK";
            }

            else if(compania.equals("AAL")){
                vuelo = "AA";
            }

            else if(compania.equals("KLM")){
                vuelo = "KL";
            }

            else if(compania.equals("LAN")){
                vuelo = "LA";
            }

            else if(compania.equals("CMP")){
                vuelo = "CM";
            }

            else if(compania.equals("AVA")){
                vuelo = "AV";
            }

            else if(compania.equals("RPB")){
                vuelo = "P5";
            }

            else if(compania.equals("VVC")){
                vuelo = "VH";
            }

            else if(compania.equals("EFY")){
                vuelo = "VE";
            }

            else if(compania.equals("GCA")){
                vuelo = "9A";
            }
            else{
                vuelo = "";
            }
            Log.i("VUELO", vuelo);
            if(infoTiquete.contains(compania) || infoTiquete.contains(vuelo)){
                String numeroVuelo = viajesData.get(i).getVuelo().substring(3,7);
                if(infoTiquete.contains(numeroVuelo)){
                    res = true;
                    break;
                }
            }
        }
        return res;
    }

    public ViajeDat ObtenerViaje(List<ViajeDat> viajesData, String infoTiquete){
        ViajeDat viajeDat = new ViajeDat();
        for (int i = 0; i < viajesData.size() ; i++) {
            String vuelo = "";
            String compania = viajesData.get(i).getCompania_handling().trim();
            if(compania.equals("JBU")){
                vuelo = "B6";
            }

            else if(compania.equals("DAL")){
                vuelo = "DL";
            }

            else if(compania.equals("TSC")){
                vuelo = "TS";
            }

            else if(compania.equals("ACA")){
                vuelo = "AC";
            }

            else if(compania.equals("NKS")){
                vuelo = "NK";
            }

            else if(compania.equals("AAL")){
                vuelo = "AA";
            }

            else if(compania.equals("KLM")){
                vuelo = "KL";
            }

            else if(compania.equals("LAN")){
                vuelo = "LA";
            }

            else if(compania.equals("CMP")){
                vuelo = "CM";
            }

            else if(compania.equals("AVA")){
                vuelo = "AV";
            }

            else if(compania.equals("RPB")){
                vuelo = "P5";
            }

            else if(compania.equals("VVC")){
                vuelo = "VH";
            }

            else if(compania.equals("EFY")){
                vuelo = "VE";
            }

            else if(compania.equals("GCA")){
                vuelo = "9A";
            }
            else{
                vuelo = "";
            }
            if(infoTiquete.contains(compania) || infoTiquete.contains(vuelo)){
                String numeroVuelo = viajesData.get(i).getVuelo().substring(3,7);
                if(infoTiquete.contains(numeroVuelo)){
                    viajeDat = viajesData.get(i);
                    break;
                }
            }
        }
        return viajeDat;
    }

    public List<ViajeDat> ObtenerViajes() {
        List<ViajeDat> viajeDats = new ArrayList<>();
        String vuelo= "", origen_destino = "", escala = "", avion = "", matricula = "",
                fecha_programada="", hora_programada="", fecha_estimada = "", hora_estimada="",estacionamiento="",
                sala= "", cinta_puerta = "", compania_handling = "", observaciones = "", llegada_salida = "",
                tipo_vuelo="", rango_mostradores="", origen_destino_= "", observaciones_="",fecha_inicio_embarque="",
                hora_inicio_embarque= "", fecha_fin_embarque = "", hora_fin_embarque = "", terminal = "", vuelo_principal = "",
                muelle_sate="", fecha_inicio_msate="", hora_inicio_msate = "";

        BufferedReader br = null;
        String line = "";
        try{
            br = new BufferedReader(new FileReader(LOCAL_FILE));
            while((line = br.readLine()) != null){
                vuelo= line.substring(0,8);
                origen_destino = line.substring(8,12);
                escala = line.substring(12,16);
                avion = line.substring(16,19);
                matricula = line.substring(19,29);
                fecha_programada=line.substring(29,37);
                hora_programada=line.substring(37,42);
                fecha_estimada = line.substring(42,50);
                hora_estimada=line.substring(50,55);
                estacionamiento=line.substring(55,59);
                sala= line.substring(59,63);
                cinta_puerta = line.substring(63,67);
                compania_handling = line.substring(67,71);
                observaciones = line.substring(71,79);
                llegada_salida = line.substring(79,80);
                tipo_vuelo=line.substring(80,81);
                rango_mostradores=line.substring(81,90);
                origen_destino_= line.substring(90,101);
                observaciones_=line.substring(101,104);
                fecha_inicio_embarque=line.substring(104,112);
                hora_inicio_embarque= line.substring(112,117);
                fecha_fin_embarque = line.substring(117,125);
                hora_fin_embarque = line.substring(125,131);
                terminal = line.substring(131,132);
                vuelo_principal = line.substring(132,140);
                muelle_sate=line.substring(140,144);
                fecha_inicio_msate=line.substring(144,152);
                hora_inicio_msate = line.substring(152,157);
                viajeDats.add(new ViajeDat(vuelo, origen_destino, escala, avion, matricula, fecha_programada, hora_programada, fecha_estimada, hora_estimada, estacionamiento, sala, cinta_puerta, compania_handling, observaciones, llegada_salida, tipo_vuelo, rango_mostradores, origen_destino_, observaciones_, fecha_inicio_embarque, hora_inicio_embarque, fecha_fin_embarque, hora_fin_embarque, terminal, vuelo_principal, muelle_sate, fecha_inicio_msate, hora_inicio_msate));
            }

        }
        catch (FileNotFoundException notFoundE) {
            System.out.println(notFoundE.getMessage());
        } catch (IOException ioE) {
            System.out.println(ioE.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException IoE) {
                    System.out.println(IoE.getMessage());
                }
            }
        }
        return viajeDats;
    }

    public Tiquete[] ObtenerInformacionTiquete(String contenido_result){
        Tiquete tiquete[] =new Tiquete[1];
        String cvsSplitBy = ";";
        contenido_result.trim();
        contenido_result = contenido_result.replaceAll("\\s{2,}", " ");
        contenido_result = contenido_result.replace(" ", ";");
        String[] info = contenido_result.split(cvsSplitBy);
        tiquete[0]= new Tiquete(info[1],info[2]);
        return tiquete;
    }

    public boolean CorrespondePuerta(ViajeDat viajeDat, String rol) {
        boolean res = false;
        String pase = viajeDat.getLlegada_salida()+viajeDat.getTipo_vuelo();
        if( (rol.equals("AERINTERNACIONAL") && pase.equals("SI") || rol.equals("AERINTERNACIONAL") && pase.equals("LI" )) || ((rol.equals("AERNACIONAL") && pase.equals("SN") ) || rol.equals("AERNACIONAL") && pase.equals("LN"))){
            res = true;
        }
        return res;
    }

    public boolean ValidarFechaSalida(ViajeDat viajeDat) {
        boolean res = false;
        SimpleDateFormat FechaFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat HoraFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        String fecha_sistema = FechaFormat.format(calendar.getTime());
        String hora_sistema = HoraFormat.format(calendar.getTime());
        String fecha_vuelo = viajeDat.getFecha_programada();
        String hora_vuelo = viajeDat.getHora_programada();
        int resFecha = fecha_sistema.compareTo(fecha_vuelo);
        if(resFecha == 0){
            int resHora = hora_sistema.compareTo(hora_vuelo);
            if(resHora <= 0){
                res = true;
            }
        }
        return res;
    }
}
