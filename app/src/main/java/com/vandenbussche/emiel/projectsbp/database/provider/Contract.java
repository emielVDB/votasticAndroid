package com.vandenbussche.emiel.projectsbp.database.provider;

import android.net.Uri;

/**
 * Created by Stijn on 10/10/2016.
 */
public class Contract {

    public static final String AUTHORITY = "com.vandenbussche.emiel.Votastic";

    //CONTENT-URIS
    public static final Uri POLLS_URI = Uri.parse("content://" + AUTHORITY + "/polls");
    public static final Uri POLLS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/polls/");
    public static final Uri PAGES_URI = Uri.parse("content://" + AUTHORITY + "/pages");
    public static final Uri PAGES_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/pages/");
    public static final Uri FOLLOWS_URI = Uri.parse("content://" + AUTHORITY + "/follows");
    public static final Uri FOLLOWS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/follows/");
    public static final Uri NOTIFICATIONS_URI = Uri.parse("content://" + AUTHORITY + "/notifications");
    public static final Uri NOTIFICATIONS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/notifications/");
    public static final Uri UPLOAD_IMAGES_URI = Uri.parse("content://" + AUTHORITY + "/uploadimages");
    public static final Uri UPLOAD_IMAGES_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/uploadimages/");

    //MIME-TYPES
    public static final String POLLS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.votastic.poll";
    public static final String POLLS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.votastic.poll";
    public static final String PAGES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.votastic.page";
    public static final String PAGES_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.votastic.page";
    public static final String FOLLOWS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.votastic.follow";
    public static final String NOTIFICATIONS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.votastic.notification";
    public static final String UPLOAD_IMAGES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.votastic.uploadimage";

    public static final int POLL_ID_PATH_POSITION = 1;
    public static final int PAGE_ID_PATH_POSITION = 1;


    public static final Uri POLL_UPLOADED_URI = Uri.parse("content://" + AUTHORITY + "/polluploaded/");
}
