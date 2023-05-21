package com.example.projetodispositivosmoveis;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {



    Button btnCep;
    Button btnBlockChain;
    Button bntConsultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCep = findViewById(R.id.btnCep);
        btnBlockChain = findViewById(R.id.btnBlockChain);
        bntConsultas = findViewById(R.id.bntConsultas);


        btnCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CepScreen.class);

                startActivity(intent);
            }
        });
        btnBlockChain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BlockChainScreen.class);

                startActivity(intent);
            }
        });
        bntConsultas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CrudScreen.class);

                startActivity(intent);
            }
        });
    }
}