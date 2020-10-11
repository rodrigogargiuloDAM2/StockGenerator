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


public class RecyclerViewAdaptador extends RecyclerView.Adapter<RecyclerViewAdaptador.ViewHolder>  {



    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private static TextView  email ,nUsuario;
        int varUsu=0;
        private static CardView cardPerfiles;


        //COntexto
        Context context;

        public ViewHolder( View itemView) {
            super(itemView);
            context = itemView.getContext();


            email=itemView.findViewById(R.id.tv_email_L2);
            nUsuario=itemView.findViewById(R.id.tv_Usuario);
            cardPerfiles=itemView.findViewById(R.id.cardPerfiles);



        }

    }

    public ArrayList<DatosUsuarios> usuariosList;

    public RecyclerViewAdaptador(ArrayList<DatosUsuarios> usuariosList) {
        this.usuariosList = usuariosList;
    }

    // public RecyclerViewAdaptador(List<DatosUsuarios> usuariosList){
     //   this.usuariosList=usuariosList;
    //}


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.disegno_recycler,parent,false);
        //View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_usuarios,parent,false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder, int position) {
        final DatosUsuarios item = usuariosList.get(position);

        
        holder.setIsRecyclable(false); //Para que los datos no se pierdan

        holder.cardPerfiles.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_transition_animation));

        holder.email.setText(usuariosList.get(position).getEmail());
        holder.email.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_transition_animation));




        // holder.setOnClickListener();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), Perfil2.class);
                intent.putExtra("itemDetail", item);
                holder.itemView.getContext().startActivity(intent);
            }
        });



    }



    @Override
    public int getItemCount() {
        return usuariosList.size();
    }
}
