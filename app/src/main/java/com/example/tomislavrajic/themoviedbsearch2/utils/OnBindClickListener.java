package com.example.tomislavrajic.themoviedbsearch2.utils;

import com.example.tomislavrajic.themoviedbsearch2.models.Result;

public interface OnBindClickListener {

    void onLoadMoreClicked();

    void onCheckedChanged(boolean isChecked, Result result);
}
