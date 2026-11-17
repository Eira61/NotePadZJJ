package com.example.notepadzjj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 数据库操作类：封装 note 表的增、删、改、查
 */
public class MyDatabase {

    private final MyHelper helper;

    public MyDatabase(Context context) {
        helper = new MyHelper(context);
    }

    /** 一条记事数据的实体 */
    public static class Note {
        public long id;
        public String title;
        public String content;
        public String time;

        public Note() {
        }

        public Note(Cursor c) {
            id = c.getLong(c.getColumnIndexOrThrow("id"));
            title = c.getString(c.getColumnIndexOrThrow("title"));
            content = c.getString(c.getColumnIndexOrThrow("content"));
            time = c.getString(c.getColumnIndexOrThrow("time"));
        }
    }

    /** 当前时间，格式 yyyy-MM-dd HH:mm:ss */
    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
    }

    /** 增加：插入一条记事，返回新记录的主键 id，失败返回 -1 */
    public long insert(String title, String content) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("time", now());
        return db.insert(MyHelper.U_NOTEPAD, null, values);
    }

    /** 修改：按 id 更新标题与内容，同时刷新时间 */
    public int update(long id, String title, String content) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("time", now());
        return db.update(MyHelper.U_NOTEPAD, values, "id=?",
                new String[]{String.valueOf(id)});
    }

    /** 删除：按 id 删除一条记事 */
    public int delete(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(MyHelper.U_NOTEPAD, "id=?",
                new String[]{String.valueOf(id)});
    }

    /** 查询：查询全部记事，按 id 倒序（最新的在最上面） */
    public List<Note> queryAll() {
        List<Note> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(MyHelper.U_NOTEPAD, null, null, null, null, null, "id desc");
        try {
            while (c.moveToNext()) {
                list.add(new Note(c));
            }
        } finally {
            c.close();
        }
        return list;
    }

    /** 查询：按 id 查询单条记事，用于修改界面回显 */
    public Note queryById(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(MyHelper.U_NOTEPAD, null, "id=?",
                new String[]{String.valueOf(id)}, null, null, null);
        try {
            if (c.moveToFirst()) {
                return new Note(c);
            }
            return null;
        } finally {
            c.close();
        }
    }

    public void close() {
        helper.close();
    }
}
