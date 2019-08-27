package com.example.mydata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class imgAdapter extends RecyclerView.Adapter<imgAdapter.imgHolder> {
    public class imgHolder extends RecyclerView.ViewHolder {
        public TextView nam;
        public ImageView imgview;

        public imgHolder(View itemView) {
            super(itemView);
            nam = itemView.findViewById(R.id.name);
            imgview = itemView.findViewById(R.id.imgView);

        }
    }

    public Context mcon;
    public List<Upload> muploads;

    public imgAdapter(Context context, List<Upload> ups) {
        mcon = context;
        muploads = ups;
    }

    @Override
    public imgHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcon).inflate(R.layout.imgview, parent, false);
        imgHolder imh = new imgHolder(v);
        return (imh);
    }

    @Override
    public void onBindViewHolder( imgHolder holder, int position) {
        Upload upcurrent = muploads.get(position);
        holder.nam.setText(upcurrent.getName());
        Picasso.with(mcon)
                .load(upcurrent.getImgurl())
                .fit()
                .centerInside()
                .into(holder.imgview);

    }

    @Override
    public int getItemCount() {
        return muploads.size();
    }


}
