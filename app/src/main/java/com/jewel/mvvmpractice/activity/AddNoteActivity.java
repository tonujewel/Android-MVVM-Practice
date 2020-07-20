package com.jewel.mvvmpractice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.jewel.mvvmpractice.R;

public class AddNoteActivity extends AppCompatActivity {

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
    }
}