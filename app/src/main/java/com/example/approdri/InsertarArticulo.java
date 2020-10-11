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
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.approdri.Utils.Utils;

import java.util.ArrayList;

public class InsertarArticulo extends AppCompatActivity {

    Button btnMas, btnMenos, btnInsertarArticulo;
    EditText txtNombreProducto, txtDescripcion, txtTienda, txtMarca, txtPrecio, txtCantidad, idProducto;
    int cantidad;




    //camara
    private static  final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    private  String[] cameraPermissions;//camara y almacen
    private String[] storagePermissions;// almacen

    private Uri imageUri;

    Button tomarFoto, btn_save;

    boolean existe;
    ImageView imageView;

    //fin



    //Spinner
    Spinner comboCategorias;
    ArrayList<String> listaCategorias; //Info Mostrada en el combo
    ArrayList<TipoCategorias> categoriaList;

    AdminSQLiteOpenHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_articulo);

        //Foto botones
        tomarFoto = findViewById(R.id.btn_tomarFotoProducto);
        btn_save=findViewById(R.id.btn_saveProducto);
        btn_save.setEnabled(false);
        imageView=findViewById(R.id.imageViewIP);
        btnMas=findViewById(R.id.btnMas);
        btnMenos=findViewById(R.id.btnMenos);

        idProducto=findViewById(R.id.id_producto);
        idProducto.setEnabled(false);

        txtNombreProducto=findViewById(R.id.edtNombreProducto);
        txtDescripcion=findViewById(R.id.edtDescripcion);
        txtTienda=findViewById(R.id.edtTienda);
        txtMarca=findViewById(R.id.edtMarca);
        txtPrecio=findViewById(R.id.edtPrecio);
        txtCantidad=findViewById(R.id.edtCantidad);

        txtCantidad.setText("0");
        btnInsertarArticulo=findViewById(R.id.btnInsertarArticulo);
        comboCategorias=findViewById(R.id.spCategoria);

        btnMas=findViewById(R.id.btnMas);
        btnMenos=findViewById(R.id.btnMenos);



        txtNombreProducto.setEnabled(true);
        txtDescripcion.setEnabled(true);
        txtTienda.setEnabled(true);
        txtMarca.setEnabled(true);
        txtPrecio.setEnabled(true);
        txtCantidad.setEnabled(false);
        btnInsertarArticulo.setEnabled(true);
        comboCategorias.setEnabled(true);
        btnMas.setEnabled(true);
        btnMenos.setEnabled(true);


        //Inicializar Permisos
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //tomarFoto
        tomarFoto=findViewById(R.id.btn_tomarFotoProducto);
        tomarFoto.setEnabled(false);
        tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickDialog();
            }
        });





        //spinner
        comboCategorias = findViewById(R.id.spCategoria);


        conn=new AdminSQLiteOpenHelper(getApplicationContext(),"administracion", null,1);

        consultarListaCategorias();

        ArrayAdapter<CharSequence> adaptador= new ArrayAdapter(this,android.R.layout.simple_spinner_item, listaCategorias);
        comboCategorias.setAdapter(adaptador);

        //Accion boton sumar
        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cantidad = Integer.valueOf(txtCantidad.getText().toString());//coge el valor del ET
                if (btnMas.isPressed()){
                    cantidad++;
                }
                txtCantidad.setText(cantidad +"");
            }
        });

        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cantidad = Integer.valueOf(txtCantidad.getText().toString());//coge el valor del ET
                if (btnMenos.isPressed()){
                    cantidad--;
                }

                txtCantidad.setText(cantidad +"");

                if (cantidad < 0){
                    txtCantidad.setText("0");
                }


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


    public void GuardarFotoProducto(View view){
        final Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        //PutExtra
        //   Bundle miBundleR=this.getIntent().getExtras();
        // final String DatoEmail=miBundleR.getString("emailRodrigo");
        Bundle miBundle2=this.getIntent().getExtras();
        String DatoEmail=miBundle2.getString("emailInsertar");

        //Create dialog to enter name to save
        AlertDialog.Builder builder = new AlertDialog.Builder(InsertarArticulo.this);
        builder.setTitle("¿Estás seguro/a de seleccionar esta imágen para el producto?");

        final EditText editText = new EditText(InsertarArticulo.this);
        final EditText nProducto = new EditText(InsertarArticulo.this);
        builder.setView(editText);
        builder.setView(nProducto);
        editText.setText(DatoEmail);
        editText.setEnabled(false);
        nProducto.setText(idProducto.getText());
        nProducto.setEnabled(false);

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
                    conn.addBitmapProductos(editText.getText().toString(), Utils.getBytes(bitmap),nProducto.getText().toString());
                    Toast.makeText(InsertarArticulo.this, "Save success !!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(InsertarArticulo.this, "Name can't be empty", Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.show();
    }




    private void consultarListaCategorias() {

        SQLiteDatabase db=conn.getReadableDatabase();

        TipoCategorias categoria= null;
        categoriaList = new ArrayList<TipoCategorias>();

        Cursor cursor=db.rawQuery("select*from categorias", null);
        while (cursor.moveToNext()){
            categoria=new TipoCategorias();
            categoria.setCategoria(cursor.getString(0));

            categoriaList.add(categoria);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaCategorias =new ArrayList<String>();
        listaCategorias.add("Seleccione");

        for (int i = 0; i< categoriaList.size(); i++){
            listaCategorias.add(categoriaList.get(i).getCategoria());
        }

    }

    public void Insertar(View view){

        conn = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        Bundle miBundle2=this.getIntent().getExtras();
        String DatoEmail=miBundle2.getString("emailInsertar");

        String nombreP = txtNombreProducto.getText().toString();
        String descripcionP = txtDescripcion.getText().toString();
        String tiendaP = txtTienda.getText().toString();
        String marcaP = txtMarca.getText().toString();
        String categoriaP = comboCategorias.getSelectedItem().toString();
        String precioP = txtPrecio.getText().toString();
        String cantidadP = txtCantidad.getText().toString();

        if (!nombreP.isEmpty() && !descripcionP.isEmpty() && !tiendaP.isEmpty() && !marcaP.isEmpty() && !categoriaP.equals("Seleccione") && !precioP.isEmpty() && !cantidadP.isEmpty()){
            // Guardar en BBDD los calores
            ContentValues producto = new ContentValues();

            producto.put("nombreProducto",nombreP);
            producto.put("descripcion",descripcionP);
            producto.put("tienda",tiendaP);
            producto.put("marca",marcaP);
            producto.put("categoria",categoriaP);
            producto.put("precio",precioP);
            producto.put("cantidad",cantidadP);
            producto.put("email",DatoEmail);

            //Guardar en tabla
            db.insert("productos",null,producto);

            Cursor cId= db.rawQuery("select id_producto from productos where email='"+DatoEmail+"'",null);
            cId.moveToLast();
            idProducto.setText(cId.getString(0));


            db.close();

            Toast.makeText(this, "Artículo registrado exitosamente!!", Toast.LENGTH_SHORT).show();
            tomarFoto.setEnabled(true);
            txtNombreProducto.setEnabled(false);
            txtDescripcion.setEnabled(false);
            txtTienda.setEnabled(false);
            txtMarca.setEnabled(false);
            txtPrecio.setEnabled(false);
            txtCantidad.setEnabled(false);
            comboCategorias.setEnabled(false);
            btnInsertarArticulo.setEnabled(false);
            btnMas.setEnabled(false);
            btnMenos.setEnabled(false);

            //Intent i = new Intent(getApplicationContext(),Inicio.class);
            //startActivity(i);
            Toast.makeText(this, "Ahora ingrese una foto", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    //Método para que no se pueda ir para atrás con el botón del móvil
    @Override
    public void onBackPressed() {
        finish();
       // Toast.makeText(this, "Botón atrás inhabilitado", Toast.LENGTH_SHORT).show();
    }
}
