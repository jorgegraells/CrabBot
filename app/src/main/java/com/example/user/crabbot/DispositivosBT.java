package com.example.user.crabbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class DispositivosBT extends AppCompatActivity {

    ListView Lista;
    private static  String TAG = "DispositivosBT";
    public static String EXTRA_DEVICE_ADRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos_bt);
    }
}
