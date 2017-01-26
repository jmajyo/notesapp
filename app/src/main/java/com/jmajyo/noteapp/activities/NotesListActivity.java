package com.jmajyo.noteapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.jmajyo.noteapp.R;
import com.jmajyo.noteapp.adapters.NoteAdapter;
import com.jmajyo.noteapp.fragments.NoteListFragment;
import com.jmajyo.noteapp.model.Note;
import com.jmajyo.noteapp.model.Notes;

import io.realm.Realm;
import io.realm.RealmResults;

public class NotesListActivity extends AppCompatActivity {

    private static final int NEW_NOTE = 69;
    Notes listOfNotes = new Notes();
    NoteListFragment noteListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_note_list);
        /*
        for (int i = 0; i < 20; i++) {
            Note note = new Note("Note " + UUID.randomUUID() + i);
            note.setText("Noticia super importante " + i);
            listOfNotes.add(note);
        }*/
        loadFromRealm();//cargo los datos de la base de datos

        //creo el adaptador.
        NoteAdapter adapter = new NoteAdapter(listOfNotes, this);
        noteListFragment.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveToRealm();
    }

    private void saveToRealm(){
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        for (int i = 0; i < listOfNotes.count(); i++) {
            Note n = listOfNotes.get(i);
            realm.copyToRealmOrUpdate(n);
        }

        realm.commitTransaction();
    }

    private void loadFromRealm(){
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Note> results = realm.where(Note.class).findAll();
        listOfNotes = new Notes();
        for(Note n: results){
            listOfNotes.add(n);
        }

        NoteAdapter adapter = new NoteAdapter(listOfNotes, this);
        noteListFragment.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_main_action_add_note) {

            Intent i = new Intent(NotesListActivity.this, noteDetailActivity.class);
            startActivityForResult(i,NEW_NOTE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_NOTE && resultCode == RESULT_OK){
            final Note newNote = (Note) data.getSerializableExtra("NewNote");
            //add to database

            Realm realm = Realm.getDefaultInstance();
            //Otra forma de hacer las transaciones en Realm
            realm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm){
                    realm.copyToRealmOrUpdate(newNote);
                }
            });
            loadFromRealm();
        }

    }
}
