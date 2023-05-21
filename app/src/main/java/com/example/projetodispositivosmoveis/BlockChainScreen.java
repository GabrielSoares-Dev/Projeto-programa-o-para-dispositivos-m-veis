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

public class BlockChainScreen extends AppCompatActivity {

    EditText paisInput;
    Button btnBuscar;
    Button btnVoltar;
    TextView blockChainResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blockchain_screen);

        paisInput = findViewById(R.id.paisInput);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnVoltar = findViewById(R.id.btnVoltar);
        blockChainResultado = findViewById(R.id.resultado);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBuscar.setText("PROCESSANDO...");
                String pais = paisInput.getText().toString();
                MyTask task = new MyTask(pais);
                String urlBlockChain = "https://blockchain.info/ticker";
                task.execute(urlBlockChain);
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

        public String pais;

        public MyTask(String pais){
            this.pais  = pais;
        }
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

            String info = null;

            try {

                JSONObject jsonObject = new JSONObject(resultado);
                JSONObject pais = jsonObject.getJSONObject(this.pais.toUpperCase());
                info = pais.getString("buy");
                blockChainResultado.setText("Valor da moeda: " + info );
                SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);

                bancoDados.execSQL("CREATE TABLE IF NOT EXISTS consultas" +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, tipo VARCHAR, dt datetime default current_timestamp,response json)");
                bancoDados.execSQL("INSERT INTO consultas(tipo, response) " +
                        "VALUES('" + "blockchain', '" + jsonObject + "')");
                btnBuscar.setText("BUSCAR DADOS");
                Log.i("blockchain-log-save","Consulta salva com sucesso");
            } catch (JSONException e) {
                e.printStackTrace();
                btnBuscar.setText("BUSCAR DADOS");
                Toast.makeText(
                        BlockChainScreen.this, "Consulte a documentação da api https://www.blockchain.com/ e coloque uma sigla de api válido",
                        Toast.LENGTH_SHORT
                ).show();
            }





        }
    }
}