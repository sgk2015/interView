package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/4/9.
 */
public class DBUtil extends SQLiteOpenHelper {

    private static final String DBNAME="interview.db";
    private static final int VERSION=1;

    public DBUtil(Context ctx){
        super(ctx,DBNAME,null,VERSION);
    }

   public DBUtil(Context context, String name, CursorFactory factory,int version){
       super(context,name,factory,version);
   }

    //第一次创建的时候只调用一次，用于创建数据库表结构
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table questions(id integer primary key,question text,answer text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
