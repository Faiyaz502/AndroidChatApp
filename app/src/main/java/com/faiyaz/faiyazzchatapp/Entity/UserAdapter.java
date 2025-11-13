package com.faiyaz.faiyazzchatapp.Entity;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faiyaz.faiyazzchatapp.ChatWindow;
import com.faiyaz.faiyazzchatapp.MainActivity;
import com.faiyaz.faiyazzchatapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {

    MainActivity mainActivity;
    List<Users> usersList;


    public UserAdapter(MainActivity mainActivity, List<Users> usersList) {

        this.mainActivity = mainActivity;
        this.usersList = usersList;

    }

    @NonNull
    @Override
    public UserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewholder holder, int position) {
        Users users = usersList.get(position);

        holder.userName.setText(users.getUsername());
        holder.userStatus.setText(users.getStatus());


        Picasso.get().load(users.getProfileImg()).into(holder.userimg);


        holder.itemView.setOnClickListener(v->{

            Intent intent = new Intent(mainActivity, ChatWindow.class);
            intent.putExtra("naam",users.getUsername());
            intent.putExtra("receiverImg",users.getProfileImg());
            intent.putExtra("uid",users.getUserId());

            mainActivity.startActivity(intent);



        });



    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        CircleImageView userimg ;
        TextView userName , userStatus ;



        public viewholder(@NonNull View itemView) {
            super(itemView);

            userimg = itemView.findViewById(R.id.userImg);
            userName = itemView.findViewById(R.id.userName);
            userStatus= itemView.findViewById(R.id.userStatus);

        }
    }
}
