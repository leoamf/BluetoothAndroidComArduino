package com.example.leonardofreitas.bluetootharduino;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 50;

    Button btSend;
    Button btConnect;
    TextView tbSend;
    static BluetoothController bluetoothController;
    ArrayList<String> pareados;
    SeekBar skPotencia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btConnect = (Button)findViewById(R.id.btConnect);
        btSend = (Button)findViewById(R.id.btSend);
        tbSend = (TextView)findViewById(R.id.tbSend);
        skPotencia = (SeekBar)findViewById(R.id.skPotencia);
        skPotencia.setMin(0);
        skPotencia.setMax(255);



        if(bluetoothController == null)
            bluetoothController = new BluetoothController(this);

        skPotencia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                //skPotencia.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                bluetoothController.send(seekBar.getProgress() + "");
                flashOff();

            }
        });
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //bluetoothController.send(((TextView)view.findViewById(R.id.tbSend)).getText().toString() + '\n');
                //bluetoothController.send(((SeekBar)view.findViewById(R.id.skPotencia)).getProgress() + "");

               // bluetoothController.send("TESTE\n");
            }
        });

        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pareados = bluetoothController.getPairedDevices();
                for(String info : pareados){
                    if(info.contains("LEOBAITOLA")){
                        String endereco = info.substring(info.indexOf('_')+1);
                        bluetoothController.connect(endereco);
                    }
                }
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);

                final boolean hasCameraFlash = getPackageManager().
                        hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                boolean isEnabled = ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED;

                if(isEnabled)
                    flashOn();

            }
        });
    }

    public void flashOn(){
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            //flashLightStatus = true;
            //imageFlashlight.setImageResource(R.drawable.btn_switch_on);
        } catch (CameraAccessException e) {

        }
    }


    public void flashOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            //flashLightStatus = false;
            //imageFlashlight.setImageResource(R.drawable.btn_switch_off);
        } catch (CameraAccessException e) {
        }
    }

}
