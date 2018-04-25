package com.example.user.crabbot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class DispositivosBT extends AppCompatActivity {

    ListView Lista;
    private static String TAG = "DispositivosBT";

    //string que se enviara a la actividad principal, mainactivity
    public static String EXTRA_DEVICE_ADRESS = "device_address";

    //declaracion de campos
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos_bt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //-------------------------
        VerificarEstadoBT();

        //Inicializa la array que contendra la lista de los dispositivos bluetooth vincula
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.nombre_dispositivos);
        //Presenta los dispositivos vinculados en el ListView
        Lista = (ListView) findViewById(R.id.IdLista);
        Lista.setAdapter(mPairedDevicesArrayAdapter);
        Lista.setOnItemClickListener(mDeviceClickListener);
        //Obtiene el adaptador local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        //Obtiene un conjunto de dispositivos acctualmente emparejados y agrega a 'pairedDevices'
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        //Adiciona un dispositivo previo emparejado al array
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    //configura un (on-click) para la lista
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {
            //Obtener la direccion MAC del dispositivo, que son los ultimos 17 caracteres en la
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            //Realiza un intent para iniciar la siguiente actividad
            //mientras toma un EXTRA_DEVICE_ADDRESS que es la dirección MAC
            Intent i = new Intent(DispositivosBT.this, UserInterfaz.class);
            i.putExtra(EXTRA_DEVICE_ADRESS, address);
            startActivity(i);
        }
    };

    private void VerificarEstadoBT() {
        // Comprueba que el dispositivo tiene Bluetooth
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter==null){
            Toast.makeText(getBaseContext(), "El dispositivo no soporta Bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if(mBtAdapter.isEnabled()){
                Log.d(TAG, "...Bluetooth Activado...");
            }else{
                //solicita al usuario que active Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,1);
            }
        }
    }
}
