package com.example.betterme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.model.SetCommunityThread;

import java.util.List;

public class CommunityThreadsAdapter extends RecyclerView.Adapter<CommunityThreadsAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<SetCommunityThread> communityThreads;
    OnClickListener callback;
    public CommunityThreadsAdapter(Context ctx, List<SetCommunityThread> communityThreads, OnClickListener callback) {
        this.inflater = LayoutInflater.from(ctx);
        this.communityThreads = communityThreads;
        this.callback = callback;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_viewer_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.setCommunityThread(communityThreads.get(position));
    }

    @Override
    public int getItemCount() {
        return communityThreads.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        SetCommunityThread communityThread;
        TextView tvTitle,tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent =itemView.findViewById(R.id.tvContent);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callback != null){
                        callback.onClickItem(communityThread);
                    }
                }
            });
        }

        public SetCommunityThread getCommunityThread() {
            return communityThread;
        }

        public void setCommunityThread(SetCommunityThread communityThread) {
            this.communityThread = communityThread;
            tvTitle.setText(communityThread.getThreadTitle());
            tvContent.setText(communityThread.getThreadContent());
        }
    }
    public interface OnClickListener {
        void onClickItem(SetCommunityThread comment);
    }
}


