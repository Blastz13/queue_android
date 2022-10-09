package com.example.shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QueueRoomAdapter extends RecyclerView.Adapter<QueueRoomAdapter.QueueRoomViewHolder> {

    Context context;
    List<QueueRoomModel> listQueueRoomModel;

    public QueueRoomAdapter(Context context, List<QueueRoomModel> listQueueRoomModel) {
        this.context = context;
        this.listQueueRoomModel = listQueueRoomModel;
    }

    @NonNull
    @Override
    public QueueRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View queueRoomItem = LayoutInflater.from(context).inflate(R.layout.queue_room_item, parent, false);
        return new QueueRoomAdapter.QueueRoomViewHolder(queueRoomItem);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueRoomViewHolder holder, int position) {
        holder.queueRoomTitle.setText(listQueueRoomModel.get(position).getTitle());

        holder.openQueueRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("idQueueRoom", listQueueRoomModel.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listQueueRoomModel.size();
    }

    public static final class QueueRoomViewHolder extends RecyclerView.ViewHolder {
        TextView queueRoomTitle;
        Button openQueueRoomBtn;

        public QueueRoomViewHolder(@NonNull View itemView) {
            super(itemView);

            queueRoomTitle = itemView.findViewById(R.id.queueTicketRoomTitle);
            openQueueRoomBtn = itemView.findViewById(R.id.openQueueRoomBtn);
        }
    }
}
