package com.example.ejemplo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ejemplo.adaptadores.PersonajeAdaptador;
import com.example.ejemplo.clases.Personaje;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Personaje> listaPersonaje = new ArrayList<>();

    Button btn_guardar;
    RecyclerView rcv_personajes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcv_personajes = findViewById(R.id.rcv_personajes);
        btn_guardar = findViewById(R.id.btn_guardar);
        cargarInformacion();
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarInformacion();
            }
        });
        cargarInformacion();

    }

    private void guardarInformacion() {
        HashMap<String,Object> json = new HashMap<>();
        json.put("id",101);
        json.put("title","asd");
        json.put("body","asd");
        json.put("userId","asd");

        JSONObject jsonObject = new JSONObject(json);

        String url = "https://jsonplaceholder.typicode.com/posts";
        JsonObjectRequest myreque = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                recibirInformacion(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_LONG).show();

            }
        });
        RequestQueue rg = Volley.newRequestQueue(getApplicationContext());
        rg.add(myreque);

    }

    public void recibirInformacion(JSONObject respuesta){
        try {
            if (respuesta.getInt("id")==101){
                Toast.makeText(getApplicationContext(), "Se guardo exitosamente", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "error guardando", Toast.LENGTH_LONG).show();

            }
        }catch (JSONException e){
            Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_LONG).show();
        }
    }
    public void cargarInformacion() {
        String url = "https://rickandmortyapi.com/api/character";

        // metodos http (SET, GET, POST)
        StringRequest myreque = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    recibirRespuesta(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_LONG).show();
            }
        });


        RequestQueue rg = Volley.newRequestQueue(getApplicationContext());
        rg.add(myreque);



    }

    public void recibirRespuesta(JSONObject respuesta) {
        // siempre utilizar un try-catch para un jsonObject
        try {

            for (int i=0; i<=respuesta.getJSONArray("results").length(); i++){
                String nombre = respuesta.getJSONArray("results").getJSONObject(i).getString("name");
                String estado = respuesta.getJSONArray("results").getJSONObject(i).getString("status");
                String especie = respuesta.getJSONArray("results").getJSONObject(i).getString("species");
                String imagen = respuesta.getJSONArray("results").getJSONObject(i).getString("image");


                Personaje p = new Personaje(nombre, estado, especie, imagen);

                listaPersonaje.add(p);



            }

            rcv_personajes.setLayoutManager(new LinearLayoutManager(this));
            rcv_personajes.setAdapter(new PersonajeAdaptador(listaPersonaje));



        }catch (JSONException e){
            Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_LONG).show();
        }
    }
}