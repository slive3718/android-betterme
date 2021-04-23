package com.example.betterme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.betterme.model.SetImages;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<SetImages> images;
    Context context;

    public ImagesAdapter(Context ctx, List<SetImages> images) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.images = images;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_image_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
//            Picasso.get().load(images.get(position).getImageURL()).placeholder(R.drawable.placeholder).into(holder.image);
            String url = images.get(position).getImageURL();
            holder.setImages = images.get(position);
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.image);
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public List<SetImages> getImages() {
        return images;
    }

    public void setImages(List<SetImages> images) {
        this.images = images;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        SetImages setImages;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageActivity.class);
                    intent.putExtra("image", setImages.getImageURL());
                    context.startActivity(intent);
                }
            });
        }
    }
}
