package com.example.tomislavrajic.themoviedbsearch2.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomislavrajic.themoviedbsearch2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVShowsOnTVFragment extends Fragment {


    public TVShowsOnTVFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshows_on_tv, container, false);
    }

}