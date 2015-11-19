package wxm.com.androiddesign.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zero on 8/19/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBhelper";
    private static final int VERTION = 1;

//    public DatabaseHelper(){
//        super();
//    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO 创建数据库后，对数据库操作
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO 更新数据库
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //TODO 每次打开数据库后首次执行
    }
}
