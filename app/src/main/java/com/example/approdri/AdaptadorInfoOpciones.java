package com.example.approdri;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorInfoOpciones extends RecyclerView.Adapter<AdaptadorInfoOpciones.ViewHolder>  {

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private static TextView descripcion;
        private static ImageView imgOpcion;
      //  private static CardView cardArticulosList;
        //int varUsu=0;


        //COntexto
        Context context;

        public ViewHolder( View itemView) {
            super(itemView);
            context = itemView.getContext();

            descripcion=itemView.findViewById(R.id.txt_info_opcion);
            imgOpcion=itemView.findViewById(R.id.iv_opcion);
        }

    }

    public ArrayList<DatosOpciones> opcionesList;

    public AdaptadorInfoOpciones(ArrayList<DatosOpciones> opcionesList) {
        this.opcionesList = opcionesList;
    }

    @Override
    public AdaptadorInfoOpciones.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       // View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.disegno_recycler_articulos,parent,false);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.disegno_informacion,parent,false);



        return new AdaptadorInfoOpciones.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final DatosOpciones item = opcionesList.get(position);


        holder.setIsRecyclable(false); //Para que los datos no se pierdan

        //holder.cardArticulosList.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_transition_animation));

        holder.descripcion.setText(item.getDescripcion());
        holder.imgOpcion.setImageResource(item.getImagenOpcion());




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(holder.itemView.getContext(), PerfilOpciones.class);
                intent.putExtra("itemDetailTitulo", item);
                holder.itemView.getContext().startActivity(intent);

            }
        });
        // holder.setOnClickListener();
     /**   holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), PerfilArticulos.class);

                holder.itemView.getContext().startActivity(intent);
            }
        });**/



    }




    @Override
    public int getItemCount() {
        return opcionesList.size();
    }
}
