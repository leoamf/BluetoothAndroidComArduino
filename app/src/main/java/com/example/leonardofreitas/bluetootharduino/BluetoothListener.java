package com.example.leonardofreitas.bluetootharduino;

import android.widget.Toast;

import java.io.InputStream;

/**
 * Created by leonardo.freitas on 26/01/2019.
 */

public class BluetoothListener extends Thread {
    InputStream entrada;
    BluetoothController controller;
    boolean isRunning = false;

    public BluetoothListener(BluetoothController controller){
        this.controller = controller;
        try{
            this.entrada = this.controller.getMySocker().getInputStream();
            this.isRunning = true;
        }
        catch(Exception e){
            Toast.makeText(this.controller.getParent().getApplicationContext(), "Erro ao inicializar listener", Toast.LENGTH_LONG).show();

        }
    }

    public void run(){
        byte[] buffer = new byte[256];
        int bytesRead = 0;
        int ultimaPosicao = 0;
        while(this.isRunning){
            try {
                int bytesAvailable = this.entrada.available();
                if(bytesAvailable > 0){
                    bytesRead = this.entrada.read(buffer, ultimaPosicao, bytesAvailable);
                    ultimaPosicao += bytesRead;

                    String texto = new String(buffer, 0, ultimaPosicao);
                    if(texto.contains("\n")) {
                        ultimaPosicao = 0;
                        this.controller.getParent().runOnUiThread(new acaoCallback(this.controller, texto.substring(0,texto.indexOf('\r'))));
                    }
                }
            }
            catch(Exception e){
                Toast.makeText(this.controller.getParent().getApplicationContext(), "Erro ao ler", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void stopThread(){
        this.isRunning = false;
    }

    class acaoCallback implements Runnable{
        private BluetoothController controller;
        private String received;
        public acaoCallback(BluetoothController controller, String received){
            this.controller = controller;
            this.received = received;
        }
        @Override
        public void run() {
            try{
                int valor = Integer.parseInt(received);
                Toast.makeText(this.controller.getParent().getApplicationContext(), "" + received, Toast.LENGTH_LONG).show();
                if(valor > 800){
                    ((MainActivity)controller.getParent()).flashOn();
                }
                else
                    ((MainActivity)controller.getParent()).flashOff();
            }
            catch(Exception e){

            }

        }
    }
}
