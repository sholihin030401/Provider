package com.project.ichwan.provider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.project.ichwan.provider.adapter.NoteAdapter;
import com.project.ichwan.provider.db.NoteHelper;
import com.project.ichwan.provider.entity.Note;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.project.ichwan.provider.FormAddUpdateActivity.REQUEST_UPDATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvNotes;
    ProgressBar progressBar;
    FloatingActionButton fabAdd;

    private LinkedList<Note> list;
    private NoteAdapter adapter;
    private NoteHelper noteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Notes");
        rvNotes = (RecyclerView)findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        fabAdd = (FloatingActionButton)findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        noteHelper.open();

        list = new LinkedList<>();

        adapter = new NoteAdapter(this);
        adapter.setListNote(list);
        rvNotes.setAdapter(adapter);

        new LoadNoteAsync().execute();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add){
            Intent intent = new Intent(MainActivity.this,FormAddUpdateActivity.class);
            startActivityForResult(intent,FormAddUpdateActivity.REQUEST_ADD);
        }
    }
    private class LoadNoteAsync extends AsyncTask<Void, Void, ArrayList<Note>> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            if(list.size() > 0){
                list.clear();
            }
        }
        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            return noteHelper.query();
        }
        @Override
        protected void onPostExecute(ArrayList<Note> notes){
            super.onPostExecute(notes);
            progressBar.setVisibility(View.GONE);

            list.addAll(notes);
            adapter.setListNote(list);
            adapter.notifyDataSetChanged();

            if(list.size() == 0){
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == FormAddUpdateActivity.REQUEST_ADD){
            if (resultCode == FormAddUpdateActivity.RESULT_ADD){
                new LoadNoteAsync().execute();
                showSnackbarMessage("Satu item berhasil ditambahkan");
                //rvNotes.getLayoutManager().smoothScrollToPosition(rvNotes,new RecyclerView.State(),0);
            }
        }
        else if(requestCode == REQUEST_UPDATE){
            if (resultCode == FormAddUpdateActivity.RESULT_UPDATE){
                new LoadNoteAsync().execute();
                showSnackbarMessage("Satu item berhasil diubah");
                //int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION,0);
                //rvNotes.getLayoutManager().smoothScrollToPosition(rvNotes,new RecyclerView.State(),position);
            }
            else if(resultCode == FormAddUpdateActivity.RESULT_DELETE){
                int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION,0);
                list.remove(position);
                adapter.setListNote(list);
                adapter.notifyDataSetChanged();
                showSnackbarMessage("Satu item berhasil dihapus");
            }
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (noteHelper != null){
            noteHelper.close();
        }
    }
    private void showSnackbarMessage(String message){
        Snackbar.make(rvNotes,message,Snackbar.LENGTH_SHORT).show();
    }
}
