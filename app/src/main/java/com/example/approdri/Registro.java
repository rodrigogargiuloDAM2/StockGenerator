package com.example.approdri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.approdri.Utils.Utils;



public class Registro extends AppCompatActivity {
    //camara
    private static  final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    private  String[] cameraPermissions;//camara y almacen
    private String[] storagePermissions;// almacen

    private Uri imageUri;

    //fin

    Button btnRegeistrar, btnIrInicioR, tomarFoto;
    EditText txtNombre, txtApellido, txtEmail, txtTelefono, txtContrasegna;
    boolean existe;

    AdminSQLiteOpenHelper helper = new AdminSQLiteOpenHelper(this,"administracion",null,1);


    ImageView imageView;
    Button  btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        btnRegeistrar = findViewById(R.id.btnRegistrarR);
        btn_save  = findViewById(R.id.btn_save);
        btn_save.setEnabled(false);
        btnRegeistrar.setEnabled(true);
        imageView = findViewById(R.id.imageView);

        txtNombre = findViewById(R.id.edtNombreR);
        txtApellido = findViewById(R.id.edtApellidoR);
        txtEmail = findViewById(R.id.edtEmailR);
        txtTelefono = findViewById(R.id.edtTelefonoR);
        txtContrasegna = findViewById(R.id.edtContrasegnaR);

        txtNombre.setEnabled(true);
        txtApellido.setEnabled(true);
        txtEmail.setEnabled(true);
        txtTelefono.setEnabled(true);
        txtContrasegna.setEnabled(true);

        btnIrInicioR =findViewById(R.id.btnIrInicioR);


        //Inicializar Permisos
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //tomarFoto
        tomarFoto=findViewById(R.id.btn_tomarFoto);
        tomarFoto.setEnabled(false);
        tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickDialog();
            }
        });


        btnIrInicioR.setEnabled(true);



        btnRegeistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.abrir();
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor emailRepetido = db.rawQuery("Select email from usuarios;",null);
                emailRepetido.moveToFirst();
                int nReg = emailRepetido.getCount();

                if (nReg ==0){
                    helper.insertarReg(String.valueOf(txtNombre.getText().toString()), String.valueOf(txtApellido.getText().toString()), String.valueOf(txtEmail.getText().toString()), String.valueOf(txtTelefono.getText().toString()), String.valueOf(txtContrasegna.getText().toString()));
                    Toast.makeText(Registro.this, "Registro almacenado con exito", Toast.LENGTH_SHORT).show();
                    btnIrInicioR.setEnabled(false);
                    txtNombre.setEnabled(false);
                    txtApellido.setEnabled(false);
                    txtEmail.setEnabled(false);
                    txtTelefono.setEnabled(false);
                    txtContrasegna.setEnabled(false);
                    btnRegeistrar.setEnabled(false);
                    tomarFoto.setEnabled(true);


                }else if (!txtEmail.getText().toString().isEmpty() && !txtNombre.getText().toString().isEmpty() && !txtApellido.getText().toString().isEmpty() && !txtTelefono.getText().toString().isEmpty() && !txtContrasegna.getText().toString().isEmpty() ){
                   emailRepetido.moveToFirst();
                do {
                    if (!txtEmail.getText().toString().equals(emailRepetido.getString(0))) {

                        existe = false;

                    } else {
                        existe=true;
                        Toast.makeText(Registro.this, "El email ya existe.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }while (emailRepetido.moveToNext());

                if (existe==false){
                    helper.insertarReg(String.valueOf(txtNombre.getText().toString()), String.valueOf(txtApellido.getText().toString()), String.valueOf(txtEmail.getText().toString()), String.valueOf(txtTelefono.getText().toString()), String.valueOf(txtContrasegna.getText().toString()));
                    btnIrInicioR.setEnabled(false);
                    txtNombre.setEnabled(false);
                    txtApellido.setEnabled(false);
                    txtEmail.setEnabled(false);
                    txtTelefono.setEnabled(false);
                    txtContrasegna.setEnabled(false);
                    btnRegeistrar.setEnabled(false);
                    tomarFoto.setEnabled(true);
                    Toast.makeText(Registro.this, "Usuario Insertado", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Registro.this, "Usuario no insertado", Toast.LENGTH_SHORT).show();
                }

                } else {
                    Toast.makeText(Registro.this, "Debes llenar todos los campos o el email ya existe.", Toast.LENGTH_SHORT).show();
                }
                helper.cerrar();


            }
        });





    }

    //Tomar Foto
    public void imagePickDialog(){
        String[] options = {"Camara","Galeria"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Seleccionar Imagen");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which==0){
                    //click en camara
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        //permiso ya otorgado
                        PickFromCamera();
                    }
                }
                else if (which==1){
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        //Permiso ya otorgado
                        PickFromGallery();
                    }
                }

            }
        });
        //crear y mostrar dialogo
        builder.create().show();
    }

    //imagen
    private void PickFromGallery(){
        //intento elegir la imagen de la galeria, la imagen se devolvera en el metodo onActivityResult
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }

    private void PickFromCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Título de la Imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Descripción de la imagen");
        //put image uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //Intento de abrir la cámara para la imagen
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);

    }

    private boolean checkStoragePermission(){
        //Comprobar si el permiso está habilitado
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        //solicita el permiso de almacenamiento
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        //verifica si el permiso está habilitado
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        //solicita permiso de camara
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //resultado del permiso permitido / denegado

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        //ambos permisos permitidos
                        PickFromCamera();
                    }
                    else {
                        Toast.makeText(this, "Se requieren permisos de cámara y almacenamiento", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted){
                        //permiso permitido
                        PickFromGallery();
                    }
                    else {
                        Toast.makeText(this, "Se requiere permiso de almacenamiento", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    //fin

    //Ctrl

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_GALLERY_CODE && resultCode == RESULT_OK && data != null)
        {
            Uri pickedImage = data.getData();

            imageView.setImageURI(pickedImage);

            btn_save.setEnabled(true);
        }else if (requestCode == IMAGE_PICK_CAMERA_CODE  && resultCode == RESULT_OK){


            imageView.setImageURI(imageUri);
            btn_save.setEnabled(true);


        }else {
            Toast.makeText(this, "Debe seleccionar una foto.", Toast.LENGTH_SHORT).show();
        }

    }

    public void irAInicio(View view){
        finish();
    }


    public void GuardarFoto(View view){
        final Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        //PutExtra
          //   Bundle miBundleR=this.getIntent().getExtras();
            // final String DatoEmail=miBundleR.getString("emailRodrigo");

        //Create dialog to enter name to save
        AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
        builder.setTitle("¿Estás seguro/a de seleccionar esta imágen?");

        final EditText editText = new EditText(Registro.this);
        builder.setView(editText);
        editText.setText(txtEmail.getText().toString());
        editText.setEnabled(false);

        //set button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(editText.getText().toString()))
                {
                    helper.addBitmap(editText.getText().toString(), Utils.getBytes(bitmap));
                    Toast.makeText(Registro.this, "Save success !!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(Registro.this, "Name can't be empty", Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.show();
    }

    /**
    @Override
    public void onBackPressed() {

    }
    **/
}

