package com.sostrackapp.Ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sostrackapp.Core.Comandos;
import com.sostrackapp.Core.Helpers.FTPClientHelper;
import com.sostrackapp.Core.Helpers.ProgressDialog_;
import com.sostrackapp.Entity.Tiquete;
import com.sostrackapp.Entity.ViajeDat;
import com.sostrackapp.R;

import java.util.List;

import static com.sostrackapp.Common.Constante.CAMERA;
import static com.sostrackapp.Common.Constante.CODIGOBARRA;
import static com.sostrackapp.Common.Constante.URL_API;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences;
    NavigationView navigationView;
    private ImageView imgImagen,iconoterminal,iconohora,iconovuelo;
    private TextView nombre, txtResultado,estadoterminal,estadohora,estadovuelo;
    public FTPClientHelper ftpclient = null;
    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
    Tiquete tiquete[];
    private Handler handler = new Handler();
    DrawerLayout drawer;
    RelativeLayout layoutButton,layoutAlerta;
    private List<ViajeDat> viajeDats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutButton = (RelativeLayout)findViewById(R.id.layoutButton);
        layoutAlerta = (RelativeLayout)findViewById(R.id.layoutAlerta);

        txtResultado = (TextView)findViewById(R.id.txtResultado);
        estadoterminal = (TextView)findViewById(R.id.estadoterminal);
        estadohora = (TextView)findViewById(R.id.estadohora);
        estadovuelo = (TextView)findViewById(R.id.estadovuelo);
        iconoterminal = (ImageView) findViewById(R.id.iconoterminal);
        iconohora = (ImageView) findViewById(R.id.iconohora);
        iconovuelo = (ImageView) findViewById(R.id.iconovuelo);

        ftpclient = new FTPClientHelper();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //viajeDats = new ArrayList<ViajeDat>();

        final String URL = URL_API + "static/img/logo_sostrack.png";
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_home2, null);
        imgImagen = (ImageView) nav_header.findViewById(R.id.Logo_corporativo);
        nombre = (TextView) nav_header.findViewById(R.id.textView4);
        navigationView.addHeaderView(nav_header);

        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("sosapp", Context.MODE_PRIVATE);
        setTitle("Escanear");
        nombre.setText(preferences.getString("nombre_usuario", ""));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.inflateMenu(R.menu.activity_home2_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menuNav=navigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.escanear);
        nav_item2.setEnabled(false);
        //leerArchivoDat();
    }

    public void cambioPantalla(){
        if (layoutAlerta.getVisibility() == View.GONE)
        {
            animar(true);
            layoutButton.setVisibility(View.GONE);
        }
    }

    private void animar(boolean mostrar)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar)
        {
            //desde la parte inferior a la superior
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            layoutAlerta.setVisibility(View.VISIBLE);
        }
        else
        {    //desde la parte superior a la inferior
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
            layoutAlerta.setVisibility(View.GONE);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        layoutAlerta.setLayoutAnimation(controller);
        layoutAlerta.startAnimation(animation);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            layoutButton.setVisibility(View.VISIBLE);
        }
    };

    private void ComprobarPermisosCamara() {
        int permissionCheck = ContextCompat.checkSelfPermission(Main.this,
                Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.permiso_necesario))
                            .setMessage(getString(R.string.conceder_permiso))
                            .setPositiveButton(getString(R.string.conceder), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA);
                                    }
                                }
                            })
                            .setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.mensaje_permiso), Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA);
                }
            }else{
                Intent intent = new Intent(Main.this, SimpleScannerActivity.class);
                startActivityForResult(intent, CODIGOBARRA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA){
            if(grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(Main.this, SimpleScannerActivity.class);
                startActivityForResult(intent, CODIGOBARRA);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(layoutAlerta.getVisibility()==View.VISIBLE){
                animar(false);
                handler.postDelayed(runnable, 500);
            }else{
                moveTaskToBack(true);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.home2, menu);
        return true;
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.escanear).setEnabled(false);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("pk_user", "");
            editor.commit();
            Intent i = new Intent(Main.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

            return true;
        }
        if (id == R.id.escanear) {
            ComprobarPermisosCamara();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Fragment fragmentoGenerico = null;
        // FragmentManager fragmentManager = getSupportFragmentManager();
        Log.i("MENU", "SE presiono el menu");

        int id = menuItem.getItemId();


        if (id == R.id.cerrar) {
            showDialog();
        }

        if (id == R.id.escanear) {
            ComprobarPermisosCamara();

        }

        if(id==R.id.politica_de_uso){
            startActivity(new Intent(Main.this, PoliticaUso.class));
        }

        if(id == R.id.soporte){
            startActivity(new Intent(Main.this, Soporte.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public void showDialog() {
        android.app.AlertDialog.Builder dialogo1 = new android.app.AlertDialog.Builder(Main.this);
        dialogo1.setTitle("Aviso");
        dialogo1.setMessage("¿Estás seguro de cerrar sesión?");
        dialogo1.setCancelable(false);

        dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("pk_user", "");
                editor.commit();
                Intent i = new Intent(Main.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean res = false;
        if (resultCode == RESULT_CANCELED) {
            // Si es así mostramos mensaje de cancelado por pantalla.
            Toast.makeText(this, "Resultado cancelado", Toast.LENGTH_SHORT)
                    .show();
        } else {
            final ProgressDialog_ loadingWidget = new ProgressDialog_(this, "Verificando....");
            loadingWidget.showProgress();
            String contenido_result = data.getExtras().getString("contenido");
            String formato_result = data.getExtras().getString("formato");
            cambioPantalla();
            //tiquete = Comandos.getInstance().ObtenerInformacionTiquete(contenido_result);
            viajeDats = Comandos.getInstance().ObtenerViajes();
            res = Comandos.getInstance().EsValido(viajeDats,contenido_result);
            if(res){
                loadingWidget.dismissProgress();
                ViajeDat viajeDat = Comandos.getInstance().ObtenerViaje(viajeDats,contenido_result);
                if( (viajeDat.getLlegada_salida() != null || viajeDat.getTipo_vuelo() != null) || (viajeDat.getHora_programada() != null || viajeDat.getFecha_programada() != null)){
                    String rol = preferences.getString("rol_user"," ");
                    res = Comandos.getInstance().CorrespondePuerta(viajeDat, rol);
                    if(res){
                        res = Comandos.getInstance().ValidarFechaSalida(viajeDat);
                        if(res){
                            //Toast.makeText(getApplicationContext(), "Puede pasar", Toast.LENGTH_SHORT).show();
                            txtResultado.setText("Bienvenido(a) Que tenga buen viaje");
                            //estadohora.setText("Correcto");
                            //estadoterminal.setText("Correcto");
                            //estadovuelo.setText("Correcto");
                            txtResultado.setTextColor(Color.GREEN);
                            estadovuelo.setTextColor(Color.GREEN);
                            estadoterminal.setTextColor(Color.GREEN);
                            estadohora.setTextColor(Color.GREEN);
                            iconohora.setBackgroundResource(R.drawable.ic_correcto);
                            iconoterminal.setBackgroundResource(R.drawable.ic_correcto);
                            iconovuelo.setBackgroundResource(R.drawable.ic_correcto);
                        }else{
                            //Toast.makeText(getApplicationContext(), "Hora de abordaje no valida", Toast.LENGTH_SHORT).show();
                            txtResultado.setText("Su vuelo ha caducado. Por favor diríjase a su aerolínea");
                            //estadohora.setText("Incorrecto");
                            //estadoterminal.setText("Correcto");
                            //estadovuelo.setText("Correcto");
                            txtResultado.setTextColor(Color.RED);
                            estadovuelo.setTextColor(Color.GREEN);
                            estadoterminal.setTextColor(Color.GREEN);
                            estadohora.setTextColor(Color.RED);
                            iconohora.setBackgroundResource(R.drawable.ic_incorrecto);
                            iconoterminal.setBackgroundResource(R.drawable.ic_correcto);
                            iconovuelo.setBackgroundResource(R.drawable.ic_correcto);
                        }
                    }else{
                        String terminal = ((preferences.getString("rol_user","")).equals("AERNACIONAL") ? "INTERNACIONAL" : "NACIONAL");
                        //Toast.makeText(getApplicationContext(), "No se encuentra en la puerta de abordaje correspondiente", Toast.LENGTH_SHORT).show();
                        txtResultado.setText("Terminal incorrecta. diríjase a la terminal "+terminal);
                        //estadohora.setText("Correcto");
                        //estadoterminal.setText("Incorrecto");
                        //estadovuelo.setText("Correcto");
                        txtResultado.setTextColor(Color.RED);
                        estadovuelo.setTextColor(Color.GREEN);
                        estadoterminal.setTextColor(Color.RED);
                        iconoterminal.setBackgroundResource(R.drawable.ic_incorrecto);
                        iconovuelo.setBackgroundResource(R.drawable.ic_correcto);
                        res = Comandos.getInstance().ValidarFechaSalida(viajeDat);
                        if(res){
                            iconohora.setBackgroundResource(R.drawable.ic_correcto);
                            estadohora.setTextColor(Color.GREEN);
                            //estadohora.setText("Correcto");
                        }else{
                            iconohora.setBackgroundResource(R.drawable.ic_incorrecto);
                            estadohora.setTextColor(Color.RED);
                            //estadohora.setText("Incorrecto");
                        }

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Ocurrio un error obteniendo vuelo", Toast.LENGTH_SHORT);
                }

            }else{
                loadingWidget.dismissProgress();
                //Toast.makeText(getApplicationContext(), "No se pudo encontrar tiquete", Toast.LENGTH_SHORT).show();
                txtResultado.setText("Su vuelo no está registrado. Por favor diríjase a su aerolínea");
                //estadohora.setText("Correcto");
                //estadoterminal.setText("Correcto");
                //estadovuelo.setText("Incorrecto");
                txtResultado.setTextColor(Color.RED);
                estadovuelo.setTextColor(Color.RED);
                estadoterminal.setTextColor(Color.RED);
                estadohora.setTextColor(Color.RED);
                iconohora.setBackgroundResource(R.drawable.ic_incorrecto);
                iconoterminal.setBackgroundResource(R.drawable.ic_incorrecto);
                iconovuelo.setBackgroundResource(R.drawable.ic_incorrecto);
            }
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    public void LeerCodigo(View view) {
        ComprobarPermisosCamara();
    }
}
