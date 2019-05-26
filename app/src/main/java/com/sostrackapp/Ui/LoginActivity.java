package com.sostrackapp.Ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sostrackapp.Common.Constante;
import com.sostrackapp.Common.Respuesta;
import com.sostrackapp.Core.Auth;
import com.sostrackapp.Core.Helpers.FTPClientHelper;
import com.sostrackapp.Core.Helpers.GCMClientManager;
import com.sostrackapp.Core.Helpers.ProgressDialog_;
import com.sostrackapp.Core.Helpers.VolleyHelper;
import com.sostrackapp.Core.Interfaces.IResultService;
import com.sostrackapp.R;
import com.sostrackapp.Service.DataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import static com.sostrackapp.Common.Constante.CAMERA;
import static com.sostrackapp.Common.Constante.EXTERNAL_WRITE_CODE;
import static com.sostrackapp.Common.Constante.HOST_FTP;
import static com.sostrackapp.Common.Constante.LOGIN_URL;
import static com.sostrackapp.Common.Constante.PASS_FTP;
import static com.sostrackapp.Common.Constante.USER_FTP;

public class LoginActivity extends AppCompatActivity {

    TextView usuario, contraseña;
    Button enviar;
    RelativeLayout rllogin1, rllogin2;
    private Handler handler = new Handler();
    String ID_GSM;
    SharedPreferences preferences;
    String uuid;
    String PROJECT_NUMBER = "336163349411";
    TelephonyManager telephonyManager;
    String deviceId;
    FTPClientHelper ftpclient;
    boolean status = false;
    String host = HOST_FTP;
    String username = USER_FTP;
    String password = PASS_FTP;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progress = ProgressDialog.show(this, "Comprobando",
                "Estamos comprobando conexiones", true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ftpclient = new FTPClientHelper();


        //Comprobar permisos
        ComprobarPermisos();

        if (compruebaConexion(this)) {
            status = ftpclient.ftpConnectTesting(host, username, password, 21);
            if (status) {
                progress.dismiss();
                ftpclient.ftpDisconnect();
                //Crear Archivo local necesario para descargar archivo desde ftp
                CrearArchivoLocal();
                //Iniciar Servicio
                startService(new Intent(LoginActivity.this, DataService.class));
            } else {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Error conexion a servidor FTP", Toast.LENGTH_LONG).show();
            }
            //Binding widgets
            String deviceId;
            usuario = (TextView) findViewById(R.id.usuario);
            contraseña = (TextView) findViewById(R.id.contraseña);
            enviar = (Button) findViewById(R.id.enviar);
            rllogin1 = (RelativeLayout) findViewById(R.id.rllogin1);
            rllogin2 = (RelativeLayout) findViewById(R.id.rllogin2);
            preferences = getSharedPreferences("sosapp", Context.MODE_PRIVATE);

            uuid = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            if (!preferences.getString("pk_user", "").equals("")) {
                Intent i = new Intent(LoginActivity.this, Main.class);
                startActivity(i);
                finish();
            }

            GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
            pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                @Override
                public void onSuccess(String registrationId, boolean isNewRegistration) {
                    ID_GSM = registrationId;
                }

                @Override
                public void onFailure(String ex) {
                    super.onFailure(ex);
                }
            });


        } else {
            progress.dismiss();
            Toast.makeText(getApplicationContext(), "Compruebe conexion a internet", Toast.LENGTH_SHORT).show();
        }
    }


    public void CrearArchivoLocal() {
        File root = new File(Constante.DIRECTORIO_LOCAL);
        if (!root.exists()) {
            root.mkdirs();
        }
        File file = new File(root, Constante.LOCAL_FILE);
    }


    //region Permisos
    private void ComprobarPermisos() {
        ComprobarPermisosEscrituraAlmacenamiento();
        ComprobarPermisosCamara();
    }

    private void ComprobarPermisosEscrituraAlmacenamiento() {
        int permissionCheck = ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.permiso_necesario))
                            .setMessage(getString(R.string.conceder_permiso))
                            .setPositiveButton(getString(R.string.conceder), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_WRITE_CODE);
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
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_WRITE_CODE);
                }
            }
        }
    }

    private void ComprobarPermisosCamara() {
        int permissionCheck = ContextCompat.checkSelfPermission(LoginActivity.this,
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
            }
        }
    }
    //endregion

    //region Manejo de animacion de login
    public void cambioPantalla(View view) {
        if (rllogin2.getVisibility() == View.GONE) {
            animar(true);
            rllogin1.setVisibility(View.GONE);
        }

    }

    private void animar(boolean mostrar) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar) {
            //desde la parte inferior a la superior
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            rllogin2.setVisibility(View.VISIBLE);
        } else {    //desde la parte superior a la inferior
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
            rllogin2.setVisibility(View.GONE);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        rllogin2.setLayoutAnimation(controller);
        rllogin2.startAnimation(animation);
    }

    @Override
    public void onBackPressed() {
        if (rllogin2.getVisibility() == View.VISIBLE) {
            animar(false);
            handler.postDelayed(runnable, 500);

        } else {
            finish();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rllogin1.setVisibility(View.VISIBLE);
        }
    };
    //endregion

    public void LogIn(View view) {
        if (!usuario.getText().toString().equals("") && !contraseña.getText().toString().equals("")) {
            sendlogin(usuario.getText().toString(), contraseña.getText().toString());
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.usuario_contra_vacio), Toast.LENGTH_LONG).show();
        }
    }

    private void sendlogin(final String usuario, String contraseña) {
        try {
            if (deviceId.equalsIgnoreCase(null)) {
                deviceId = "";
            }
        } catch (Exception e) {
            deviceId = "";
        }
        final String ususario_ = usuario.trim(), contraseña_ = contraseña.trim();
        final ProgressDialog_ loadingWidget = new ProgressDialog_(this, "Autenticando...");
        loadingWidget.showProgress();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("jason", "" + s);
                        loadingWidget.dismissProgress();

                        try {
                            String rol = "", roluser = "";
                            JSONArray json = new JSONArray(s);
                            JSONArray JSONrol = new JSONArray(json.getJSONObject(0).getString("rol"));
                            for (int i = 0; i < JSONrol.length(); i++) {
                                JSONObject object = JSONrol.getJSONObject(i);
                                rol = object.getString("nombre_rol");
                                if (rol.equals("AERNACIONAL")) {
                                    roluser = "AERNACIONAL";
                                }
                                if (rol.equals("AERINTERNACIONAL")) {
                                    roluser = "AERINTERNACIONAL";
                                }
                            }
                            if (returnRol(JSONrol)) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("pk_user", json.getJSONObject(0).getString("pk"));
                                editor.putString("nombre_usuario", json.getJSONObject(0).getString("nombre"));
                                editor.putString("rol_user", roluser);
                                editor.commit();
                                Intent inte = new Intent(LoginActivity.this, Main.class);
                                startActivity(inte);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "El usuario y contraseña incorrectos", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingWidget.dismissProgress();

                            Toast.makeText(LoginActivity.this, "El usuario y contraseña incorrectos", Toast.LENGTH_LONG).show();

                            loadingWidget.dismissProgress();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loadingWidget.dismissProgress();
                        if (compruebaConexion(getApplicationContext())) {
                            Toast.makeText(LoginActivity.this, "Hemos experimentado problemas con el servidor", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Conexión a internet no disponible", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("user", ususario_);
                params.put("password", contraseña_);
                params.put("regid", ID_GSM);
                params.put("devid", deviceId);


                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public static boolean compruebaConexion(Context context) {

        boolean connected = false;

        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        int i = 0;
        while (i < redes.length && !connected) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
            i++;
        }
        return connected;
    }

    public Boolean returnRol(JSONArray JSONrol) {
        for (int i = 0; i < JSONrol.length(); i++) {
            try {
                if (JSONrol.getJSONObject(i).getString("nombre_rol").equals("SUPERVISOR") || JSONrol.getJSONObject(i).getString("nombre_rol").equals("COORDINADOR")) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("rol_usuario", JSONrol.getJSONObject(i).getString("nombre_rol"));
                    return true;
                } else {
                    JSONObject object = JSONrol.getJSONObject(i);
                    String rol = object.getString("nombre_rol");
                    if (rol.equals("AERNACIONAL")) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("rol_usuario", rol);
                        return true;
                    }

                    if (rol.equals("AERINTERNACIONAL")) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("rol_usuario", rol);
                        return true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;

    }

}
