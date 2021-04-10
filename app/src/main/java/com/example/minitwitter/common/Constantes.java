package com.example.minitwitter.common;

public class Constantes {
    public static final String API_MINITWITTER_BASE_URL = "https://www.minitwitter.com:3001/apiv1/";
    public static final String API_MINITWITTER_FILES_URL = "https://www.minitwitter.com/apiv1/uploads/photos/";
    public static final String ARG_TWEET_ID = "TWEET_ID";

    //startActivityForResult
    public static final int SELECT_PHOTO_GALLERY = 1;

    //Preferences
    public static String PREF_TOKEN = "PREF_TOKEN";
    public static String PREF_USERNAME = "PREF_USERNAME";
    public static String PREF_EMAIL = "PREF_EMAIL";
    public static String PREF_PHOTOURL = "PREF_PHOTOURL";
    public static String PREF_CREATED = "PREF_CREATED";
    public static String PREF_ACTIVE = "PREF_ACTIVE";

    //Arguments
    public static final String TWEET_LIST_TYPE = "TWEET_LIST_TYPE";
    public static final int TWEET_LIST_ALL = 1;
    public static final int TWEET_LIST_FAVS = 2;
}
