package com.example.tomislavrajic.themoviedbsearch2.utils;

import com.example.tomislavrajic.themoviedbsearch2.models.Result;

public interface MoreInfoClickListener {
    void onMoreInfoClicked(Result movieResult, String isMovie);
}