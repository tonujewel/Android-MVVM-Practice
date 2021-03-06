package com.jewel.mvvmpractice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.jewel.mvvmpractice.R;
import com.jewel.mvvmpractice.utils.AppConstant;

public class AddEditNoteActivity extends AppCompatActivity {

    private EditText edtTitle, edtDescription;
    private NumberPicker priorityNumberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);
        priorityNumberPicker = findViewById(R.id.priority_number_picker);

        priorityNumberPicker.setMinValue(1);
        priorityNumberPicker.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        // set data for update/edit note
        Intent intent = getIntent();
        if (intent.hasExtra(AppConstant.EXTRA_ID)) {
            setTitle(getString(R.string.edit_note));
            edtTitle.setText(intent.getStringExtra(AppConstant.EXTRA_TITLE));
            edtDescription.setText(intent.getStringExtra(AppConstant.EXTRA_DESCRIPTION));
            priorityNumberPicker.setValue(intent.getIntExtra(AppConstant.EXTRA_PRIORITY, 1));
        } else {
            setTitle(getString(R.string.add_note));
        }
    }


    private void saveNote() {

        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        int priority = priorityNumberPicker.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, R.string.insert_title_error + " and " + R.string.insert_description_error, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(AppConstant.EXTRA_TITLE, title);
        data.putExtra(AppConstant.EXTRA_DESCRIPTION, description);
        data.putExtra(AppConstant.EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(AppConstant.EXTRA_ID, -1);

        if (id != -1) {
          data.putExtra(AppConstant.EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.save_note:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


}