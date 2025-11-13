package com.faiyaz.faiyazzchatapp.Entity;

import static com.faiyaz.faiyazzchatapp.ChatWindow.receiverImg;
import static com.faiyaz.faiyazzchatapp.ChatWindow.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faiyaz.faiyazzchatapp.ChatWindow;
import com.faiyaz.faiyazzchatapp.R;
import com.faiyaz.faiyazzchatapp.msgClass;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;

    List<msgClass> msgList;

    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2 ;

    public MessageAdapter(Context context, List<msgClass> msgList) {
        this.context = context;
        this.msgList = msgList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      if(viewType == ITEM_SEND){

          View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
          return new senderViewHolder(view);

      }else {

          View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
          return new receiverViewHolder(view);
      }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgClass messages = msgList.get(position);

        if(holder.getClass()==senderViewHolder.class){
            senderViewHolder viewHolder = (senderViewHolder) holder;

            viewHolder.msgTxt.setText(messages.getMsg());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);

        }else {


            receiverViewHolder viewHolder = (receiverViewHolder) holder;

            viewHolder.msgTxt.setText(messages.getMsg());

            Picasso.get().load(receiverImg).into(viewHolder.circleImageView);
        }


    }

    @Override
    public int getItemCount() {
       return msgList.size();
    }


    @Override
    public int getItemViewType(int position) {
        msgClass msg = msgList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(msg.getSenderId())){
            return ITEM_SEND;
        }else{

            return ITEM_RECEIVE;

        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView ;
        TextView msgTxt ;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgTxt = itemView.findViewById(R.id.msgsendertyp);
        }
    }
    class receiverViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView ;
        TextView msgTxt ;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgTxt = itemView.findViewById(R.id.recivertextset);
        }
    }
}
