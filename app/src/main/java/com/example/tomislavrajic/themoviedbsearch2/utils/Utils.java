package com.example.tomislavrajic.themoviedbsearch2.utils;

import java.util.List;

public class Utils {

    private static String getGenre(int genreID) {
        switch (genreID) {
            case 12:
                return "Adventure, ";
            case 14:
                return "Fantasy, ";
            case 16:
                return "Animation, ";
            case 18:
                return "Drama, ";
            case 27:
                return "Horror, ";
            case 28:
                return "Action, ";
            case 35:
                return "Comedy, ";
            case 36:
                return "History, ";
            case 37:
                return "Western, ";
            case 53:
                return "Thriller, ";
            case 80:
                return "Crime, ";
            case 99:
                return "Documentary, ";
            case 878:
                return "Science Fiction, ";
            case 9648:
                return "Mystery, ";
            case 10402:
                return "Music, ";
            case 10749:
                return "Romance, ";
            case 10751:
                return "Family, ";
            case 10752:
                return "War, ";
            case 10759:
                return "Action & Adventure, ";
            case 10762:
                return "Kids, ";
            case 10763:
                return "News, ";
            case 10764:
                return "Reality, ";
            case 10765:
                return "Sci-Fi & Fantasy, ";
            case 10766:
                return "Soap, ";
            case 10767:
                return "Talk, ";
            case 10768:
                return "War & Politics, ";
            case 10770:
                return "TV Movie, ";
            default:
                return "No Genre";
        }
    }

    public static String getGenreList(List<Integer> listGenreIDs) {
        StringBuilder genre = new StringBuilder();
        for (int i = 0; i < listGenreIDs.size(); i++) {
            genre.append(getGenre(listGenreIDs.get(i)));
        }
        if (genre.length() < 2) {
            return "No Genre!";
        } else {
            return genre.toString().substring(0, genre.length() - 2);
        }
    }

    public static String getErrorMessage(String code) {
        switch (code) {
            case "7":
                return "Invalid API key: You must be granted a valid key.";
            default:
                return "Something went wrong!";
        }
    }
}