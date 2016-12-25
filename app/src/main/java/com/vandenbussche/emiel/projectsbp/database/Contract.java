package com.vandenbussche.emiel.projectsbp.database;

import android.provider.BaseColumns;

/**
 * Created by Stijn on 26/09/2016.
 */
public class Contract {

        /*
    Your contract basically defines your database and how people should interact with it through the Content Provider.
    A contract class defines constants that help applications work with the content URIs,
    column names, intent actions, and other features of a content provider. Contract classes are not
    included automatically with a provider; the provider's developer has to define them and then make them available to other developers.
    */

    public static final int DATABASE_VERSION = 10;
    public static final String DATABASE_NAME = "database.db";

    public interface PollsColumns extends BaseColumns {
        public static final String TABLE_NAME = "polls";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_TAGS = "tags"; //json formaat insteken
        public static final String COLUMN_OPTIONS = "options"; //json insteken
        public static final String COLUMN_CHOICE_INDEX = "choice_index";
        public static final String COLUMN_TOTAL_VOTES= "total_votes";
        public static final String COLUMN_TOTAL_REACTIONS= "total_reactions";
        public static final String COLUMN_FLAG= "flag";
        public static final String COLUMN_UPLOAD_TIME= "upload_time";
        public static final String COLUMN_PAGE_ID= "page_id";
        public static final String COLUMN_PAGE_TITLE= "page_title";
    }
    public interface PagesColumns extends BaseColumns {
        public static final String TABLE_NAME = "pages";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TAGS = "tags";
        public static final String COLUMN_POLLS_COUNT= "polls_count";
        public static final String COLUMN_FLAG= "flag";
    }

    public interface FollowsColumns extends BaseColumns {
        public static final String TABLE_NAME = "follows";
        public static final String COLUMN_PAGE_ID = "page_id";
        public static final String COLUMN_FLAG= "flag";
    }

    public interface NotificationsColumns extends BaseColumns {
        public static final String TABLE_NAME = "notifications";
        public static final String COLUMN_MESSAGE = "message";
    }

    public static abstract class PollsDB implements PollsColumns {

        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "( "
                + _ID + " string primary key, "
                + COLUMN_QUESTION + " text not null, "
                + COLUMN_TAGS + " text not null, "
                + COLUMN_OPTIONS + " text not null, "
                + COLUMN_CHOICE_INDEX + " integer not null, "
                + COLUMN_TOTAL_VOTES + " integer not null, "
                + COLUMN_TOTAL_REACTIONS + " integer not null, "
                + COLUMN_FLAG + " integer not null, "
                + COLUMN_UPLOAD_TIME + " integer, "
                + COLUMN_PAGE_ID + " text, "
                + COLUMN_PAGE_TITLE + " text "
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class PagesDB implements PagesColumns {

        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "( "
                + _ID + " string primary key, "
                + COLUMN_TITLE + " text not null, "
                + COLUMN_TAGS + " text not null, "
                + COLUMN_POLLS_COUNT + " integer not null, "
                + COLUMN_FLAG + " integer not null "
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class FollowsDB implements FollowsColumns {

        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "( "
                + _ID + " integer primary key autoincrement, "
                + COLUMN_PAGE_ID + " text not null, "
                + COLUMN_FLAG + " integer not null "
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    
    public static abstract class NotificationsDB implements NotificationsColumns {

        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "( "
                + _ID + " integer primary key autoincrement, "
                + COLUMN_MESSAGE + " text not null"
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
