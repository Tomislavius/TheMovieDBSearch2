package com.example.tomislavrajic.themoviedbsearch2;

import com.example.tomislavrajic.themoviedbsearch2.models.Result;

public interface MoreInfoClickListener {
    void onMoreInfoClicked(Result movieResult, boolean isMovie);
}
