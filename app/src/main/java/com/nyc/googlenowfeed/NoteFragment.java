package com.nyc.googlenowfeed;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {
    private EditText title, description;
    private Button save;
    private static final String SHARED_PREFS_KEY = "NotesSharedPrefs";
    private SharedPreferences saveNote;
    private Spinner savedNotes;
    private List<String> noteTitles;


    public NoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        saveNote = getContext().getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        savedNotes = rootView.findViewById(R.id.get_notes);
        title = rootView.findViewById(R.id.title_note);
        description = rootView.findViewById(R.id.description_note);
        save = rootView.findViewById(R.id.save_note);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = saveNote.edit();
                editor.putString(title.getText().toString(),title.getText().toString());
                editor.putString(title.getText().toString() + "note", description.getText().toString());
                editor.putBoolean("BooleanKey", true);
                editor.commit();
                title.setText("");
                description.setText("");
            }
        });

//        load.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (saveNote.getBoolean("BooleanKey",false)) {
//                    title.setText(saveNote.getString("title",null));
//                    description.setText(saveNote.getString("note", null));
//                }
//            }
//        });

        return rootView;
    }

}
