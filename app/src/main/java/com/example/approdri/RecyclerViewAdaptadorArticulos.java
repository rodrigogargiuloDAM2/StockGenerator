package com.example.approdri;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdaptadorArticulos extends RecyclerView.Adapter<RecyclerViewAdaptadorArticulos.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private static TextView email ,nombreProducto, idProducto;
        private static CardView cardArticulosList;
        //int varUsu=0;


        //COntexto
        Context context;

        public ViewHolder( View itemView) {
            super(itemView);
            context = itemView.getContext();


            email=itemView.findViewById(R.id.tv_email_RVA);
            nombreProducto=itemView.findViewById(R.id.tv_producto_RVA);
            idProducto=itemView.findViewById(R.id.tv_id_RVA);
            cardArticulosList=itemView.findViewById(R.id.cardArticulosList);




        }

    }

    public ArrayList<DatosArticulos> articulosList;

    public RecyclerViewAdaptadorArticulos(ArrayList<DatosArticulos> articulosList) {
        this.articulosList = articulosList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.disegno_recycler_articulos,parent,false);
        //View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_usuarios,parent,false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdaptadorArticulos.ViewHolder holder, int position) {
        final DatosArticulos item = articulosList.get(position);


        holder.setIsRecyclable(false); //Para que los datos no se pierdan

        holder.cardArticulosList.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_transition_animation));

        holder.email.setText(item.getEmail());
        holder.nombreProducto.setText(item.getNombreProducto());
        holder.idProducto.setText(item.getId_producto());



        // holder.setOnClickListener();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), PerfilArticulos.class);
                intent.putExtra("itemDetailProductos", item);
                intent.putExtra("itemDetailProductosId",item);
                holder.itemView.getContext().startActivity(intent);
            }
        });



    }



    @Override
    public int getItemCount() {
        return articulosList.size();
    }
}

