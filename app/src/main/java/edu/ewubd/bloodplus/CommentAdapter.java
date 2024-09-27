package edu.ewubd.bloodplus;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> cData;

    public CommentAdapter(Context context, List<Comment> cData) {
        this.context = context;
        this.cData = cData;
    }

    @NonNull
    @Override

    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.row_comment, parent,false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        Glide.with(context).load(cData.get(position).getUserImg()).apply(RequestOptions.circleCropTransform()).into(holder.imgUser);
        holder.nameUser.setText(cData.get(position).getUserName());
        holder.commentUser.setText(cData.get(position).getComment());
        holder.tvDate.setText(timeStampToString((Long) cData.get(position).getTimeStamp()));

    }

    @Override
    public int getItemCount() {
        return cData.size();
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder{

        ImageView imgUser;
        TextView nameUser, commentUser, tvDate ;

        public CommentViewHolder(@NonNull View itemView) {

            super(itemView);

            imgUser = itemView.findViewById(R.id.commentUserImg);
            nameUser = itemView.findViewById(R.id.commentUserName);
            commentUser = itemView.findViewById(R.id.userComment);
            tvDate = itemView.findViewById(R.id.commentDate);

        }
    }

    private String timeStampToString(long time){

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm",calendar).toString();

        return date;

    }
}
