package com.example.jimmy.solitaire;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;import com.example.jimmy.solitaire.R;

/**
 * Created by Amritpal on 4/12/2016.
 */
public class ScoreActivityFragment extends Fragment {

    public ScoreActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score, container, false);
    }
}
