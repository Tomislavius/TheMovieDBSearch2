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
            case "1":
                return "Success.";
            case "2":
                return "Invalid service: this service does not exist.";
            case "3":
                return "Authentication failed: You do not have permissions to access the service.";
            case "4":
                return "Invalid format: This service doesn't exist in that format.";
            case "5":
                return "Invalid parameters: Your request parameters are incorrect.";
            case "6":
                return "Invalid id: The pre-requisite id is invalid or not found.";
            case "7":
                return "Invalid API key: You must be granted a valid key.";
            case "8":
                return "Duplicate entry: The data you tried to submit already exists.";
            case "9":
                return "Service offline: This service is temporarily offline, try again later.";
            case "10":
                return "Suspended API key: Access to your account has been suspended, contact TMDb.";
            case "11":
                return "Internal error: Something went wrong, contact TMDb.";
            case "12":
                return "The item/record was updated successfully.";
            case "13":
                return "The item/record was deleted successfully.";
            case "14":
                return "Authentication failed.";
            case "15":
                return "Failed.";
            case "16":
                return "Device denied.";
            case "17":
                return "Session denied.";
            case "18":
                return "Validation failed.";
            case "19":
                return "Invalid accept header.";
            case "20":
                return "Invalid date range: Should be a range no longer than 14 days.";
            case "21":
                return "Entry not found: The item you are trying to edit cannot be found.";
            case "22":
                return "Invalid page: Pages start at 1 and max at 1000. They are expected to be an integer.";
            case "23":
                return "Invalid date: Format needs to be YYYY-MM-DD.";
            case "24":
                return "Your request to the backend server timed out. Try again.";
            case "25":
                return "Your request count (#) is over the allowed limit of (40).";
            case "26":
                return "You must provide a username and password.";
            case "27":
                return "Too many append to response objects: The maximum number of remote calls is 20.";
            case "28":
                return "Invalid timezone: Please consult the documentation for a valid timezone.";
            case "29":
                return "You must confirm this action: Please provide a confirm=true parameter.";
            case "30":
                return "Invalid username and/or password: You did not provide a valid login.";
            case "31":
                return "Account disabled: Your account is no longer active. Contact TMDb if this is an error.";
            case "32":
                return "Email not verified: Your email address has not been verified.";
            case "33":
                return "Invalid request token: The request token is either expired or invalid.";
            case "34":
                return "The resource you requested could not be found.";
            default:
                return "Something went wrong!";
        }
    }
}