package com.example.tomislavrajic.themoviedbsearch2.utils;

import android.support.annotation.NonNull;

import com.example.tomislavrajic.themoviedbsearch2.R;
import com.example.tomislavrajic.themoviedbsearch2.adapters.MoviesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private Utils() {
    }

    public static String getGenre(int genreID) {
        switch (genreID) {
            case 28:
                return "Action, ";
            case 12:
                return "Adventure, ";
            case 16:
                return "Animation, ";
            case 35:
                return "Comedy, ";
            case 80:
                return "Crime, ";
            case 99:
                return "Documentary, ";
            case 18:
                return "Drama, ";
            case 10751:
                return "Family, ";
            case 14:
                return "Fantasy, ";
            case 36:
                return "History, ";
            case 27:
                return "Horror, ";
            case 10402:
                return "Music, ";
            case 9648:
                return "Mystery, ";
            case 10749:
                return "Romance, ";
            case 878:
                return "Science Fiction, ";
            case 10770:
                return "TV Movie, ";
            case 53:
                return "Thriller, ";
            case 10752:
                return "War, ";
            default:
                return "No Genre";
        }
    }

    public static String getGenreList(List<Integer> genreIDlist){
        StringBuilder genre = new StringBuilder();
        for (int i = 0; i < genreIDlist.size(); i++) {
            genre.append(getGenre(genreIDlist.get(i)))
        }
        return "null";
    }

}
