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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.approdri.Utils.Utils;

import java.util.ArrayList;

public class PerfilArticulos extends AppCompatActivity {

    private TextView id_producto, nombreProducto, descripcion, tienda, marca, categoria, precio, cantidad,emailPArt;
    private ImageView imgPArt;
    private Spinner sp_categoria;
    private DatosArticulos itemDetailEmail, itemDetailId;
    AdminSQLiteOpenHelper conn=new AdminSQLiteOpenHelper(this,"administracion",null,1);
    SQLiteDatabase db;


    //camara
    private static  final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    private  String[] cameraPermissions;//camara y almacen
    private String[] storagePermissions;// almacen

    private Uri imageUri;

    Button tomarFoto, btn_save , sinMod;

    boolean existe;
    ImageView imageView;

    //fin

    //Spinner
    ArrayList<String> listaCategorias; //Info Mostrada en el combo
    ArrayList<TipoCategorias> categoriaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_articulos);


        initViews();
        initValues();


        String id = itemDetailId.getId_producto();//?
        String email = itemDetailEmail.getEmail();

        //Mostrar imagaen
        byte[] data = conn.getBitmapByNameProductos(email, id);
        if (data != null)
        {
            Bitmap bitmap = Utils.getImage(data);
            imgPArt.setImageBitmap(bitmap);
        }
        else
        {
            Toast.makeText(getApplicationContext(), email + " not found", Toast.LENGTH_SHORT).show();
        }


        db=conn.getReadableDatabase();
        Cursor consultaDatosP2 = db.rawQuery("select * from productos where email='"+ email +"' and id_producto='" + id + "';",null);
        consultaDatosP2.moveToFirst();
        //id_producto.setText("ID: " + consultaDatosP2.getString(0));
        nombreProducto.setText(consultaDatosP2.getString(1));
        descripcion.setText(consultaDatosP2.getString(2));
        tienda.setText( consultaDatosP2.getString(3));
        marca.setText(consultaDatosP2.getString(4));
        categoria.setText(consultaDatosP2.getString(5));
        precio.setText(consultaDatosP2.getString(6));
        cantidad.setText(consultaDatosP2.getString(7));

        db.close();



    }

    private void initValues() {

        itemDetailEmail = (DatosArticulos) getIntent().getExtras().getSerializable("itemDetailProductos");
        emailPArt.setText(itemDetailEmail.getEmail());

        itemDetailId = (DatosArticulos) getIntent().getExtras().getSerializable("itemDetailProductosId");
        id_producto.setText(itemDetailEmail.getId_producto());



    }


    private void initViews() {

        id_producto=findViewById(R.id.tv_idPArt);
        nombreProducto=findViewById(R.id.tv_productoPArt);
        descripcion=findViewById(R.id.tv_descripcionPArt);
        tienda=findViewById(R.id.tv_tiendaPArt);
        marca=findViewById(R.id.tv_marcaPArt);
        categoria=findViewById(R.id.tv_categoriaPArt);
        precio=findViewById(R.id.tv_precioPArt);
        cantidad=findViewById(R.id.tv_cantidadPArt);
        emailPArt=findViewById(R.id.tv_emailPArt);
        imgPArt=findViewById(R.id.imgPerfilArticulos);
        sinMod=findViewById(R.id.btnSalirProdMP);
        sinMod.setEnabled(true);

        //Spinner
        sp_categoria=findViewById(R.id.sp_categoriaPArt);

        consultarListaCategorias();

        ArrayAdapter<CharSequence> adaptador= new ArrayAdapter(this,android.R.layout.simple_spinner_item, listaCategorias);
        sp_categoria.setAdapter(adaptador);

        emailPArt.setEnabled(false);

        //Foto botones
        tomarFoto = findViewById(R.id.btn_tomarFotoProdMP);
        btn_save=findViewById(R.id.btn_saveProdMP);
        btn_save.setEnabled(false);
        imageView=findViewById(R.id.imageViewProductoMP);


        //Inicializar Permisos
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //tomarFoto
        //tomarFoto=findViewById(R.id.btn_tomarFotoProdMP);
       // tomarFoto.setEnabled(false);
        tomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickDialog();
            }
        });

    }

    public void ModificarArticulo(View view){
        conn = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase(); //abrimos BBDD

       // Bundle miBundle2=this.getIntent().getExtras();
        String DatoEmail=emailPArt.getText().toString();
        String idP = id_producto.getText().toString();
        Cursor cursor = db.rawQuery("Select id_producto from productos where email='"+ DatoEmail+"' and id_producto="+idP,null);

        String nombreP = nombreProducto.getText().toString();
        String descripcionP = descripcion.getText().toString();
        String tiendaP = tienda.getText().toString();
        String marcaP = marca.getText().toString();
        String categoriaP = sp_categoria.getSelectedItem().toString();
        String precioP = precio.getText().toString();
        String cantidadP = cantidad.getText().toString();

        if (cursor.moveToFirst() && !idP.isEmpty() && !nombreP.isEmpty() && !descripcionP.isEmpty() && !tiendaP.isEmpty() && !marcaP.isEmpty() && !categoriaP.equals("Seleccione") && !precioP.isEmpty() && !cantidadP.isEmpty()) {


            // Guardar en BBDD los calores
            ContentValues producto = new ContentValues();

            producto.put("nombreProducto", nombreP);
            producto.put("descripcion", descripcionP);
            producto.put("tienda", tiendaP);
            producto.put("marca", marcaP);
            producto.put("categoria", categoriaP);
            producto.put("precio", precioP);
            producto.put("cantidad", cantidadP);

            int cantidad = db.update("productos", producto, "id_producto=" + idP, null);  // cantidad de registros actualizados,, Metodo update
            db.close();

            //Validar
            if (cantidad == 1) {
                Toast.makeText(this, "Artículo modificado correctamente", Toast.LENGTH_SHORT).show();
                //sinMod.setEnabled(false);
                // Intent i = new Intent(getApplicationContext(),MainActivity.class);
               // startActivity(i);
              //  finish();
            } else {
                Toast.makeText(this, "El artículo no existe", Toast.LENGTH_SHORT).show();

            }



        }else {
            Toast.makeText(this, "No existe ese ID de artículo.", Toast.LENGTH_SHORT).show();
        }


    }

    //Método para eliminar articulo
    public void EliminarArticulo(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper
                (this,"administracion",null,1);

        SQLiteDatabase db = admin.getWritableDatabase();

        String DatoEmail=emailPArt.getText().toString();
        String idP = id_producto.getText().toString();

        if (!idP.isEmpty()){
            int cantidad = db.delete("productos","id_producto=" + idP + " and email='"+DatoEmail+"'"  , null);  //cantidad de articulos borrados ,, metodo delete
            db.close();

          /**  sp_categoria.setSelection(0);
            txt_nombreP.setText("");
            txt_descripcionP.setText("");
            txt_tiendaP.setText("");
            txt_marcaP.setText("");
            txt_precioP.setText("");
            txt_cantidadP.setText("");**/


            if (cantidad == 1){
                Toast.makeText(this, "Articulo eliminado correctamente", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }else {
                Toast.makeText(this, "El articulo no existe", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Debes introducir el código", Toast.LENGTH_SHORT).show();
        }


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


    public void GuardarFotoProductoMod(View view){
        final Bitmap bitmap2 = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        //PutExtra
        //   Bundle miBundleR=this.getIntent().getExtras();
        // final String DatoEmail=miBundleR.getString("emailRodrigo");
        //Bundle miBundle2=this.getIntent().getExtras();
        String DatoEmail= emailPArt.getText().toString();

        //Create dialog to enter name to save
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilArticulos.this);
        builder.setTitle("¿Estás seguro/a de seleccionar esta imágen para el producto?");

        final EditText editText = new EditText(PerfilArticulos.this);
        final EditText idproducto = new EditText(PerfilArticulos.this);
        builder.setView(editText);
        builder.setView(idproducto);
        editText.setText(DatoEmail);
        editText.setEnabled(false);
        idproducto.setText(id_producto.getText());
        idproducto.setEnabled(false);

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
                    conn.addBitmapProductos(editText.getText().toString(), Utils.getBytes(bitmap2),idproducto.getText().toString());
                    Toast.makeText(PerfilArticulos.this, "Save success !!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(PerfilArticulos.this, "Name can't be empty", Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.show();
    }

    //Método para que no se pueda ir para atrás con el botón del móvil
    @Override
    public void onBackPressed() {

        // Toast.makeText(this, "Botón atrás inhabilitado", Toast.LENGTH_SHORT).show();
    }

    public void SinMod(View view)
    {
        finish();
    }
}
