package com.example.approdri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.approdri.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PruebaReciclerView extends AppCompatActivity {

    private RecyclerView recyclerViewUsuario;
   // private RecyclerViewAdaptador adaptadorUsuarios;
    ArrayList<DatosUsuarios> datosUsuariosList;

    SQLiteDatabase db;
    AdminSQLiteOpenHelper admin= new AdminSQLiteOpenHelper(this,"administracion",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_recicler_view);


        datosUsuariosList=new ArrayList<>();

        recyclerViewUsuario=findViewById(R.id.recyclerUsuarios);
        recyclerViewUsuario.setLayoutManager(new LinearLayoutManager(this));
        //recyclerViewUsuario.setLayoutManager(new GridLayoutManager(this,2));
        
        
        llenarUsuarios();


       RecyclerViewAdaptador adaptador = new RecyclerViewAdaptador(datosUsuariosList);
       recyclerViewUsuario.setAdapter(adaptador);


    }

    private void llenarUsuarios() {
        //ArrayList<DatosUsuarios> usuarios = new ArrayList<>();


        db = admin.getReadableDatabase();
        Cursor datosUsuarios = db.rawQuery("Select email from usuarios", null);
        int numero_registro =datosUsuarios.getCount();


        while (datosUsuarios.moveToNext()){



            datosUsuariosList.add(new DatosUsuarios(datosUsuarios.getString(0)));



        }
        Toast.makeText(this, "Usuarios: " + numero_registro, Toast.LENGTH_SHORT).show();




    }

   /** public List<DatosUsuarios>obtenerUsuarios(){
        List<DatosUsuarios> usuarios = new ArrayList<>();
        db = admin.getReadableDatabase();
        Cursor datosUsuarios = db.rawQuery("Select id, nombre, apellido, email, telefono from usuarios", null);
        int numero_registro =datosUsuarios.getCount();
        DatosUsuarios user = null;

        while (datosUsuarios.moveToNext()){
           user =new DatosUsuarios();
           user.setId(datosUsuarios.getString(0));
           user.setNombre(datosUsuarios.getString(1));
           user.setApellido(datosUsuarios.getString(2));
           user.setEmail(datosUsuarios.getString(3));
           user.setTelefono(datosUsuarios.getString(4));

           usuarios.add(user);
        }
        Toast.makeText(this, "Usuarios: " + numero_registro, Toast.LENGTH_SHORT).show();

        return usuarios;
    }**/
}
