package com.sostrackapp.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.sostrackapp.Core.Helpers.FTPClientHelper;
import com.sostrackapp.Core.Interfaces.IResultService;

import java.util.Timer;
import java.util.TimerTask;
import static com.sostrackapp.Common.Constante.HOST_FTP;
import static com.sostrackapp.Common.Constante.LOCAL_FILE;
import static com.sostrackapp.Common.Constante.PASS_FTP;
import static com.sostrackapp.Common.Constante.SERVER_FILE;
import static com.sostrackapp.Common.Constante.USER_FTP;

public class DataService extends Service {

    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds

    private Handler mHandler = new Handler();

    IResultService mResultCallback = null;

    // timer handling
    private Timer mTimer = null;


    public DataService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
// cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        mTimer.schedule(new GetDataFromServerTask(), 0, NOTIFY_INTERVAL);

    }

    class GetDataFromServerTask extends TimerTask {
        public FTPClientHelper ftpclient = new FTPClientHelper();
        public final String TAG = "SERVICIO";


        @Override
        public void run() {
            // run on another thread
            //mHandler.post(new Runnable() {
            new Thread(new Runnable() {

                @Override
                public void run() {

                        GetDataJson();


                }

            }).start();
        }

        private void GetDataJson() {
            boolean status = false;
            String host = HOST_FTP;
            String username = USER_FTP;
            String password = PASS_FTP;
            status = ftpclient.ftpConnect(host, username, password, 21);
            if (status == true) {
                Log.d(TAG, "Connection success");
                ftpclient.ftpDescargarArchivo(LOCAL_FILE, SERVER_FILE);
                ftpclient.ftpDisconnect();
            } else {
                Log.d(TAG, "Connection failed");
            }
        }
    }
}
