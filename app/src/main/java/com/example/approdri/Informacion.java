package com.example.approdri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class Informacion extends AppCompatActivity {

    private RecyclerView recyclerViewInfo;
    // private RecyclerViewAdaptador adaptadorUsuarios;
    ArrayList<DatosOpciones> datosOpciones;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);


        datosOpciones=new ArrayList<>();

        recyclerViewInfo=findViewById(R.id.recyclerOpciones);
        //recyclerViewInfo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewInfo.setLayoutManager(new GridLayoutManager(this,2));


        llenarArticulos();


        AdaptadorInfoOpciones adaptador = new AdaptadorInfoOpciones(datosOpciones);
        recyclerViewInfo.setAdapter(adaptador);
    }

    private void llenarArticulos() {

        //ArrayList<DatosUsuarios> usuarios = new ArrayList<>();









            datosOpciones.add(new DatosOpciones("Insertar Categoría",R.drawable.in_categoria));
            datosOpciones.add(new DatosOpciones("Insertar Artículo",R.drawable.inventario));
            datosOpciones.add(new DatosOpciones("Economía",R.drawable.economia));
            datosOpciones.add(new DatosOpciones("Listado de Artículos",R.drawable.listado));
            datosOpciones.add(new DatosOpciones("Perfíl",R.drawable.perfil));
            datosOpciones.add(new DatosOpciones("Contacto",R.drawable.captura3));






    }
}
