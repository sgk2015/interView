package com.buaa.interView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.MyPagerAdapter;
import adapter.QuestionAdapter;
import util.Globals;
import util.QuestionDao;

public class MainActivity extends AppCompatActivity {

    //原BaseActivity用
    private Button setBtn;
    private TextView title;
    private Button[] btns=new Button[3];
    private int[] bottomImages={R.drawable.left_a,R.drawable.middle_a,R.drawable.right_a};
    //选中时使用的图片
    private int[] bottomImagesSelected={R.drawable.left_b,R.drawable.middle_b,R.drawable.right_b};
    private  int i;
    //浮动窗口
    private PopupWindow pop;

    //原QuestionActivity用
    private ListView list;
    private QuestionAdapter adapter;
    private List<Map<String,Object>> values;

    //分页查询使用的变量
    private int pagenum;
    private int pagesize;
    private int recordCount;
    private int pageCount;

    //用来判断分页
    private int first;
    private int visCount;
    private int total;

    //页脚
    private TextView footerView;

    //搜索关键字
    private String keyword;

    //原searchActivity用
    private Button searchBtn;
    private EditText keywordText;

    //ViewPage用
    private ViewPager viewPager;
    private List<View> views=new ArrayList<View>();
    private MyPagerAdapter pagerAdapter;
    private String[] allTitles={"程序员面试宝典","全部问题","搜索"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init("程序员面试宝典",0);

        viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        views.add(LayoutInflater.from(this).inflate(R.layout.page_main,null));
        View questionView=LayoutInflater.from(this).inflate(R.layout.page_question,null);
        initQuestion(questionView);
        views.add(questionView);
        View searchView=LayoutInflater.from(this).inflate(R.layout.page_search,null);
        initSearch(searchView);
        views.add(searchView);
        pagerAdapter=new MyPagerAdapter(views);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i=0;i<views.size();i++){
                    if (i==position){
                        btns[i].setBackgroundResource(bottomImagesSelected[i]);
                    }else{
                        btns[i].setBackgroundResource(bottomImages[i]);
                    }
                }
                title.setText(allTitles[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void init(String titleName,int flag){
        setBtn=(Button)findViewById(R.id.setBtn);

        title=(TextView)findViewById(R.id.list_title);
        title.setText(titleName);

        btns[0]=(Button)findViewById(R.id.bottom_btn01);
        btns[1]=(Button)findViewById(R.id.bottom_btn02);
        btns[2]=(Button)findViewById(R.id.bottom_btn03);

        for (i=0;i<btns.length;i++){
            final int temp=i;
            if (i==flag){
                btns[i].setBackgroundResource(bottomImagesSelected[i]);
            }else {
                btns[i].setBackgroundResource(bottomImages[i]);
            }
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(temp);
                }
            });
        }
      setBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (pop==null){
                  pop=new PopupWindow(Globals.SCREEN_WIDTH/4,Globals.SCREEN_HEIGHT/6);
                  View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_line,null);
                  pop.setContentView(view);

                  TextView version=(TextView)view.findViewById(R.id.version);
                  TextView about=(TextView)view.findViewById(R.id.about);
                  TextView exit=(TextView)view.findViewById(R.id.exit);

                  //版本信息
                  version.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          Toast.makeText(MainActivity.this,"当前版本号:1.0",Toast.LENGTH_LONG).show();
                      }
                  });

                  //关于我们
                  about.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                        Builder builder=new Builder(MainActivity.this);
                          builder.setTitle("关于我们");
                          builder.setMessage("练习作品（程序员面试宝典）");
                          builder.setNegativeButton("关闭",new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {

                              }
                          });
                          builder.create().show();
                      }
                  });

                  //退出功能
                  exit.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          finish();
                      }
                  });
              }
              //判断是否正在显示，如果在显示时点击，那么隐藏
              if (pop.isShowing()){
                  pop.dismiss();
              }else{
                  pop.showAsDropDown(setBtn);
              }
          }
      });
    }

    public void initQuestion(View question){
        if (keyword==null){
            keyword="";
        }
        pagesize=16;
        pagenum=1;
        recordCount= QuestionDao.getCount(keyword);
        pageCount=(recordCount-1)/pagesize+1;

        list=(ListView)question.findViewById(R.id.list);

        //在listView中加入尾部提示文字
        if (pagenum<pageCount){
            footerView=new TextView(this);
            footerView.setText("数据正在加载中，请稍候...");
            footerView.setTextSize(12);
            footerView.setTextColor(Color.BLACK);
            list.addFooterView(footerView);
        }

        //数据库读取所有记录
        values=QuestionDao.listData(pagenum,pagesize,keyword);
        adapter=new QuestionAdapter(this,values);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout=(LinearLayout)view;

                //如果LinearLayout中的子节点数为1，说明只有问题，那么加入答案
                if (layout.getChildCount()==1){
                    TextView answer=new TextView(MainActivity.this);
                    answer.setText(values.get(position).get("answer").toString());
                    answer.setTextSize(14);
                    answer.setTextColor(Color.RED);
                    layout.addView(answer);
                    //当点击问题时，答案展开，所以showFlag要变成true
                    values.get(position).put("showFlag",true);
                }else{
                    //子节点数不为1，说明有答案也有问题，那么把答案去掉
                    layout.removeViewAt(1);
                    //再次点击问题时，答案收回，showFlag变为false
                    values.get(position).put("showFlag",false);
                }
            }
        });

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState==SCROLL_STATE_IDLE&&first+visCount==total&&total!=0){
                    if (pagenum<pageCount){
                        pagenum++;
                        values.addAll(QuestionDao.listData(pagenum,pagesize,keyword));
                        adapter.notifyDataSetChanged();
                    }else {
                        if (list.getFooterViewsCount()!=0){
                            list.removeFooterView(footerView);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                first = firstVisibleItem;
                visCount=visibleItemCount;
                total=totalItemCount;
            }
        });
    }


    public void initSearch(View search){
        keywordText=(EditText)search.findViewById(R.id.keyword);
        searchBtn=(Button)search.findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword=keywordText.getText().toString();
                pagenum=1;
                values.clear();
                values.addAll(QuestionDao.listData(pagenum,pagesize,keyword));
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(1);
            }
        });
    }
}
