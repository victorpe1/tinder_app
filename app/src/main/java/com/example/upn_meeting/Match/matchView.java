package com.example.upn_meeting.Match;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upn_meeting.ChatActivity;
import com.example.upn_meeting.R;

public class matchView  extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mMatchId, mNombre_match, mUltimoEstadoCon, mUltimoMensaje, mRecibir, mDar, mPresup, mPerfil;
    public ImageView mNotificationDot;
    public ImageView mMatchImage;

    public matchView(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId = (TextView) itemView.findViewById(R.id.Matchid);
        mNombre_match = (TextView) itemView.findViewById(R.id.match_nombre);
        mUltimoEstadoCon = (TextView) itemView.findViewById(R.id.ultimoStado);
        mUltimoMensaje = (TextView) itemView.findViewById(R.id.lasMessage);

        mRecibir = (TextView) itemView.findViewById(R.id.recid);
        mDar = (TextView) itemView.findViewById(R.id.darid);
        mPresup= (TextView) itemView.findViewById(R.id.presuid);
        mMatchImage= (ImageView) itemView.findViewById(R.id.MatchImage);
        mPerfil = (TextView) itemView.findViewById(R.id.perfilid);
        mNotificationDot = (ImageView) itemView.findViewById(R.id.notification_dot);

    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("matchId",mMatchId.getText().toString());
        b.putString("matchNombre",mNombre_match.getText().toString());
        b.putString("ultimoMensaje",mUltimoMensaje.getText().toString());
        b.putString("ultimoEstado", mUltimoEstadoCon.getText().toString());
        b.putString("presupuesto",mPresup.getText().toString());
        b.putString("recibir", mRecibir.getText().toString());
        b.putString("dar", mDar.getText().toString());
        b.putString("perfil", mPerfil.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}

