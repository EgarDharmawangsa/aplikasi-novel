package com.example.projekmobileprog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class NovelRVAdapter extends RecyclerView.Adapter<NovelRVAdapter.NovelViewHolder> {

    private List<Map<String, String>> novel_list;
    private Context context;
    private OnItemClickListener listener;

    public NovelRVAdapter(Context context, List<Map<String, String>> novel_list) {
        this.context = context;
        this.novel_list = novel_list;
    }

    @NonNull
    @Override
    public NovelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.novel_item, parent, false);
        return new NovelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NovelViewHolder holder, int position) {
        Map<String, String> novel = novel_list.get(position);
        holder.idTextView.setText(novel.get("novel_id"));
        holder.nameTextView.setText(novel.get("novel_name"));
        holder.userTextView.setText("by " + novel.get("user_name"));
        holder.categoryTextView.setText("in " + novel.get("category_name"));
    }

    @Override
    public int getItemCount() {
        return novel_list.size();
    }

    public void updateData(List<Map<String, String>> novels) {
        novel_list.clear();
        novel_list.addAll(novels);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int novelId);
    }

    public class NovelViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, nameTextView, userTextView, categoryTextView;

        public NovelViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.id_novel_item_ui);
            nameTextView = itemView.findViewById(R.id.name_novel_item_ui);
            userTextView = itemView.findViewById(R.id.user_novel_item_ui);
            categoryTextView = itemView.findViewById(R.id.category_novel_item_ui);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(Integer.parseInt(novel_list.get(position).get("novel_id")));
                    }
                }
            });
        }
    }
}
