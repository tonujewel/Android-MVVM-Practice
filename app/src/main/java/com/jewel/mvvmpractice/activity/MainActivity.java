package com.jewel.mvvmpractice.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jewel.mvvmpractice.R;
import com.jewel.mvvmpractice.adapter.NoteAdapter;
import com.jewel.mvvmpractice.room.Note;
import com.jewel.mvvmpractice.utils.AppConstant;
import com.jewel.mvvmpractice.viewmodel.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private Button btnNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNote = findViewById(R.id.btn_add_noted);

        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, AppConstant.ADD_NOTE_REQUEST);

            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView_note);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
              adapter.setNotes(notes);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.ADD_NOTE_REQUEST || resultCode == RESULT_OK){
            String title = data.getStringExtra(AppConstant.EXTRA_TITLE);
            String description = data.getStringExtra(AppConstant.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AppConstant.EXTRA_PRIORITY, 1);
            Note note = new Note(title,description,priority);
            noteViewModel.insert(note);
            Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, R.string.note_note_saved, Toast.LENGTH_SHORT).show();
        }
    }
}