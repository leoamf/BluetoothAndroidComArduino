package com.example.leonardofreitas.bluetootharduino;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by leonardo.freitas on 26/01/2019.
 */

public class BluetoothConnection extends AsyncTask<Void, Void, Void> {

    private BluetoothController myController;
    private String endereco;
    private boolean sucesso;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb".toUpperCase());
    public BluetoothConnection(BluetoothController controller, String endereco){
        this.myController = controller;
        this.endereco = endereco;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            if (myController.getMySocker() == null) {
                BluetoothDevice device = myController.getMyAdapter().getRemoteDevice(endereco);
                BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(myUUID);
                socket.connect();
                myController.setMySocker(socket);
            }
            this.sucesso = true;
        }
        catch(Exception e)
        {
            sucesso = false;
        }
        return null;
    }

    @Override
    protected void onPreExecute(){
        Toast.makeText(myController.getParent().getApplicationContext(), "Conectando bluetooth", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(Void result){
        if(sucesso) {
            Toast.makeText(myController.getParent().getApplicationContext(), "Conectado com sucesso", Toast.LENGTH_LONG).show();
            this.myController.startListener();
        }
        else
            Toast.makeText(myController.getParent().getApplicationContext(), "Falha na conexão", Toast.LENGTH_LONG).show();

    }
}
