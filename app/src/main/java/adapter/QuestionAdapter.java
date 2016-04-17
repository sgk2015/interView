package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buaa.interView.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/10.
 */
public class QuestionAdapter extends BaseAdapter {
    private Context ctx;
    private List<Map<String,Object>> values;

    public QuestionAdapter(Context ctx, List<Map<String, Object>> values) {
        this.ctx = ctx;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.question_line,null);
        }else {
            LinearLayout line=(LinearLayout)convertView;
            //如果当前convertView中有两个子节点，将最后的删除
            if (line.getChildCount()==2){
                line.removeViewAt(1);
            }
        }
        TextView question=(TextView)convertView.findViewById(R.id.question);
        Map<String,Object> map=values.get(position);
        question.setText(map.get("question").toString());

        if ((Boolean) map.get("showFlag")){
            LinearLayout line=(LinearLayout)convertView;
            TextView answer=new TextView(ctx);
            answer.setText(values.get(position).get("answer").toString());
            answer.setTextSize(14);
            answer.setTextColor(Color.RED);
            line.addView(answer);
        }
        return convertView;
    }
}
