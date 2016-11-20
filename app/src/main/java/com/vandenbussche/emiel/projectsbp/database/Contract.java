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

    public static final int DATABASE_VERSION = 1;
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
                + COLUMN_FLAG + " integer not null "
                + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
