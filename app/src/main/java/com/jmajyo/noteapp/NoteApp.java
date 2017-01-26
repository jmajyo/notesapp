package com.jmajyo.noteapp;

import android.app.Application;

import io.realm.Realm;

public class NoteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //init Realm
        Realm.init(getApplicationContext());
    }
}
