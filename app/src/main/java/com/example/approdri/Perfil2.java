package com.example.approdri;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.approdri.Utils.Utils;

public class Perfil2 extends AppCompatActivity {

    private TextView idMD,emailMD, nombreMD, apellidoMD,telefonoMD;
    private DatosUsuarios itemDetail;
    AdminSQLiteOpenHelper conn=new AdminSQLiteOpenHelper(this,"administracion",null,1);
    ImageView imgP2;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil2);

        initViews();
        initValues();

        String email = itemDetail.getEmail();

        //Mostrar imagaen
        byte[] data = conn.getBitmapByName(email);
        if (data != null)
        {
            Bitmap bitmap = Utils.getImage(data);
            imgP2.setImageBitmap(bitmap);
        }
        else
        {
            Toast.makeText(getApplicationContext(), email + " not found", Toast.LENGTH_SHORT).show();
        }


        db=conn.getReadableDatabase();
        Cursor consultaDatosP2 = db.rawQuery("select id,nombre,apellido,telefono,email from usuarios where email='"+ email +"'",null);
        consultaDatosP2.moveToFirst();
        nombreMD.setText("NOMBRE: "+ consultaDatosP2.getString(1));
        idMD.setText("ID: "+consultaDatosP2.getString(0));
        apellidoMD.setText("APELLIDO: "+consultaDatosP2.getString(2));
        telefonoMD.setText("TELÉFONO: "+consultaDatosP2.getString(3));
        db.close();




    }

    private void initValues() {
        itemDetail = (DatosUsuarios) getIntent().getExtras().getSerializable("itemDetail");
        emailMD.setText(itemDetail.getEmail());


    }

    private void initViews() {
        nombreMD=findViewById(R.id.tv_nombreP2);
        apellidoMD=findViewById(R.id.tv_apellidoP2);
        emailMD=findViewById(R.id.tv_emailP2);
        telefonoMD=findViewById(R.id.tv_telefonoP2);
        idMD=findViewById(R.id.tv_idP2);
        imgP2=findViewById(R.id.imgPerfil2);

    }
}
