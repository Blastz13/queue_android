package com.example.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QueueTicketRoomAdapter extends RecyclerView.Adapter<QueueTicketRoomAdapter.QueueTicketViewHolder> {

    Context context;
    List<QueueTicketRoomModel> listQueueTicketRoomModel;

    public QueueTicketRoomAdapter(Context context, List<QueueTicketRoomModel> listQueueTicketRoomModel) {
        this.context = context;
        this.listQueueTicketRoomModel = listQueueTicketRoomModel;
    }

    @NonNull
    @Override
    public QueueTicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View queueRoomItem = LayoutInflater.from(context).inflate(R.layout.queue_ticket_item, parent, false);
        return new QueueTicketRoomAdapter.QueueTicketViewHolder(queueRoomItem);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueTicketViewHolder holder, int position) {
        holder.queueTicketRoomTitle.setText(listQueueTicketRoomModel.get(position).getUsername());
        holder.queueTicketStatus.setText(listQueueTicketRoomModel.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return listQueueTicketRoomModel.size();
    }

    public static final class QueueTicketViewHolder extends RecyclerView.ViewHolder {
        TextView queueTicketRoomTitle;
        TextView queueTicketStatus;

        public QueueTicketViewHolder(@NonNull View itemView) {
            super(itemView);

            queueTicketRoomTitle = itemView.findViewById(R.id.queueTicketRoomTitle);
            queueTicketStatus = itemView.findViewById(R.id.queueTicketStatus);
        }
    }
}
