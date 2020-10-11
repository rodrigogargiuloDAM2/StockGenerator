package com.example.approdri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.approdri.Utils.Utils;

public class Perfil extends AppCompatActivity {

    AdminSQLiteOpenHelper conn = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
    TextView tv_id,tv_nombre, tv_apellido, tv_email, tv_tel;
    ImageView imagenPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        SQLiteDatabase db = conn.getWritableDatabase();
        //Bundle
        Bundle miBundle2=this.getIntent().getExtras();
        String DatoEmail=miBundle2.getString("email2");

        tv_id=findViewById(R.id.tv_idP2);
        tv_nombre=findViewById(R.id.tv_nombreP2);
        tv_apellido=findViewById(R.id.tv_apellidoP2);
        tv_email=findViewById(R.id.tv_emailP2);
        tv_tel=findViewById(R.id.tv_telefonoP2);
        imagenPerfil=findViewById(R.id.imgPerfil2);

        //Imagen
        //Bundle miBundle=this.getIntent().getExtras();
        //String DatoEmail2=miBundle.getString("email2");
        imagenPerfil = findViewById(R.id.imgPerfil2);

        //Mostrar imagen
        byte[] data = conn.getBitmapByName(DatoEmail);
        if (data != null)
        {
            Bitmap bitmap = Utils.getImage(data);
            imagenPerfil.setImageBitmap(bitmap);
        }
        else
        {
            Toast.makeText(getApplicationContext(), DatoEmail + " not found", Toast.LENGTH_SHORT).show();
        }








        Cursor datosPerfil=db.rawQuery("SELECT id,nombre,apellido, email, telefono FROM usuarios where email='"+DatoEmail+"'",null);
        while (datosPerfil.moveToNext()) {

            tv_id.setText(datosPerfil.getString(0));
            tv_nombre.setText(datosPerfil.getString(1));
            tv_apellido.setText(datosPerfil.getString(2));
            tv_email.setText(datosPerfil.getString(3));
            tv_tel.setText(datosPerfil.getString(4));

        }

        db.close();
        datosPerfil.close();




    }

    public void IrModPerf(View view){
        Intent intent = new Intent(getApplicationContext(),ModPerfil.class);

        Bundle miBundle2=new Bundle();
        miBundle2.putString("email3",tv_email.getText().toString());

        intent.putExtras(miBundle2);

        startActivity(intent);
    }



    public void CerrarSesion(View view){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

}
