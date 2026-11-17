package com.example.notepadzjj;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 记事列表适配器：绑定 list_item.xml
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private final List<MyDatabase.Note> data = new ArrayList<>();
    private OnNoteListener listener;

    /** 点击 / 长按 的回调 */
    public interface OnNoteListener {
        void onNoteClick(MyDatabase.Note note);

        void onNoteLongClick(MyDatabase.Note note);
    }

    public void setOnNoteListener(OnNoteListener listener) {
        this.listener = listener;
    }

    /** 刷新数据 */
    public void setData(List<MyDatabase.Note> notes) {
        data.clear();
        if (notes != null) {
            data.addAll(notes);
        }
        notifyDataSetChanged();
    }

    public MyDatabase.Note getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        MyDatabase.Note note = data.get(position);
        holder.tvTitle.setText(note.title);
        holder.tvContent.setText(note.content);
        holder.tvTime.setText(note.time);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNoteClick(note);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onNoteLongClick(note);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class NoteHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final TextView tvContent;
        final TextView tvTime;

        NoteHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
