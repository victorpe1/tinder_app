package com.example.upn_meeting.Match;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.upn_meeting.R;

import java.util.List;

public class matchAdapt extends RecyclerView.Adapter<matchView> {
    private List<match> matchesList;
    private Context context;


    public matchAdapt(List<match> matchesList, Context context) {
        this.matchesList = matchesList;
        this.context = context;
    }


    @Override
    public matchView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        matchView rcv = new matchView(layoutView);


        return rcv;
    }

    @Override
    public void onBindViewHolder(matchView holder, int position) {
        holder.mMatchId.setText(matchesList.get(position).getId_usuario());
        holder.mPresup.setText(matchesList.get(position).getFavoritos());
        holder.mDar.setText(matchesList.get(position).getEntrega());
        holder.mPerfil.setText(matchesList.get(position).getImagen_perfil());
        holder.mRecibir.setText(matchesList.get(position).getRecibo());
        holder.mNombre_match.setText(matchesList.get(position).getNombre());
        holder.mUltimoMensaje.setText(matchesList.get(position).getUltimoMensaje());
        String ultimo_visto = "";
        ultimo_visto = matchesList.get(position).getUltimoVisto();

        if (ultimo_visto.equals("true"))
            holder.mNotificationDot.setVisibility(View.VISIBLE);
        else
            holder.mNotificationDot.setVisibility(View.INVISIBLE);
        holder.mUltimoEstadoCon.setText(matchesList.get(position).getUltimoEstado_conectado());
        if (!matchesList.get(position).getImagen_perfil().equals("default")){
            Glide.with(context).load(matchesList.get(position).getImagen_perfil()).into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.matchesList.size();

    }
}
