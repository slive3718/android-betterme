package com.example.betterme;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.model.SetComment;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    SetComment comment;
    View llButtonContainer;
    ImageView ivEdit, ivDelete;
    TextView tvName, tvDate, tvMessage;
    Listener callback;

    public CommentViewHolder(@NonNull View itemView, Listener callback) {
        super(itemView);
        this.callback = callback;
        llButtonContainer = itemView.findViewById(R.id.llButtonContainer);
        ivEdit = itemView.findViewById(R.id.ivEdit);
        ivDelete = itemView.findViewById(R.id.ivDelete);
        tvName = itemView.findViewById(R.id.tvName);
        tvDate = itemView.findViewById(R.id.tvDate);
        tvMessage = itemView.findViewById(R.id.tvMessage);
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClickEdit(comment);
                }
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClickDelete(comment);
                }
            }
        });
    }

    public SetComment getComment() {
        return comment;
    }

    public void setComment(SetComment comment) {
        this.comment = comment;
        tvName.setText(comment.getUserInfo().getFullName());
        tvMessage.setText(comment.getComment());
        tvDate.setText(DateHelper.getFormattedDate(comment.getDate()));
        llButtonContainer.setVisibility(comment.getUser_id().equalsIgnoreCase(AppApplication.getInstance().getCurrentUser().getId()) ? View.VISIBLE : View.GONE);
    }

    public interface Listener {
        void onClickEdit(SetComment comment);

        void onClickDelete(SetComment comment);
    }
}
