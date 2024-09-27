package edu.ewubd.bloodplus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    public UserAdapter(Context context, List<User> uData) {
        this.context = context;
        this.uData = uData;
    }

    Context context;
    List<User> uData;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        User user = uData.get(position);

    }

    @Override
    public int getItemCount() {
        return uData.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{


        public viewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
