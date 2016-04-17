package util;

import android.app.Activity;

/**
 * Created by Administrator on 2016/4/9.
 */
public class Globals {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static DBUtil util;
    public static void init(Activity a){
        SCREEN_WIDTH=a.getWindowManager().getDefaultDisplay().getWidth();
        SCREEN_HEIGHT=a.getWindowManager().getDefaultDisplay().getHeight();
        util=new DBUtil(a);
    }
}
