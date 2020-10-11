package com.example.approdri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ListadoArticulos extends AppCompatActivity {

    AdminSQLiteOpenHelper conn = new AdminSQLiteOpenHelper(this,"administracion", null, 1);


    EditText txt_idP, txt_nombreP, txt_descripcionP, txt_tiendaP, txt_marcaP, txt_precioP, txt_cantidadP, txtEmailP;

   //Spinner
    Spinner sp_categoriaP;
    ArrayList<String> listaCategorias; //Info Mostrada en el combo
    ArrayList<TipoCategorias> categoriaList;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_articulos);


        txt_idP = findViewById(R.id.et_idL);
        txt_nombreP = findViewById(R.id.et_NomPL);
        txt_descripcionP = findViewById(R.id.et_DescrL);
        txt_tiendaP = findViewById(R.id.et_tiendaL);
        txt_marcaP = findViewById(R.id.et_MarcaL);
        txt_precioP = findViewById(R.id.et_precioL);
        txt_cantidadP = findViewById(R.id.et_CantidadL);
        txtEmailP = findViewById(R.id.et_EmailL);

        //spinner
        sp_categoriaP = findViewById(R.id.sp_CatL);

        consultarListaCategorias();

        ArrayAdapter<CharSequence> adaptador= new ArrayAdapter(this,android.R.layout.simple_spinner_item, listaCategorias);
        sp_categoriaP.setAdapter(adaptador);

        //Colocar email artículo
        Bundle miBundle2=this.getIntent().getExtras();
        String DatoEmail=miBundle2.getString("emailListado");


        txtEmailP.setText(DatoEmail);





    }

    public void consultarArticulos(View view) {

        SQLiteDatabase db = conn.getReadableDatabase();

        Bundle miBundle2=this.getIntent().getExtras();
        String DatoEmail=miBundle2.getString("emailListado");

        if (db!=null){
            Cursor c = db.rawQuery("Select * from productos where email='"+DatoEmail+"'",null);
            int cantidad = c.getCount();
            int i = 0;
            String[] arreglo = new String[cantidad];
            if (c.moveToFirst()){
                do {
                    String linea = "\nID: "+c.getInt(0) +"  \nNombre Producto: "+c.getString(1) + "  \nDescripción: " +c.getString(2) + "  \nTienda: " + c.getString(3)+ "  \nMarca: " + c.getString(4) +"  \nCategoría: " + c.getString(5) + "  \nPrecio: " + c.getInt(6) + "  \nCantidad: " + c.getInt(7) + "  \nEmail: " + c.getString(8);
                    arreglo[i] = linea;
                    i++;
                }while (c.moveToNext());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arreglo);
            ListView lista = findViewById(R.id.lv_listadoArticulos);
            lista.setAdapter(adapter);
        }


        db.close();

    }

    public void Modificar(View view){
        conn = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase(); //abrimos BBDD

        Bundle miBundle2=this.getIntent().getExtras();
        String DatoEmail=miBundle2.getString("emailListado");
        String idP = txt_idP.getText().toString();
        Cursor cursor = db.rawQuery("Select id_producto from productos where email='"+DatoEmail+"' and id_producto="+idP,null);

        String nombreP = txt_nombreP.getText().toString();
        String descripcionP = txt_descripcionP.getText().toString();
        String tiendaP = txt_tiendaP.getText().toString();
        String marcaP = txt_marcaP.getText().toString();
        String categoriaP = sp_categoriaP.getSelectedItem().toString();
        String precioP = txt_precioP.getText().toString();
        String cantidadP = txt_cantidadP.getText().toString();

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
                } else {
                    Toast.makeText(this, "El artículo no existe", Toast.LENGTH_SHORT).show();

                }



        }else {
            Toast.makeText(this, "No existe ese ID de artículo.", Toast.LENGTH_SHORT).show();
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


    public void Buscar(View view){
        conn = new AdminSQLiteOpenHelper(this, "administracion", null,1);
        SQLiteDatabase db = conn.getWritableDatabase();

        String idP = txt_idP.getText().toString();
        Bundle miBundle2=this.getIntent().getExtras();
        String DatoEmail=miBundle2.getString("emailListado");


        if (!idP.isEmpty()){
            Cursor fila = db.rawQuery
                    ("select id_producto, nombreProducto,descripcion,tienda, marca, categoria,precio, cantidad, email from productos where id_producto =" + idP+" and email='"+DatoEmail+"'", null);

            if (fila.moveToFirst()){ //identifica si la consulta contiene valores para mostrarlo
                //colocar consulta
                txt_nombreP.setText(fila.getString(1));//el primero es siempre 0
                txt_descripcionP.setText(fila.getString(2));
                txt_tiendaP.setText(fila.getString(3));
                txt_marcaP.setText(fila.getString(4));
                txt_precioP.setText(fila.getString(6));
                txt_cantidadP.setText(fila.getString(7));
                txt_cantidadP.setText(fila.getString(7));
                txtEmailP.setText(fila.getString(8));


                db.close();
            }else {
                Toast.makeText(this, "No existe el articulo", Toast.LENGTH_SHORT).show();
                db.close();
            }

        }else {
            Toast.makeText(this, "Debes introducir el ID", Toast.LENGTH_SHORT).show();
        }
    }


    //Método para eliminar articulo
    public void Eliminar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper
                (this,"administracion",null,1);

        SQLiteDatabase db = admin.getWritableDatabase();
        Bundle miBundle2=this.getIntent().getExtras();
        String DatoEmail=miBundle2.getString("emailListado");

        String idP = txt_idP.getText().toString();

        if (!idP.isEmpty()){
            int cantidad = db.delete("productos","id_producto=" + idP + " and email='"+DatoEmail+"'"  , null);  //cantidad de articulos borrados ,, metodo delete
            db.close();

            sp_categoriaP.setSelection(0);
            txt_nombreP.setText("");
            txt_descripcionP.setText("");
            txt_tiendaP.setText("");
            txt_marcaP.setText("");
            txt_precioP.setText("");
            txt_cantidadP.setText("");


            if (cantidad == 1){
                Toast.makeText(this, "Articulo eliminado correctamente", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "El articulo no existe", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Debes introducir el código", Toast.LENGTH_SHORT).show();
        }


    }

    public void irRecyclerArti(View view){
        Intent i = new Intent(getApplicationContext(),RecyclerArticulos.class);
        Bundle miBundleR=new Bundle();
        miBundleR.putString("emailInsertarR",txtEmailP.getText().toString());

        i.putExtras(miBundleR);
        startActivity(i);
    }
}
