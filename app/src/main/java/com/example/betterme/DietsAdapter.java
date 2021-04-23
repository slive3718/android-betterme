package com.example.betterme;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.betterme.model.SetDiets;

import java.util.ArrayList;
import java.util.List;

public class DietsAdapter extends RecyclerView.Adapter<DietsAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<SetDiets> diets;
    OnClickListener onClickListener;
    Context context;

    public DietsAdapter(Context ctx, List<SetDiets> diets, OnClickListener onClickListener) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.diets = diets;
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDiets(diets.get(position));
    }

    private String appendNextLineIfNeeded(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str + " \n";
        }
        return str;
    }

    @Override
    public int getItemCount() {
        return diets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SetDiets diets = null;
        RecyclerView recyclerView;
        TextView tvTitle, tvContent, tvKey, tvValue, tvName, tvDate, tvLike, tvComment;
        ImageView avatar;
        View containerLike, containerComment;
        ImagesAdapter imagesAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvKey = itemView.findViewById(R.id.tvKey);
            tvValue = itemView.findViewById(R.id.tvValue);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvComment = itemView.findViewById(R.id.tvComment);
            avatar = itemView.findViewById(R.id.ivAvatar);
            containerLike = itemView.findViewById(R.id.containerLike);
            containerComment = itemView.findViewById(R.id.containerComment);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            imagesAdapter = new ImagesAdapter(context, new ArrayList<>());
            recyclerView.setAdapter(imagesAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            containerLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClickLike(ViewHolder.this);
                    }
                }
            });
            containerComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClickComment(ViewHolder.this);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClickHolder(ViewHolder.this);
                    }
                }
            });
        }

        public SetDiets getDiets() {
            return diets;
        }

        public void setDiets(SetDiets item) {
            this.diets = item;
            tvName.setText(item.getUserInfo().getFullName()); //TODO: set actual Posted by Name
            tvDate.setText(DateHelper.getFormattedDateTime(item.getDate_posted()));
            tvTitle.setText(item.getTitle());
            tvContent.setText(item.getContent());
            tvLike.setText("Like " + (item.getGetLikeCount() > 0 ? item.getGetLikeCount() : ""));
            tvComment.setText("Comments " + (item.getComments().size() > 0 ? item.getComments().size() : ""));//TODO: set actual Like Count

            if(item.getLike_status().equalsIgnoreCase("0")){
                tvLike.setTextColor(context.getResources().getColor(R.color.black));
                tvLike.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_like),null,null,null);
            }else{
                tvLike.setTextColor(context.getResources().getColor(R.color.blue));
                tvLike.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_like_blue),null,null,null);
            }

            String key = "";
            String value = "";

            if (!TextUtils.isEmpty(item.getType_of_diet())) {
                key += "Type of Diet:";
                value += item.getType_of_diet();
            }
            if (!TextUtils.isEmpty(item.getRoutine_format())) {
                key = appendNextLineIfNeeded(key);
                value = appendNextLineIfNeeded(value);
                key += "Routine:";
                value += item.getRoutine_count() + " " + item.getRoutine_format();
            }
            if (!TextUtils.isEmpty(item.getTarget_audience())) {
                key = appendNextLineIfNeeded(key);
                value = appendNextLineIfNeeded(value);
                key += "Suitable For:";
                value += item.getTarget_audience();
            }
            tvKey.setText(key);
            tvValue.setText(value);
            try {
//                Picasso.get().load(item.getPost_image_url()).into(avatar);
                Glide.with(context)
                        .load(item.getUserInfo().getImageURL())
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(avatar);
            } catch (Exception e) {

            }
            imagesAdapter.setImages(diets.getImages());
            recyclerView.setVisibility(diets.getImages().size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    public interface OnClickListener {
        void onClickComment(ViewHolder viewHolder);

        void onClickLike(ViewHolder viewHolder);

        void onClickHolder(ViewHolder viewHolder);
    }
}
