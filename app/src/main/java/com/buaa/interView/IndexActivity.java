package com.buaa.interView;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import util.Globals;
import util.QuestionDao;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取得主屏的宽和高，创建并初始化SQLite数据库
        Globals.init(this);
        setContentView(R.layout.activity_index);

        //加载欢迎界面的同时在子线程做加载数据的操作
        new Thread(){
            @Override
            public void run() {
                try {
                    //创建SharePreferrnces存储临时变量
                    SharedPreferences s = getSharedPreferences("ques", MODE_PRIVATE);
                    //question的值，如果之前已经创建，则正常返回，否则，返回默认值false
                    boolean saved = s.getBoolean("questionFlag", false);
                    if (saved) {
                        Thread.sleep(2000);

                    } else {
                        //清除数据库中原有的数据，重新加载
                        QuestionDao.deleteData();
                        //读取assets中的question.txt中的数据，
                        InputStream in = IndexActivity.this.getClass().getClassLoader().getResourceAsStream("assets/question.txt");
                        BufferedReader br = new BufferedReader(new InputStreamReader(in, "GBK"));
                        //将取到的数据存储在SQLite数据库中
                        getFileData(br);
                        //数据存入数据库之后，将其标志位改为true
                        Editor e = s.edit();
                        e.putBoolean("questionFlag", true);
                        e.commit();
                    }
                    //数据加载完成之后进行页面的跳转
                    Intent intent = new Intent(IndexActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getFileData(BufferedReader br) throws Exception{
        String line=null;
        StringBuffer question=new StringBuffer();
        StringBuffer answer=new StringBuffer();
        boolean questionFlag=false;
        while ((line=br.readLine())!=null){
            if(line.equals("QUESTION_BEGIN")){
                questionFlag=true;
            }else if (line.equals("ANSWER_BEGIN")){
                questionFlag=false;
            }else if (line.equals("END")){
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("question",question);
                map.put("answer",answer);
                QuestionDao.insertQuestion(map);
                question=new StringBuffer();
                answer=new StringBuffer();
            }else {
                if (questionFlag){
                    question.append(line+"\r\n");
                }else {
                    answer.append(line+"\r\n");
                }
            }
        }
    }
}
