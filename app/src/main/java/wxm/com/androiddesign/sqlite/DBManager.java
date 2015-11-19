package wxm.com.androiddesign.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Zero on 8/19/2015.
 */
public class DBManager {
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context, String name) {
        helper = new DatabaseHelper(context, name, null, 1);

        db = helper.getWritableDatabase();
    }
}
