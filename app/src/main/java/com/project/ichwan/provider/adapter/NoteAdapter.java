package com.project.ichwan.provider.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ichwan.provider.CustomOnItemClickListener;
import com.project.ichwan.provider.FormAddUpdateActivity;
import com.project.ichwan.provider.R;
import com.project.ichwan.provider.entity.Note;

import java.util.LinkedList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private LinkedList<Note> listNote;
    private Activity activity;

    public NoteAdapter(Activity activity){
        this.activity = activity;
    }

    public LinkedList<Note> getListNote() {
        return listNote;
    }

    public void setListNote(LinkedList<Note> listNote) {
        this.listNote = listNote;
    }


    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.tvTitle.setText(getListNote().get(position).getTitle());
        holder.tvDescription.setText(getListNote().get(position).getDescription());
        holder.tvDate.setText(getListNote().get(position).getDate());
        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, FormAddUpdateActivity.class);
                intent.putExtra(FormAddUpdateActivity.EXTRA_POSITION,position);
                intent.putExtra(FormAddUpdateActivity.EXTRA_NOTE,getListNote().get(position));
                activity.startActivityForResult(intent,FormAddUpdateActivity.REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return getListNote().size();
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDescription, tvDate;
        CardView cvNote;

        public NoteViewHolder(View itemView){
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_item_title);
            tvDescription = (TextView)itemView.findViewById(R.id.tv_item_description);
            tvDate = (TextView)itemView.findViewById(R.id.tv_item_date);
            cvNote = (CardView)itemView.findViewById(R.id.cv_item_note);
        }
    }
}
