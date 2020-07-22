package com.jewel.mvvmpractice.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, R.string.note_deleted, Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AppConstant.EXTRA_ID, note.getId());
                intent.putExtra(AppConstant.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AppConstant.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AppConstant.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, AppConstant.EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            String title = data.getStringExtra(AppConstant.EXTRA_TITLE);
            String description = data.getStringExtra(AppConstant.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AppConstant.EXTRA_PRIORITY, 1);
            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);
            Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show();

        } else if (requestCode == AppConstant.EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = getIntent().getIntExtra(AppConstant.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, R.string.note_cant_update, Toast.LENGTH_SHORT).show();
            }
            String title = data.getStringExtra(AppConstant.EXTRA_TITLE);
            String description = data.getStringExtra(AppConstant.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AppConstant.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, R.string.note_updated, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.note_note_saved, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, R.string.all_notes_deleted, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}