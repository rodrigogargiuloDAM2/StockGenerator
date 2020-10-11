package com.example.approdri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.approdri.Utils.Utils;

public class Inicio extends AppCompatActivity {
    TextView txtNombreU;

    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion",null,1);
    SQLiteDatabase db;
    ImageView avatarImage;
  //  Bundle datos;
    CardView cardInsertar, cardCategoria, cardEconomia, cardListadoArticulos ,cardPerfil, cardInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

      //  datos = getIntent().getExtras();
       // String datosObtenidos = datos.getString("datoEmail");


        Bundle miBundle=this.getIntent().getExtras();
        String DatoEmail=miBundle.getString("email");
        avatarImage = findViewById(R.id.avatarImage);
        avatarImage.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
        //Mostrar imagaen
        byte[] data = admin.getBitmapByName(DatoEmail);
        if (data != null)
        {
            Bitmap bitmap = Utils.getImage(data);
            avatarImage.setImageBitmap(bitmap);
        }
        else
        {
            Toast.makeText(getApplicationContext(), DatoEmail + " not found", Toast.LENGTH_SHORT).show();
        }


        //fin

        txtNombreU = findViewById(R.id.textNickName);
        txtNombreU.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));

        txtNombreU.setText(DatoEmail);

        //Cards
        cardInsertar=findViewById(R.id.cardInsertarArt);
        cardCategoria=findViewById(R.id.cardCategoria);
        cardEconomia=findViewById(R.id.cardEconomia);
        cardListadoArticulos=findViewById(R.id.cardListadoArticulos);
        cardPerfil=findViewById(R.id.cardPerfil);
        cardInfo=findViewById(R.id.cardInfo);

        //onClicks
        cardInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),InsertarArticulo.class);

                Bundle miBundle2=new Bundle();
                miBundle2.putString("emailInsertar",txtNombreU.getText().toString());

                intent.putExtras(miBundle2);


                startActivity(intent);

            }
        });

        cardCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Categoria.class);
                startActivity(intent);

            }
        });


        cardEconomia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Economia.class);

                Bundle miBundle2=new Bundle();
                miBundle2.putString("emailEcon",txtNombreU.getText().toString());

                intent.putExtras(miBundle2);

                startActivity(intent);

            }
        });

        cardListadoArticulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RecyclerArticulos.class);

                Bundle miBundle2=new Bundle();
                miBundle2.putString("emailInsertarR",txtNombreU.getText().toString());
                //miBundle2.putString("emailListado",txtNombreU.getText().toString());

                intent.putExtras(miBundle2);

                startActivity(intent);

            }
        });

        cardPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Perfil.class);

                Bundle miBundle2=new Bundle();
                miBundle2.putString("email2",txtNombreU.getText().toString());

                intent.putExtras(miBundle2);

                startActivity(intent);

            }
        });

        cardInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Informacion.class);
                startActivity(i);
            }
        });
    }

    //Método para no retroceder con el botón del móvil


    @Override
    public void onBackPressed() {

    }
}
