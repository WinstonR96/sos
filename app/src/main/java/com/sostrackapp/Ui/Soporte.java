package com.sostrackapp.Ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sostrackapp.R;

import static com.sostrackapp.Common.Constante.CAMERA;
import static com.sostrackapp.Common.Constante.CODIGOBARRA;
import static com.sostrackapp.Common.Constante.URL_API;

public class Soporte extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    SharedPreferences preferences;
    NavigationView navigationView;
    DrawerLayout drawer;
    private ImageView imgImagen;
    private TextView nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soporte);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final String URL = URL_API + "static/img/logo_sostrack.png";
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_home2, null);
        imgImagen = (ImageView) nav_header.findViewById(R.id.Logo_corporativo);
        nombre = (TextView) nav_header.findViewById(R.id.textView4);
        navigationView.addHeaderView(nav_header);

        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("sosapp", Context.MODE_PRIVATE);
        setTitle("Soporte");
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
        MenuItem nav_item2 = menuNav.findItem(R.id.soporte);
        nav_item2.setEnabled(false);
    }

    public void showDialog() {
        android.app.AlertDialog.Builder dialogo1 = new android.app.AlertDialog.Builder(Soporte.this);
        dialogo1.setTitle("Aviso");
        dialogo1.setMessage("¿Estás seguro de cerrar sesión?");
        dialogo1.setCancelable(false);

        dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("pk_user", "");
                editor.commit();
                Intent i = new Intent(Soporte.this, LoginActivity.class);
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


    private void ComprobarPermisosCamara() {
        int permissionCheck = ContextCompat.checkSelfPermission(Soporte.this,
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
                Intent intent = new Intent(Soporte.this, SimpleScannerActivity.class);
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
                Intent intent = new Intent(Soporte.this, SimpleScannerActivity.class);
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
            moveTaskToBack(true);
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Fragment fragmentoGenerico = null;
        // FragmentManager fragmentManager = getSupportFragmentManager();
        int id = menuItem.getItemId();

        if (id == R.id.cerrar) {
            showDialog();
        }

        if (id == R.id.escanear) {
            startActivity(new Intent(Soporte.this, Main.class));
        }

        if(id==R.id.politica_de_uso){
            startActivity(new Intent(Soporte.this, PoliticaUso.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Soporte(View view){
        String numero = "3168347052";
        String dial = "tel:"+numero;
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
    }
}
