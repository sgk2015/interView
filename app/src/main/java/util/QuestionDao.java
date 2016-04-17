package util;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/9.
 */
public class QuestionDao {
    //插入数据
    public static void insertQuestion(Map<String,Object> map){
        String sql="insert into questions (question,answer) values (?,?)";
        Globals.util.getWritableDatabase().execSQL(sql,new Object[]{map.get("question"),map.get("answer")});

    }
    //查询全部数据
    public static List<Map<String,Object>> listData(){
        List<Map<String,Object>> values=new ArrayList<Map<String, Object>>();
        String sql="select id,question,answer from questions";
        Cursor c=Globals.util.getReadableDatabase().rawQuery(sql,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("question",c.getInt(0)+"."+c.getString(1));
            map.put("answer",c.getString(2));
            values.add(map);
            c.moveToNext();
        }
        c.close();
        return values;
    }

    //分页显示数据库信息
    public static List<Map<String,Object>> listData(int pagenum,int pagesize,String keyword){
        List<Map<String,Object>> values=new ArrayList<Map<String,Object>>();
        String sql="select id,question,answer from questions where question like ? limit ?,?";
        Cursor c=Globals.util.getReadableDatabase().rawQuery(sql,new String[]{"%"+keyword+"%",(pagenum-1)*pagesize+"",pagesize+""});
        c.moveToFirst();
        while (!c.isAfterLast()){
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("question",c.getInt(0)+"."+c.getString(1));
            map.put("answer",c.getString(2));
            //加入标志位，表示当前答案是否展开
            map.put("showFlag",false);
            values.add(map);
            c.moveToNext();
        }
        c.close();
        return values;
    }

    //查询数据库记录的条数
    public static int getCount(String keyword){
        int recordCount=0;
        String sql="select count(*) from questions where question like ?";
        Cursor c=Globals.util.getReadableDatabase().rawQuery(sql,new String[]{"%"+keyword+"%"});
        c.moveToFirst();
        recordCount=c.getInt(0);
        c.close();
        return recordCount;
    }

    //删除数据库数据
    public static void deleteData(){
        String sql="delete from questions";
        Globals.util.getWritableDatabase().execSQL(sql);
    }
}
