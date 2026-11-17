package com.example.notepadzjj;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库创建类：完全按课程要求定义库名、表名与字段
 * 数据库名 note.db，表名 note，字段 id / title / content / time
 */
public class MyHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;          // 数据库版本号
    public static String DB_NAME = "note.db";          // 数据库名称
    public static final String U_NOTEPAD = "note";     // 记事本表名称

    public MyHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + U_NOTEPAD + "("
                + "id integer primary key autoincrement,"
                + "title varchar, "
                + "content varchar, "
                + "time varchar "
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + U_NOTEPAD);
        onCreate(db);
    }
}
