package com.example.approdri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnIngresar, btnIrRegistro;
    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion",null,1);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIrRegistro=findViewById(R.id.btnRegistro);
        btnIngresar=findViewById(R.id.btnInicioSesion);

      /**  btnIrRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtEmail=findViewById(R.id.edt_email_I);
                Intent i = new Intent(getApplicationContext(),Registro.class);
                Bundle miBundle=new Bundle();
                miBundle.putString("emailR",txtEmail.getText().toString());
                startActivity(i);
            }
        });  **/


    }

    public void Registrarse(View view){
        EditText txtEmail=findViewById(R.id.edt_email_I);
        Intent i = new Intent(getApplicationContext(),Registro.class);
       // Bundle miBundleR=new Bundle();
        //miBundleR.putString("emailRodrigo",txtEmail.getText().toString());
        //i.putExtras(miBundleR);
        startActivity(i);
    }

    public void IngresarSesion(View view){
        EditText txtEmail=findViewById(R.id.edt_email_I);
        EditText txtContrasegna=findViewById(R.id.edt_password_I);

        admin.abrir();


        try {
            Cursor cursor=admin.ConsultarUsuPas(txtEmail.getText().toString(),
                    txtContrasegna.getText().toString());


            if (cursor.getCount()>0){
                Intent i = new Intent(getApplicationContext(),Inicio.class);

                Bundle miBundle=new Bundle();
                miBundle.putString("email",txtEmail.getText().toString());

                i.putExtras(miBundle);

                startActivity(i);
                finish();
            }else {
                Toast.makeText(MainActivity.this, "Usuario y/o Pass Incorrectos",
                        Toast.LENGTH_SHORT).show();
            }

            txtEmail.setText("");
            txtContrasegna.setText("");
            txtEmail.findFocus();
            admin.cerrar();
        }catch (SQLException e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "usuario y/o contraseña incorrecta", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {

    }

    //Ir a prueba Recycler
    public void PruebaRecycler(View view)
    {
        Intent i = new Intent(getApplicationContext(),PruebaReciclerView.class);
        startActivity(i);
    }
}