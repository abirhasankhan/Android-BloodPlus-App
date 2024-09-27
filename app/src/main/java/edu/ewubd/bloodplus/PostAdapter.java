package edu.ewubd.bloodplus;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.ewubd.bloodplus.databinding.ActivityLoginBinding;
import edu.ewubd.bloodplus.databinding.ActivitySignUpBinding;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context context;
    List<Post> mData;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    String name;

    public PostAdapter(Context context, List<Post> mData) {
        this.context = context;
        this.mData = mData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(context).inflate(R.layout.row_post, parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //Post model = mData.get(position);
        holder.tvName.setText(mData.get(position).getUserName());
        holder.tvTitle.setText(mData.get(position).getTitle());
        Glide.with(context).load(mData.get(position).getUserPhoto()).apply(RequestOptions.circleCropTransform()).into(holder.imgUserPhoto);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvTitle;
        ImageView imgUserPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.row_userPostName);
            tvTitle = itemView.findViewById(R.id.row_postTitle);
            imgUserPhoto = itemView.findViewById(R.id.row_postUserImage);

            itemView.setOnClickListener(v -> funGOPostPage());

            itemView.setOnLongClickListener(v -> funOption());

        }
        private boolean funOption() {
            Toast.makeText(context,"Long click",Toast.LENGTH_SHORT).show();
            return true;
        }

        private void funGOPostPage() {
            Intent goPostPage = new Intent(context, PostDetail.class);
            int position = getAdapterPosition();

           //  date

            goPostPage.putExtra("title", mData.get(position).getTitle());
            goPostPage.putExtra("name", mData.get(position).getUserName());
            goPostPage.putExtra("userImage", mData.get(position).getUserPhoto());
            goPostPage.putExtra("blood", mData.get(position).getBloodType());
            goPostPage.putExtra("phone", mData.get(position).getPhone());
            goPostPage.putExtra("date", mData.get(position).getDate());
            goPostPage.putExtra("country", mData.get(position).getCountry());
            goPostPage.putExtra("city", mData.get(position).getCity());
            goPostPage.putExtra("desc", mData.get(position).getDesc());

            goPostPage.putExtra("postKey", mData.get(position).getPostKey());

            long timeStamp = (long) mData.get(position).getTimeStamp();
            goPostPage.putExtra("postDate",timeStamp);

            context.startActivity(goPostPage);



        }
    }

}
