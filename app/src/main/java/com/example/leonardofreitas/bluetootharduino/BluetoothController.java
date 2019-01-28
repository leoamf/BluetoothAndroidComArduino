package com.example.leonardofreitas.bluetootharduino;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by leonardo.freitas on 25/01/2019.
 */

public class BluetoothController {
    private Activity parent;
    private BluetoothAdapter myAdapter;
    private BluetoothSocket mySocker;
    private BluetoothListener myListener;
    public BluetoothController(Activity parent)
    {
        this.parent = parent;
        myAdapter = BluetoothAdapter.getDefaultAdapter();
        if(myAdapter == null)
            Toast.makeText(parent.getApplicationContext(), "Sem bluetooth", Toast.LENGTH_LONG).show();
        else if(!myAdapter.isEnabled()){
            Intent habilitar = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.parent.startActivityForResult(habilitar, 1);
        }
    }
    public Activity getParent() {
        return parent;
    }


    public void setParent(Activity parent) {
        this.parent = parent;
    }

    public BluetoothAdapter getMyAdapter() {
        return myAdapter;
    }

    public void setMyAdapter(BluetoothAdapter myAdapter) {
        this.myAdapter = myAdapter;
    }

    public void startListener(){
        this.myListener = new BluetoothListener(this);
        this.myListener.start();
    }
    public ArrayList<String> getPairedDevices(){
        ArrayList<String> retorno = new ArrayList<String>();
        Set pairedDevices = myAdapter.getBondedDevices();
        for(Object obj : pairedDevices){
            BluetoothDevice device = (BluetoothDevice)obj;
            String info = device.getName() + "_" + device.getAddress();
            retorno.add(info);
            Log.d("BLE", info);
        }
        return retorno;
    }

    public void send(String s){
        try
        {
            mySocker.getOutputStream().write(s.getBytes());
            Toast.makeText(parent.getApplicationContext(), "Dados enviados", Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            Toast.makeText(parent.getApplicationContext(), "Erro ao enviar dados", Toast.LENGTH_LONG).show();
        }
    }
    /*public String receive(){
        try
        {
            String s = "";
            mySocker.getInputStream().read(s);
        }
        catch(Exception e){
            Toast.makeText(parent.getApplicationContext(), "Erro ao enviar dados", Toast.LENGTH_LONG);
        }
    }*/
    public BluetoothSocket getMySocker() {
        return mySocker;
    }

    public void setMySocker(BluetoothSocket mySocker) {
        this.mySocker = mySocker;
    }
    public void connect(String endereco){
        BluetoothConnection conexao = new BluetoothConnection( this, endereco);
        conexao.execute();
    }
}
