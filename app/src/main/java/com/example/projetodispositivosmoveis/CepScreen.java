package com.example.projetodispositivosmoveis;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CepScreen extends AppCompatActivity {

    EditText cepInput;
    Button btnBuscar;
    Button btnVoltar;
    TextView cepResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cep_screen);

        cepInput = findViewById(R.id.paisInput);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnVoltar = findViewById(R.id.btnVoltar);
        cepResultado = findViewById(R.id.resultado);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    btnBuscar.setText("PROCESSANDO...");
                    MyTask task = new MyTask();
                    String cep = cepInput.getText().toString();
                    String urlCep = "https://viacep.com.br/ws/" + cep + "/json/";
                    task.execute(urlCep);

            }
        });
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
            }
        });
    }

    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = new StringBuffer();

            try {

                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                int responseCode = conexao.getResponseCode();

                    inputStream = conexao.getInputStream();

                    inputStreamReader = new InputStreamReader( inputStream );
                    BufferedReader reader = new BufferedReader( inputStreamReader );
                    buffer = new StringBuffer();
                    String linha = "";

                    while((linha = reader.readLine()) != null){
                        buffer.append( linha );
                    }


            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }

            return buffer.toString();


        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            String cep = null;
            String logradouro = null;
            String bairro = null;
            String localidade = null;
            String uf = null;



    try {

        JSONObject jsonObject = new JSONObject(resultado);
        cep = jsonObject.getString("cep");
        uf = jsonObject.getString("uf");
        logradouro = jsonObject.getString("logradouro");
        bairro = jsonObject.getString("bairro");
        localidade = jsonObject.getString("localidade");
        uf = jsonObject.getString("uf");
        SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);

        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS consultas" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, tipo VARCHAR, dt datetime default current_timestamp,response json)");
        bancoDados.execSQL("INSERT INTO consultas(tipo, response) " +
                "VALUES('" + "cep" + "', '" + jsonObject + "')");
        cepResultado.setText(
                "Logradouro: "+logradouro
                        + "\n"
                        + "Bairro: " + bairro
                        + "\n"
                        + "Localidade: " + localidade
                        + "\n"
                        + "Uf: " + uf
                        + "\n"
                        + "Cep: " + cep
        );
        btnBuscar.setText("BUSCAR DADOS");
        Log.i("cep-log-save","Consulta salva com sucesso");

    } catch (JSONException e) {
        e.printStackTrace();
        btnBuscar.setText("BUSCAR DADOS");
        Toast.makeText(CepScreen.this, "Insira um cep válido", Toast.LENGTH_SHORT).show();
}







        }
    }
}