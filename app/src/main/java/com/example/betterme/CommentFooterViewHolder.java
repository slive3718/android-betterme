package com.example.betterme;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.model.SetComment;

public class CommentFooterViewHolder extends RecyclerView.ViewHolder {
    SetComment comment; //TODO: replace this to commment Object
    View llButtonContainer;
    EditText etMessage;
    TextView btnPost, btnCancel;
    Listener callback;
    public CommentFooterViewHolder(@NonNull View itemView,Listener callback) {
        super(itemView);
        this.callback = callback;
        llButtonContainer = itemView.findViewById(R.id.llButtonContainer);
        etMessage = itemView.findViewById(R.id.etMessage);
        btnPost = itemView.findViewById(R.id.btnPost);
        btnCancel = itemView.findViewById(R.id.btnCancel);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null){
                    if(!TextUtils.isEmpty(etMessage.getText().toString().trim())) {
                        callback.onClickPost(comment, etMessage.getText().toString().trim());
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = null;
                etMessage.setText("");
                btnPost.setText("Post Comment");
            }
        });
    }

    public SetComment getComment() {
        return comment;
    }

    public void setComment(SetComment comment) {
        this.comment = comment;
        if (comment != null) {
            etMessage.setText(comment.getComment());
            btnPost.setText("Update Comment");
        } else {
            etMessage.setText("");
            btnPost.setText("Post Comment");
        }
    }

    public interface Listener{
        void onClickPost(SetComment comment,String message);
        void onClickCancel();
    }
}
