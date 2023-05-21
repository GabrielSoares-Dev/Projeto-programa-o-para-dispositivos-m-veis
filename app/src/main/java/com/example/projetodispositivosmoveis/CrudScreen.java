package com.example.projetodispositivosmoveis;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CrudScreen extends AppCompatActivity {

    Button btnPesquisar;
    Button btnDeletar;
    Button btnAtualizar;

    Button btnVoltar;
    EditText idConsultaDeletar;
    EditText idConsultaAtualizar;
    CheckBox  checkBlockChain;
    CheckBox  checkCep;

    RadioButton radioCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crud_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnPesquisar = findViewById(R.id.btnPesquisar);
        btnDeletar = findViewById(R.id.btnDeletar);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnVoltar  = findViewById(R.id.btnVoltar);
        idConsultaDeletar = findViewById(R.id.idConsultaDeletar);
        idConsultaAtualizar = findViewById(R.id.idConsultaAtualizar);
        checkBlockChain = findViewById(R.id.checkBlockChain);
        checkCep = findViewById(R.id.checkCep);
        radioCep = findViewById(R.id.radioCep);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    btnPesquisar.setText("PROCESSANDO...");
                    SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);
                    String consulta = "SELECT * FROM consultas " +  (checkCep.isChecked() ? "WHERE tipo='cep' " : "") + (checkBlockChain.isChecked() && checkCep.isChecked() ? "OR " : "") + (checkBlockChain.isChecked() ?(checkBlockChain.isChecked() && checkCep.isChecked() ? "" : "WHERE") + " tipo='blockchain'" : "")  ;
                    Cursor cursor = bancoDados.rawQuery(consulta, null);

                    int indiceId = cursor.getColumnIndex("id");
                    int indiceTipo = cursor.getColumnIndex("tipo");
                    int indiceDt = cursor.getColumnIndex("dt");
                    int indiceResponse = cursor.getColumnIndex("response");
                   cursor.moveToFirst();
                    while (cursor != null   && !cursor.isAfterLast()) {

                        String id = cursor.getString(indiceId);
                        String tipo = cursor.getString(indiceTipo);
                        String dt = cursor.getString(indiceDt);
                        String response = cursor.getString(indiceResponse);
                        Log.i(
                                "consultas",
                                "Resultado - " +  "ID " + id + "\n"
                                        +  "tipo consulta: " + tipo
                                        + " \n"
                                        + "data consulta: " + dt
                                        + "\n" +
                                        "resultado gerado: " + response

                        );
                        cursor.moveToNext();
                    }
                    cursor.close();
                    bancoDados.close();
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(
                            new Runnable() {
                                public void run() {
                                    btnPesquisar.setText("PESQUISAR");;
                                }
                            },
                            500);
                } catch (Exception e) {
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(
                            new Runnable() {
                                public void run() {
                                    btnPesquisar.setText("PESQUISAR");;
                                }
                            },
                            500);
                    e.printStackTrace();

                }

            }
        });
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    btnAtualizar.setText("PROCESSANDO...");
                    SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);
                    int idAtualizar = Integer.parseInt(idConsultaAtualizar.getEditableText().toString());
                    String tipoConsulta = radioCep.isChecked() ? "cep" : "blockchain";
                    bancoDados.execSQL("UPDATE consultas  SET tipo = '" + tipoConsulta + "'" + " WHERE id = '" + idAtualizar + "'" );
                    String consulta = "SELECT * FROM consultas" ;
                    Cursor cursor = bancoDados.rawQuery(consulta, null);
                    Toast.makeText(CrudScreen.this, "Consulta atualizada com sucesso", Toast.LENGTH_SHORT).show();
                    int indiceId = cursor.getColumnIndex("id");
                    int indiceTipo = cursor.getColumnIndex("tipo");
                    int indiceDt = cursor.getColumnIndex("dt");
                    int indiceResponse = cursor.getColumnIndex("response");
                    cursor.moveToFirst();
                    while (cursor != null   && !cursor.isAfterLast()) {

                        String id = cursor.getString(indiceId);
                        String tipo = cursor.getString(indiceTipo);
                        String dt = cursor.getString(indiceDt);
                        String response = cursor.getString(indiceResponse);
                        Log.i(
                                "consultas",
                                "Resultado - " +  "ID " + id + "\n"
                                        +  "tipo consulta: " + tipo
                                        + " \n"
                                        + "data consulta: " + dt
                                        + "\n" +
                                        "resultado gerado: " + response

                        );
                        cursor.moveToNext();
                    }
                    cursor.close();
                    bancoDados.close();
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(
                            new Runnable() {
                                public void run() {
                                    btnAtualizar.setText("ATUALIZAR");;
                                }
                            },
                            500);

                } catch (Exception e) {
                    e.printStackTrace();
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(
                            new Runnable() {
                                public void run() {
                                    btnAtualizar.setText("ATUALIZAR");;
                                }
                            },
                            500);
                    Toast.makeText(CrudScreen.this, "Insira um id valido para atualizar", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    btnDeletar.setText("PROCESSANDO...");
                    SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);
                    int idDeletado = Integer.parseInt(idConsultaDeletar.getEditableText().toString());
                    bancoDados.execSQL("DELETE FROM consultas WHERE id = '" + idDeletado + "'");
                    String consulta = "SELECT * FROM consultas" ;
                    Cursor cursor = bancoDados.rawQuery(consulta, null);
                    Toast.makeText(CrudScreen.this, "Consulta deletada com sucesso", Toast.LENGTH_SHORT).show();
                    int indiceId = cursor.getColumnIndex("id");
                    int indiceTipo = cursor.getColumnIndex("tipo");
                    int indiceDt = cursor.getColumnIndex("dt");
                    int indiceResponse = cursor.getColumnIndex("response");
                    cursor.moveToFirst();
                    while (cursor != null   && !cursor.isAfterLast()) {

                        String id = cursor.getString(indiceId);
                        String tipo = cursor.getString(indiceTipo);
                        String dt = cursor.getString(indiceDt);
                        String response = cursor.getString(indiceResponse);
                        Log.i(
                                "consultas",
                                "Resultado - " +  "ID " + id + "\n"
                                        +  "tipo consulta: " + tipo
                                        + " \n"
                                        + "data consulta: " + dt
                                        + "\n" +
                                        "resultado gerado: " + response

                        );
                        cursor.moveToNext();
                    }
                    cursor.close();
                    bancoDados.close();
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(
                            new Runnable() {
                                public void run() {
                                   btnDeletar.setText("DELETAR");;
                                }
                            },
                            500);
                } catch (Exception e) {
                    e.printStackTrace();
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(
                            new Runnable() {
                                public void run() {
                                    btnDeletar.setText("DELETAR");;
                                }
                            },
                            500);
                    Toast.makeText(CrudScreen.this, "Insira um id valido para deletar", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

