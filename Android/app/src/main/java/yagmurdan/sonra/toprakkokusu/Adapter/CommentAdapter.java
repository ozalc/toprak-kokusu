package yagmurdan.sonra.toprakkokusu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import yagmurdan.sonra.toprakkokusu.Model.Comment;
import yagmurdan.sonra.toprakkokusu.Model.User;
import yagmurdan.sonra.toprakkokusu.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private List<Comment> CommentList;

    private FirebaseUser firebaseUser;

    public CommentAdapter(Context mContext, List<Comment> CommentList) {
        this.mContext = mContext;
        this.CommentList = CommentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Comment comment=CommentList.get(position);
        holder.comment.setText(comment.getComment());

        if (comment.getPublisher()!=null)
            getUserInfo(holder.profileImage, holder.name, comment.getPublisher());

    }

    @Override
    public int getItemCount() {
        return CommentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView profileImage;
        public TextView name,comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.commentItemImageView);
            name=itemView.findViewById(R.id.commentItemName);
            comment=itemView.findViewById(R.id.commentItemComment);
        }
    }
    public void getUserInfo (ImageView imageView, TextView name, String publisherid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(publisherid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(mContext).load(snapshot.child("image").getValue()).into(imageView);
                name.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
