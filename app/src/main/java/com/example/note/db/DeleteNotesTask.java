package com.example.note.db;

import android.os.AsyncTask;

import com.example.note.dao.NoteDAO;
import com.example.note.model.Note;

public class DeleteNotesTask extends AsyncTask<Note, Void, Void> {
    @Override
    protected Void doInBackground(Note... notes) {
        AppDatabase db = DatabaseHelper.getInstance().getDb();
        NoteDAO noteDAO = db.noteDAO();
        noteDAO.deleteNote(notes[0]);
        return null;
    }
}
