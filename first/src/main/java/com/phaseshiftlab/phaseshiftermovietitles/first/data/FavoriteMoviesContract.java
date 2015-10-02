package com.phaseshiftlab.phaseshiftermovietitles.first.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by phaseshiftlab on 10/1/2015.
 */
public class FavoriteMoviesContract {
    public static final String CONTENT_AUTHORITY = "com.phaseshiftlab.phaseshiftermovietitles.first";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE_FAVORITES = "favorites";

    public static final class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_FAVORITES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_FAVORITES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_FAVORITES;

        // Table name
        public static final String TABLE_NAME = "movie_favorites";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";

        public static Uri buildFavoritesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
