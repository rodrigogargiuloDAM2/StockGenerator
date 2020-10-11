package com.example.approdri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PerfilOpciones extends AppCompatActivity {

    TextView titulo;
    private DatosOpciones itemDetailTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_opciones);

        titulo=findViewById(R.id.tv_tituloOpc);
        itemDetailTitulo = (DatosOpciones) getIntent().getExtras().getSerializable("itemDetailTitulo");
        titulo.setText(itemDetailTitulo.getDescripcion());
    }
}
