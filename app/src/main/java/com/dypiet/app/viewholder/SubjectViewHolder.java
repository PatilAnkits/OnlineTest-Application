package com.dypiet.app.viewholder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.dypiet.app.R;

public class SubjectViewHolder extends RecyclerView.ViewHolder {
    Button button;
    public SubjectViewHolder(@NonNull View itemView) {
        super (itemView);
        button = itemView.findViewById (R.id.sub_button);
    }

    public void setText(String text){
        button.setText (text);
    }

    public void setOnClick(View.OnClickListener onClickListener){
        button.setOnClickListener (onClickListener);
    }
}
