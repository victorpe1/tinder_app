package com.example.upn_meeting.Tarjeta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.upn_meeting.R;

import java.util.List;

public class listaTarjeta extends ArrayAdapter<tarjeta> {
    Context context;


    public listaTarjeta(Context context, int id, List<tarjeta> items){
        super(context, id, items);
    }

    public View getView(int pos, View convertView, ViewGroup parent)
    {
        tarjeta tarjeta_item = getItem(pos);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);

        }

        TextView nombre = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView presu = (TextView) convertView.findViewById(R.id.presu);
        ImageView nImage = (ImageView) convertView.findViewById(R.id.necesitoImage);
        ImageView nDarImage = (ImageView) convertView.findViewById(R.id.giveImage);

        nombre.setText(tarjeta_item.getNombre());
        presu.setText(tarjeta_item.getFavoritos());


        //need Image
        if (tarjeta_item.getRecibo().equals("Netflix"))
            nImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.netflix));
        else if (tarjeta_item.getRecibo().equals("Hulu"))
            nImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.hulu));
        else if (tarjeta_item.getRecibo().equals("Vudu"))
            nImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.vudu));
        else if (tarjeta_item.getRecibo().equals("HBO Now"))
            nImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.hbo));
        else if (tarjeta_item.getRecibo().equals("Youtube Originals"))
            nImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.youtube));
        else
            nImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.none));


        //Give Image
        if (tarjeta_item.getEntrega().equals("Netflix"))
            nDarImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.netflix));
        else if (tarjeta_item.getEntrega().equals("Amazon Prime"))
            nDarImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.amazon));
        else if (tarjeta_item.getEntrega().equals("Hulu"))
            nDarImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.hulu));
        else if (tarjeta_item.getEntrega().equals("Vudu"))
            nDarImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.vudu));
        else if (tarjeta_item.getEntrega().equals("HBO Now"))
            nDarImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.hbo));
        else if (tarjeta_item.getEntrega().equals("Youtube Originals"))
            nDarImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.youtube));
        else
            nDarImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.none));

        switch (tarjeta_item.getImagen_perfil()){
            case "default" :
                Glide.with(convertView.getContext()).load(R.drawable.profile).into(image);
                break;
            default:
                Glide.with(context).clear(image);
                Glide.with(convertView.getContext()).load(tarjeta_item.getImagen_perfil()).into(image);
                break;
        }
        return convertView;
    }


}
