package com.nyc.googlenowfeed;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {
    private EditText title, description;


    public NoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        title = container.findViewById(R.id.title_note);
        description = container.findViewById(R.id.description_note);

        return inflater.inflate(R.layout.fragment_note, container, false);
    }

}
