package com.example.note.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.note.MainActivity;
import com.example.note.adapter.NoteOverviewAdapter;
import com.example.note.R;
import com.example.note.controller.NoteManager;
import com.example.note.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NoteOverviewFragment extends Fragment {

    private NoteOverviewAdapter mAdapter;
    private List<Note> mAllNotes;
    private NoteManager mNoteManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.overview_fragment, container, false);
        Context context = inflater.getContext();

        mNoteManager = NoteManager.getInstance();
        mAllNotes = mNoteManager.getNotes();

        ListView listView = view.findViewById(R.id.overview_list);
        mAdapter = new NoteOverviewAdapter(context, R.layout.overview_list_item, mAllNotes);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("note_id", mAllNotes.get(i).getId());
                NavHostFragment.findNavController(NoteOverviewFragment.this)
                        .navigate(R.id.action_overview_to_detail_fragment, bundle);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                final Note note = mAllNotes.get(i);
                bundle.putInt("note_id", note.getId());
                new AlertDialog.Builder(NoteOverviewFragment.super.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete note " +note.getTitle() + " ?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mNoteManager.deleteNote(note);
                                mAllNotes.remove(note);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
                return true;
            }
        });

        FloatingActionButton new_note_btn = view.findViewById(R.id.overview_new_note_btn);
        new_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for Future implementation of NOTE_002C Add new note
                /*Bundle bundle = new Bundle();
                bundle.putInt("note_id", -1);
                NavHostFragment.findNavController(NoteOverviewFragment.this)
                        .navigate(R.id.action_overview_to_detail_fragment, bundle);*/

                //only for testing
                Note note = new Note(mNoteManager.getNextFreeId(), "Titel", "Content");
                mNoteManager.addNote(note);
                mAllNotes = mNoteManager.getNotes();
                mAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if(visible && mAdapter != null) {
            mAllNotes = mNoteManager.getNotes();
            mAdapter.notifyDataSetChanged();
        }
    }
}
