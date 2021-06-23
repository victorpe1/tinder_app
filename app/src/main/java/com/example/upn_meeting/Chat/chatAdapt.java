package com.example.upn_meeting.Chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upn_meeting.R;

import java.util.List;

public class chatAdapt extends RecyclerView.Adapter<chatView>{

    private List<chat> chatList;
    private Context context;

    public chatAdapt(List<chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public chatView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        chatView rcv = new chatView(layoutView);

        return rcv;

    }


    @Override
    public void onBindViewHolder(@NonNull chatView holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMensaje());

        if (chatList.get(position).getAct_usuario()){
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(20);
            shape.setCornerRadii(new float[] {25,25,3,25,25,25,25,25});
            shape.setColor(Color.parseColor("#5fc9f8"));
            holder.mContainer.setGravity(Gravity.END);
            holder.mMessage.setBackground(shape);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else{
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(20);
            shape.setCornerRadii(new float[] {25,25,3,25,25,25,25,25});
            shape.setColor(Color.parseColor("#53d769"));
            holder.mContainer.setGravity(Gravity.START);
            holder.mMessage.setBackground(shape);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
        }

    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}
