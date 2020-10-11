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

public class ModPerfil extends AppCompatActivity {

    boolean existe;

    //camara
    private static  final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    private  String[] cameraPermissions;//camara y almacen
    private String[] storagePermissions;// almacen

    private Uri imageUri;

    AdminSQLiteOpenHelper conn = new AdminSQLiteOpenHelper(this,"administracion", null, 1);

    EditText txtID, txtNombre, txtApellido, txtEmail, txtTelefono, txtContrasegna;

    //Imagen

    ImageView imageView;
    Button tomarFoto ,btn_save, btnSalir, btnEliminarCuentaMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_perfil);

        //Botones imagen
        btn_save  = findViewById(R.id.btn_saveMod);
        btn_save.setEnabled(false);

        btnEliminarCuentaMP=findViewById(R.id.btnEliminarCuentaMP);
        btnEliminarCuentaMP.setEnabled(false);
        btnSalir = findViewById(R.id.btnSalirModP);
        btnSalir.setEnabled(true);


        tomarFoto = findViewById(R.id.btn_tomarFotoMod);
        tomarFoto.setEnabled(false);
        tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickDialog();
            }
        });

        imageView = findViewById(R.id.imageViewMod);

        //fin

        txtID = findViewById(R.id.edt_idM);
        txtNombre = findViewById(R.id.edt_NombreM);
        txtApellido = findViewById(R.id.edt_ApellidoM);
        txtEmail = findViewById(R.id.edt_EmailM);
        txtTelefono = findViewById(R.id.edt_TelM);
        txtContrasegna = findViewById(R.id.edt_contrasegnaM);

        txtNombre.setEnabled(true);
        txtApellido.setEnabled(true);
        txtEmail.setEnabled(true);
        txtTelefono.setEnabled(true);
        txtContrasegna.setEnabled(true);

        txtID.setEnabled(false);

        SQLiteDatabase db = conn.getWritableDatabase(); //abrimos BBDD


        Bundle miBundle2=this.getIntent().getExtras();
        String DatoEmailM=miBundle2.getString("email3");

        Cursor datosPerfil=db.rawQuery("SELECT id, nombre, apellido, email, telefono, contrasegna FROM usuarios where email='"+DatoEmailM+"'",null);
        while (datosPerfil.moveToNext()) {

            txtID.setText(datosPerfil.getString(0));
            txtNombre.setText( datosPerfil.getString(1));
            txtApellido.setText( datosPerfil.getString(2));
            txtEmail.setText(datosPerfil.getString(3));
            txtTelefono.setText(datosPerfil.getString(4));
            txtContrasegna.setText(datosPerfil.getString(5));
        }



        db.close();
        datosPerfil.close();





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

        onBackPressed();
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


    public void ModificarDatosPerfil(View view)
    {
     SQLiteDatabase db = conn.getWritableDatabase(); //abrimos BBDD
        String id=txtID.getText().toString();
        String nombre = txtNombre.getText().toString();
        String apellido = txtApellido.getText().toString();
        String email = txtEmail.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String contrasegna = txtContrasegna.getText().toString();

        if (!nombre.isEmpty() && !apellido.isEmpty() && !email.isEmpty() && !telefono.isEmpty() && !contrasegna.isEmpty()){

            Cursor validarEmail = db.rawQuery("select email from usuarios;",null);
            validarEmail.moveToFirst();
            Bundle miBundle2=this.getIntent().getExtras();
            String DatoEmailM=miBundle2.getString("email3");
            do {
                if (email.equals(validarEmail.getString(0)) && !email.equals(DatoEmailM)){
                     existe = true;
                    Toast.makeText(this, "El email ya existe", Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    existe = false;

                }
            }while (validarEmail.moveToNext());
            if (existe==false){
                ContentValues registro = new ContentValues();
                registro.put("nombre",nombre);
                registro.put("apellido",apellido);
                registro.put("email",email);
                registro.put("telefono",telefono);
                registro.put("contrasegna",contrasegna);

                int cantidad = db.update("usuarios",registro,"id="+ id,null);
                db.close();

                //Validar
                if (cantidad == 1){
                    txtNombre.setEnabled(false);
                    txtApellido.setEnabled(false);
                    txtEmail.setEnabled(false);
                    txtTelefono.setEnabled(false);
                    txtContrasegna.setEnabled(false);
                   // btnSalir.setEnabled(false);
                    tomarFoto.setEnabled(true);
                    btnEliminarCuentaMP.setEnabled(true);

                    Toast.makeText(this, "Perfíl modificado correctamente", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "La cuenta no existe", Toast.LENGTH_SHORT).show();

                }
            }


        }else {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void EliminarCuenta(View view){

        SQLiteDatabase db = conn.getWritableDatabase(); //abrimos BBDD
        String id=txtID.getText().toString();

        int cantidad = db.delete("usuarios","id=" + id, null);  //cantidad de articulos borrados ,, metodo delete


        int productos= db.delete("productos","email='"+ txtEmail.getText()+"'",null);
        db.close();

        if (productos != 0){
            Toast.makeText(this, "Artículos eliminados.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "ERROR?", Toast.LENGTH_SHORT).show();
        }

        if (cantidad == 1 ){
            Toast.makeText(this, "Cuenta eliminada correctamente", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }else {
            Toast.makeText(this, "La cuenta no existe", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_GALLERY_CODE && resultCode == RESULT_OK && data != null)
        {
            Uri pickedImage = data.getData();

            imageView.setImageURI(pickedImage);

            btn_save.setEnabled(true);
        }else if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode== RESULT_OK ){


            imageView.setImageURI(imageUri);
            btn_save.setEnabled(true);


        }else {
            Toast.makeText(this, "Error de algún tipo.", Toast.LENGTH_SHORT).show();
        }
    }


    public void ActualizarFoto(View view){
        final Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        //Create dialog to enter name to save
        AlertDialog.Builder builder = new AlertDialog.Builder(ModPerfil.this);
        builder.setTitle("¿Actualizar? (*La sesión se cerrará para aplicar los cambios*)");

        final EditText editText = new EditText(ModPerfil.this);
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
                    conn.addBitmap(editText.getText().toString(), Utils.getBytes(bitmap));
                    Toast.makeText(ModPerfil.this, "Save success !!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(ModPerfil.this, "Name can't be empty", Toast.LENGTH_SHORT).show();

                }
                finish();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
       // finish();
        //Intent i = new Intent(getApplicationContext(),MainActivity.class);
        //startActivity(i);
        //Toast.makeText(this, "Debes seleccionar/tomar una foto.", Toast.LENGTH_SHORT).show();

    }

    public void salirModPer(View view){
        finish();
    }
}
