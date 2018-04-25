package com.example.user.crabbot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class UserInterfaz extends AppCompatActivity {

    ImageButton Up, Down, Left, Right, Light, Automatic, Crab;
    TextView BufferIn;
    //-----------------------------------------------------------
    Handler bluetoothIn;
    int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MyConexionBT;
    //Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //String para la direccion MAC
    private static String address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interfaz);

        Up = (ImageButton) findViewById(R.id.IdUp);
        Down = (ImageButton) findViewById(R.id.IdDown);
        Left = (ImageButton) findViewById(R.id.IdLeft);
        Right = (ImageButton) findViewById(R.id.IdRight);
        Light = (ImageButton) findViewById(R.id.IdLight);
        Automatic = (ImageButton) findViewById(R.id.IdAutomatic);
        Crab = (ImageButton) findViewById(R.id.IdCrab);
        BufferIn = (TextView) findViewById(R.id.IdBufferIn);

        bluetoothIn = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == handlerState){
                    String readMessage = (String) msg.obj;
                    DataStringIN.append(readMessage);

                    int endOfLineIndex = DataStringIN.indexOf("#");

                    if (endOfLineIndex > 0){
                        String dataInPrint = DataStringIN.substring(0, endOfLineIndex);
                        BufferIn.setText("Dato: "+dataInPrint);
                        DataStringIN.delete(0, DataStringIN.length());
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); //get Bluetooth adapter
        VerificarEstadoBT();

        //Configuracion onClick listeners para los botones
        //para indicar que se realizara cuando se detecte
        //el evento de Click

        Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("1");
            }
        });

        Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("2");
            }
        });

        Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("3");
            }
        });

        Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("4");
            }
        });

        Light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("0");
            }
        });

        Automatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("5");
            }
        });

        Crab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConexionBT.write("6");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            btSocket.close();
        } catch (IOException e) {

        }

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException{
        //crea una conexion de salida segura para el dispositivo
        //usando el servicio UUID
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //consigue la direccion MAC desde DeviceListActivity via intent
        Intent intent = getIntent();
        //Consigue la direccion MAC desde DeviceListActivity vvia EXTRA
        address = intent.getStringExtra(DispositivosBT.EXTRA_DEVICE_ADRESS);
        //Setea la direccion MAC
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try{
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del socket fallo", Toast.LENGTH_LONG).show();
            try {
                btSocket.close();
            } catch (IOException e2) {  }
        }


        MyConexionBT = new ConnectedThread(btSocket);
        MyConexionBT.start();

    }

    //comprueba que el dispositivo Bluetooth esta disponible y solicita que se active si esta desactivado
    private void VerificarEstadoBT(){
        if (btAdapter==null){
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        }else{
            if (btAdapter.isEnabled()) {
            }else{
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,1);
            }
        }
    }

    //crea un thread que permite tener la conexion
    private class ConnectedThread extends Thread{

        private InputStream mmInStream;
        private OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket){
            InputStream tmpIn = null;
            OutputStream tmpOut = null;


            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {}
               mmInStream = tmpIn;
               mmOutStream = tmpOut;

        }

       public void run(){
            byte[] buffer = new byte[256];
            int bytes;

            //se mantiene en modo escucha para determinar el ingreso de datos

           while(true){
               try {
                   bytes = mmInStream.read(buffer);
                   String readMessage = new String(buffer, 0, bytes);
                   //Envia los datos obtenidos hacia el evento via handler
                   bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage);
               } catch (IOException e) {
                  break;
               }
           }
       }

       //Envio de trama
        public void write(String input){
            try {
                mmOutStream.write(input.getBytes());
            } catch (IOException e) {
                //si no es posible enviar datos se cierra la conexion
                Toast.makeText(getBaseContext(), "La conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }

    }

}
