package com.example.user.crabbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class UserInterfaz extends AppCompatActivity {

    ImageButton Up, Down, Left, Right, Light, Automatic, Crab;
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

        Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //contenido
            }
        });

        Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //contenido
            }
        });

        Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //contenido
            }
        });

        Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //contenido
            }
        });

        Light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //contenido
            }
        });

        Automatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //contenido
            }
        });

        Crab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //contenido
            }
        });
    }
}
