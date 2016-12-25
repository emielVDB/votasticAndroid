package com.vandenbussche.emiel.projectsbp.database.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vandenbussche.emiel.projectsbp.database.DatabaseHelper;

import java.util.HashMap;


/**
 * Created by Stijn on 10/10/2016.
 */
public class VotasticContentProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;
    //private static final String PRODUCTS_DB = "Products";

    private static final int POLLS = 1;
    private static final int POLL_ID = 2;
    private static final int PAGES = 3;
    private static final int PAGE_ID = 4;
    private static final int FOLLOWS = 5;
    private static final int NOTIFICATIONS = 6;

    private static HashMap<String, String> POLLS_PROJECTION_MAP;
    private static HashMap<String, String> PAGES_PROJECTION_MAP;
    private static HashMap<String, String> FOLLOWS_PROJECTION_MAP;
    private static HashMap<String, String> NOTIFICATIONS_PROJECTION_MAP;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.AUTHORITY, "polls", POLLS);
        uriMatcher.addURI(Contract.AUTHORITY, "polls/#", POLL_ID);
        uriMatcher.addURI(Contract.AUTHORITY, "pages", PAGES);
        uriMatcher.addURI(Contract.AUTHORITY, "pages/#", PAGE_ID);
        uriMatcher.addURI(Contract.AUTHORITY, "follows", FOLLOWS);
        uriMatcher.addURI(Contract.AUTHORITY, "notifications", NOTIFICATIONS);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = DatabaseHelper.getInstance(getContext());
        POLLS_PROJECTION_MAP = new HashMap<>();
        POLLS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns._ID, com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns._ID);
        POLLS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_QUESTION, com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_QUESTION);
        POLLS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_TAGS, com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_TAGS);
        POLLS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_OPTIONS, com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_OPTIONS);
        POLLS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_CHOICE_INDEX, com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_CHOICE_INDEX);
        POLLS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_TOTAL_VOTES, com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_TOTAL_VOTES);
        POLLS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_TOTAL_REACTIONS, com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_TOTAL_REACTIONS);
        POLLS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_FLAG, com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_FLAG);
        POLLS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_PAGE_ID, com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_PAGE_ID);
        POLLS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_PAGE_TITLE, com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns.COLUMN_PAGE_TITLE);

        PAGES_PROJECTION_MAP = new HashMap<>();
        PAGES_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PagesColumns._ID, com.vandenbussche.emiel.projectsbp.database.Contract.PagesColumns._ID);
        PAGES_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PagesColumns.COLUMN_TITLE, com.vandenbussche.emiel.projectsbp.database.Contract.PagesColumns.COLUMN_TITLE);
        PAGES_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PagesColumns.COLUMN_TAGS, com.vandenbussche.emiel.projectsbp.database.Contract.PagesColumns.COLUMN_TAGS);
        PAGES_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PagesColumns.COLUMN_POLLS_COUNT, com.vandenbussche.emiel.projectsbp.database.Contract.PagesColumns.COLUMN_POLLS_COUNT);
        PAGES_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.PagesColumns.COLUMN_FLAG, com.vandenbussche.emiel.projectsbp.database.Contract.PagesColumns.COLUMN_FLAG);
        
        FOLLOWS_PROJECTION_MAP = new HashMap<>();
        FOLLOWS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.FollowsColumns._ID, com.vandenbussche.emiel.projectsbp.database.Contract.FollowsColumns._ID);
        FOLLOWS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.FollowsColumns.COLUMN_PAGE_ID, com.vandenbussche.emiel.projectsbp.database.Contract.FollowsColumns.COLUMN_PAGE_ID);
        FOLLOWS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.FollowsColumns.COLUMN_FLAG, com.vandenbussche.emiel.projectsbp.database.Contract.FollowsColumns.COLUMN_FLAG);

        NOTIFICATIONS_PROJECTION_MAP = new HashMap<>();
        NOTIFICATIONS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.NotificationsColumns._ID, com.vandenbussche.emiel.projectsbp.database.Contract.NotificationsColumns._ID);
        NOTIFICATIONS_PROJECTION_MAP.put(com.vandenbussche.emiel.projectsbp.database.Contract.NotificationsColumns.COLUMN_MESSAGE, com.vandenbussche.emiel.projectsbp.database.Contract.NotificationsColumns.COLUMN_MESSAGE);
      
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String limitString = null;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case POLLS:
                if(sortOrder == null) sortOrder = com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB.COLUMN_UPLOAD_TIME+" desc";
                queryBuilder.setTables(com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB.TABLE_NAME);
                queryBuilder.setProjectionMap(POLLS_PROJECTION_MAP);
                break;
            case PAGES:
                queryBuilder.setTables(com.vandenbussche.emiel.projectsbp.database.Contract.PagesDB.TABLE_NAME);
                queryBuilder.setProjectionMap(PAGES_PROJECTION_MAP);
                break;
            case FOLLOWS:
                queryBuilder.setTables(com.vandenbussche.emiel.projectsbp.database.Contract.FollowsDB.TABLE_NAME);
                queryBuilder.setProjectionMap(FOLLOWS_PROJECTION_MAP);
                break;
            case NOTIFICATIONS:
                queryBuilder.setTables(com.vandenbussche.emiel.projectsbp.database.Contract.NotificationsDB.TABLE_NAME);
                queryBuilder.setProjectionMap(NOTIFICATIONS_PROJECTION_MAP);
                limitString = "0, 5";
                break;

            case POLL_ID:
                queryBuilder.setTables(com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB.TABLE_NAME);
                queryBuilder.setProjectionMap(POLLS_PROJECTION_MAP);

                String pollid = uri.getPathSegments().get(Contract.POLL_ID_PATH_POSITION);
                DatabaseUtils.concatenateWhere(selection, "( " + com.vandenbussche.emiel.projectsbp.database.Contract.PollsColumns._ID + " = ?" + ")"); //strict genomen haakjes niet nodig
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs, new String[]{"" + pollid});

                break;

            case PAGE_ID:
                queryBuilder.setTables(com.vandenbussche.emiel.projectsbp.database.Contract.PagesDB.TABLE_NAME);
                queryBuilder.setProjectionMap(PAGES_PROJECTION_MAP);

                String pageid = uri.getPathSegments().get(Contract.PAGE_ID_PATH_POSITION);
                DatabaseUtils.concatenateWhere(selection, "( " + com.vandenbussche.emiel.projectsbp.database.Contract.PagesColumns._ID + " = ?" + ")"); //strict genomen haakjes niet nodig
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs, new String[]{"" + pageid});

                break;


            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor data = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder,
                limitString
        );

        data.getCount();

        data.setNotificationUri(getContext().getContentResolver(), uri);
        return data;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case POLLS:
                return Contract.POLLS_CONTENT_TYPE;
            case POLL_ID:
                return Contract.POLLS_ITEM_CONTENT_TYPE;
            case PAGES:
                return Contract.PAGES_CONTENT_TYPE;
            case PAGE_ID:
                return Contract.PAGES_ITEM_CONTENT_TYPE;
            case FOLLOWS:
                return Contract.FOLLOWS_CONTENT_TYPE;
            case NOTIFICATIONS:
                return Contract.NOTIFICATIONS_CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case POLLS:
                long newRowId = db.insert(
                        com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);

                Uri pollsItemUri = ContentUris.withAppendedId(Contract.POLLS_ITEM_URI, newRowId);
                getContext().getContentResolver().notifyChange(pollsItemUri, null);
                return pollsItemUri;

            case PAGES:
                newRowId = db.insert(
                        com.vandenbussche.emiel.projectsbp.database.Contract.PagesDB.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);

                Uri pagesItemUri = ContentUris.withAppendedId(Contract.PAGES_ITEM_URI, newRowId);
                getContext().getContentResolver().notifyChange(pagesItemUri, null);
                return pagesItemUri;

            case FOLLOWS:
                newRowId = db.insert(
                        com.vandenbussche.emiel.projectsbp.database.Contract.FollowsDB.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);

                Uri followsItemUri = ContentUris.withAppendedId(Contract.FOLLOWS_ITEM_URI, newRowId);
                getContext().getContentResolver().notifyChange(followsItemUri, null);
                return followsItemUri;

            case NOTIFICATIONS:
                newRowId = db.insert(
                        com.vandenbussche.emiel.projectsbp.database.Contract.NotificationsDB.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);

                Uri notificationsItemUri = ContentUris.withAppendedId(Contract.NOTIFICATIONS_ITEM_URI, newRowId);
                getContext().getContentResolver().notifyChange(notificationsItemUri, null);
                return notificationsItemUri;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String finalWhere;
        int count;
        switch (uriMatcher.match(uri)) {
            case POLLS:
                count = db.delete(
                        com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case PAGES:
                count = db.delete(
                        com.vandenbussche.emiel.projectsbp.database.Contract.PagesDB.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case FOLLOWS:
                count = db.delete(
                        com.vandenbussche.emiel.projectsbp.database.Contract.FollowsDB.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case POLL_ID:
                String pollItemId = uri.getPathSegments().get(1);
                finalWhere = "_id = " + pollItemId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB.TABLE_NAME,
                        finalWhere,
                        selectionArgs
                );
                break;

            case PAGE_ID:
                String pageItemId = uri.getPathSegments().get(1);
                finalWhere = "_id = " + pageItemId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        com.vandenbussche.emiel.projectsbp.database.Contract.PagesDB.TABLE_NAME,
                        finalWhere,
                        selectionArgs
                );
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (uriMatcher.match(uri)) {
            case POLLS:
                count = db.update(
                        com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;

            case PAGES:
                count = db.update(
                        com.vandenbussche.emiel.projectsbp.database.Contract.PagesDB.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case FOLLOWS:
                count = db.update(
                        com.vandenbussche.emiel.projectsbp.database.Contract.FollowsDB.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;

            case POLL_ID:
                String pollId = uri.getPathSegments().get(1);
                finalWhere = "_id = " + pollId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        com.vandenbussche.emiel.projectsbp.database.Contract.PollsDB.TABLE_NAME,
                        values,
                        finalWhere,
                        selectionArgs
                );
                break;

            case PAGE_ID:
                String pageId = uri.getPathSegments().get(1);
                finalWhere = "_id = " + pageId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        com.vandenbussche.emiel.projectsbp.database.Contract.PagesDB.TABLE_NAME,
                        values,
                        finalWhere,
                        selectionArgs
                );
                break;
            
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}

