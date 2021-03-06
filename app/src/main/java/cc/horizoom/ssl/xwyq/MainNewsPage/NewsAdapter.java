package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.NewsData;
import cc.horizoom.ssl.xwyq.DataManager.entity.NewsEntity;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.network.volley.VolleyError;

/**
 * Created by pi on 15-9-5.
 * 新闻适配器
 */
public class NewsAdapter extends BaseAdapter{

    protected ArrayList<NewsEntity> data;

    protected BaseActivity baseActivity;

    public NewsAdapter(BaseActivity baseActivity,ArrayList<NewsEntity> data) {
        this.baseActivity = baseActivity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView;
        if (null == view) {
            holderView = new HolderView();
            LayoutInflater layoutInflater = LayoutInflater.from(baseActivity);
            view = layoutInflater.inflate(R.layout.view_news_item,null);
            holderView.outLl = (LinearLayout) view.findViewById(R.id.outLl);
            holderView.contentTv = (TextView) view.findViewById(R.id.contentTv);
            holderView.numTv = (TextView) view.findViewById(R.id.numTv);
            holderView.timeTv = (TextView) view.findViewById(R.id.timeTv);
            holderView.smallBellIv = (ImageView) view.findViewById(R.id.smallBellIv);
            holderView.sourceTv = (TextView) view.findViewById(R.id.sourceTv);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        NewsEntity newsEntity = data.get(i);
//        holderView.numTv.setText(i+"");
        holderView.contentTv.setText(newsEntity.getTitle());
        holderView.timeTv.setText(newsEntity.getPublishTime());
        if (TextUtils.isEmpty(newsEntity.getSource())) {
            holderView.sourceTv.setText("来源：未知");
        } else {
            holderView.sourceTv.setText("来源："+newsEntity.getSource());
        }

        if (newsEntity.getPupushLevel() >= 5) {
            holderView.smallBellIv.setVisibility(View.VISIBLE);
        } else {
            holderView.smallBellIv.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    class HolderView {
        LinearLayout outLl;//外部控键
        TextView numTv;//序号
        TextView contentTv;//内容
        TextView timeTv;//时间
        ImageView smallBellIv;//铃铛
        TextView sourceTv;//信息源
    }

}
