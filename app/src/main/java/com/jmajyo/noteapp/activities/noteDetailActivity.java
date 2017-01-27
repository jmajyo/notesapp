package com.jmajyo.noteapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jmajyo.noteapp.R;
import com.jmajyo.noteapp.fragments.NoteDetailFragment;
import com.jmajyo.noteapp.model.Note;

import java.io.Serializable;

public class noteDetailActivity extends AppCompatActivity {

    Button saveButton;
    NoteDetailFragment detailFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        //Para poder acceder al fragmento necesito el fragment manager. OJO!!! utilizar el SupportManagerFra
        detailFragment = (NoteDetailFragment) getSupportFragmentManager().findFragmentById(R.id.activity_note_detail___detail_fragment);

        saveButton = (Button) findViewById(R.id.activity_note_detail___close_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                Note note = detailFragment.getNote();
                returnIntent.putExtra("NewNote",(Serializable) note);

                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
