package com.example.betterme;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.betterme.model.SetComment;
import com.example.betterme.model.SetCommunityThread;

public class CommunityThreadDetailsAdapter extends RecyclerView.Adapter {
    static final int VIEW_TYPE_HEADER = 0;
    static final int VIEW_TYPE_COMMENT = 1;
    static final int VIEW_TYPE_FOOTER = 2;

    LayoutInflater inflater;
    SetCommunityThread communityThread;
    SetComment editingComment;
    OnClickListener onClickListener;
    Context context;

    public CommunityThreadDetailsAdapter(Context ctx, SetCommunityThread diet, OnClickListener onClickListener) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.communityThread = diet;
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.custom_community_thread_header_layout, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == VIEW_TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.custom_write_comment_footer_layout, parent, false);
            return new CommentFooterViewHolder(view, new CommentFooterViewHolder.Listener() {

                @Override
                public void onClickPost(SetComment comment, String message) {
                    if (onClickListener != null) {
                        onClickListener.onClickComment(comment, message);
                    }
                }

                @Override
                public void onClickCancel() {

                }
            });
        } else {
            View view = inflater.inflate(R.layout.custom_comment_layout, parent, false);
            return new CommentViewHolder(view, new CommentViewHolder.Listener() {
                @Override
                public void onClickEdit(SetComment comment) {
                    editingComment = comment;
                    notifyItemChanged(getItemCount() - 1);
                }

                @Override
                public void onClickDelete(SetComment comment) {
                    if (onClickListener != null) {
                        onClickListener.onClickDeleteComment(comment);
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).setCommunityThread(communityThread);
        } else if (holder instanceof CommentViewHolder) {
            ((CommentViewHolder) holder).setComment(communityThread.getComments().get(position - 1));
        } else if (holder instanceof CommentFooterViewHolder) {
            ((CommentFooterViewHolder) holder).setComment(editingComment);
        }
    }

    private String appendNextLineIfNeeded(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str + " \n";
        }
        return str;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : position == getItemCount() - 1 ? VIEW_TYPE_FOOTER : VIEW_TYPE_COMMENT;
    }

    @Override
    public int getItemCount() {
        return communityThread.getComments().size() + 2; // 1 header 1 footer (write a comment)

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        SetCommunityThread communityThread = null;
        TextView tvTitle, tvContent, tvName, tvDate, tvLike, tvComment;
        ImageView avatar;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvComment = itemView.findViewById(R.id.tvComment);
            avatar = itemView.findViewById(R.id.ivAvatar);

            tvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClickLike();
                    }
                }
            });
        }

        public SetCommunityThread getCommunityThread() {
            return communityThread;
        }

        public void setCommunityThread(SetCommunityThread item) {
            this.communityThread = item;
            tvName.setText(item.getUserInfo().getFullName()); //TODO: set actual Posted by Name
            tvDate.setText(DateHelper.getFormattedDateTime(item.getThreadDate()));
            tvTitle.setText(item.getThreadTitle());
            tvContent.setText(item.getThreadContent());
            tvComment.setText("Comments " + (item.getComments().size() > 0 ? item.getComments().size() : ""));//TODO: set actual Like Count
            try {
                Glide.with(context)
                        .load(item.getUserInfo().getImageURL())
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(avatar);
            } catch (Exception e) {

            }
        }
    }

    public interface OnClickListener {
        void onClickComment(SetComment comment, String message);

        void onClickLike();

        void onClickDeleteComment(SetComment comment);
    }
}
