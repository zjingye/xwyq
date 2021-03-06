package cc.horizoom.ssl.xwyq.setting.warning;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.NewsData;
import cc.horizoom.ssl.xwyq.DataManager.NewsListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.DataManager.WarningParatmer;
import cc.horizoom.ssl.xwyq.DataManager.entity.NewsEntity;
import cc.horizoom.ssl.xwyq.MainNewsPage.NewsAdapter;
import cc.horizoom.ssl.xwyq.MainNewsPage.NewsPageActivity;
import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.setting.more.MoreActivity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;
import cn.com.myframe.network.volley.VolleyError;
import cn.com.myframe.view.MyBounceListView.MyBounceListview;
import cn.com.myframe.webVeiw.MyWebVeiwClient;

/**
 * Created by pizhuang on 2015/9/11.
 * 预警页面
 */
public class WarningActivity extends MyBaseActivity implements View.OnClickListener {

    private RelativeLayout backRl;//返回

    private RelativeLayout headRl;//上半部分

    private TextView numNewstv;//安全得分

    private TextView redNumTv;//红色级别数量

    private TextView orangeNumTv;//橙色级别数量

    private TextView blueNumTv;//蓝色级别数量

    private LinearLayout barDownLl;//向下啦

    private LinearLayout barUpLl;//向上拉

    private MyBounceListview listview;//消息列表

    private WebView webView;//柱状图

    private ArrayList<NewsEntity> data;

    protected NewsAdapter newsAdapter;//新闻适配起

    private ImageView bellRedIv;//红色级别

    private ImageView bellOrangeIv;//橙色级别

    private ImageView bellBlueIv;//蓝色级别

    private LinearLayout redLl;//红色级别按钮

    private LinearLayout orangeLl;//橙色级别按钮

    private LinearLayout blueLl;//蓝色级别按钮

    private AdapterView.OnItemClickListener myListOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (data.size() > 0 && l>=0) {
                NewsEntity newsEntity = data.get((int)l);
                requestNews(newsEntity);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waring);
        backRl = (RelativeLayout) findViewById(R.id.backRl);//返回
        numNewstv = (TextView) findViewById(R.id.num_news_tv);//新闻总数
        redNumTv = (TextView) findViewById(R.id.redNumTv);
        orangeNumTv = (TextView) findViewById(R.id.orangeNumTv);
        blueNumTv = (TextView) findViewById(R.id.blueNumTv);
        barDownLl = (LinearLayout) findViewById(R.id.barDownLl);//向下啦
        barUpLl = (LinearLayout) findViewById(R.id.barUpLl);//向上拉
        listview = (MyBounceListview) findViewById(R.id.listView);//消息列表
        webView = (WebView) findViewById(R.id.webView);//柱状图
        headRl = (RelativeLayout) findViewById(R.id.headRl);
        redLl = (LinearLayout) findViewById(R.id.red_ll);//红色级别按钮
        orangeLl = (LinearLayout) findViewById(R.id.orange_ll);//橙色级别按钮
        blueLl = (LinearLayout) findViewById(R.id.blue_ll);//蓝色级别按钮
        bellRedIv = (ImageView) findViewById(R.id.bell_red_iv);//红色级别
        bellOrangeIv = (ImageView) findViewById(R.id.bell_orange_iv);//橙色级别
        bellBlueIv = (ImageView) findViewById(R.id.bell_blue_iv);//蓝色级别
        redLl.setOnClickListener(this);
        orangeLl.setOnClickListener(this);
        blueLl.setOnClickListener(this);
        backRl.setOnClickListener(this);
        barDownLl.setOnClickListener(this);
        barUpLl.setOnClickListener(this);
        numNewstv.setOnClickListener(this);
        updataView();


        String customer_id = UserData.getInstance().getCustomerId(this);
        String url = Protocol.WC+"?customer_id="+customer_id;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebVeiwClient());
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.loadUrl(url);
    }

    /**
     * 更新页面显示
     */
    private void updataView() {
        listview.addFooterView(getFooter());
        data = NewsListData.getInstance().getNewsData(this);
        newsAdapter = new NewsAdapter(this, data);
        listview.setAdapter(newsAdapter);
        listview.setOnItemClickListener(myListOnItemClickListener);
        String a_value = WarningParatmer.getInstance().getA_value(this);
        String b_value = WarningParatmer.getInstance().getB_value(this);
        String c_value = WarningParatmer.getInstance().getC_value(this);
        String d_value = WarningParatmer.getInstance().getD_value(this);
        numNewstv.setText(a_value);
        redNumTv.setText(b_value);
        orangeNumTv.setText(c_value);
        blueNumTv.setText(d_value);
    }

    /**
     * 获得list眉脚
     * @return
     */
    private View getFooter() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View v = layoutInflater.inflate(R.layout.view_news_footer, null);
        TextView loadTv = (TextView) v.findViewById(R.id.loadTv);
        loadTv.setText("点击查看更多历史预警");
        loadTv.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backRl:
                closeActivity(WarningActivity.class.getName());
                break;
            case R.id.barUpLl:
                showWebView();
                break;
            case R.id.barDownLl:
                hideWebView();
                break;
            case R.id.loadTv:
                long page = NewsListData.getInstance().getPage(this);
                if (page == -1) {
                    NewsListData.getInstance().setPage(0);
                }
                requestWarningNewList();
                break;
            case R.id.red_ll:
                setBellStatus(2);
                requestWarningListBuyLevel(2);
                break;
            case R.id.orange_ll:
                setBellStatus(1);
                requestWarningListBuyLevel(1);
                break;
            case R.id.blue_ll:
                setBellStatus(0);
                requestWarningListBuyLevel(0);
                break;
            case R.id.num_news_tv:
                setBellStatus(-1);
                requestWarningListBuyLevel(-1);
                break;

        }
    }

    /**
     * 设置铃铛的显示状态 -1 未选 2 红色 1 橙色 0蓝色
     * @param status
     */
    private void setBellStatus(int status) {
        switch (status) {
            case 0:
                bellRedIv.setImageResource(R.mipmap.waring_bell_red_uncheck);//红色级别
                bellOrangeIv.setImageResource(R.mipmap.waring_bell_orange_uncheck);//橙色级别
                bellBlueIv.setImageResource(R.mipmap.waring_bell_blue_checked);//蓝色级别
                break;
            case 1:
                bellRedIv.setImageResource(R.mipmap.waring_bell_red_uncheck);//红色级别
                bellOrangeIv.setImageResource(R.mipmap.waring_bell_orange_checked);//橙色级别
                bellBlueIv.setImageResource(R.mipmap.waring_bell_blue_uncheck);//蓝色级别
                break;
            case 2:
                bellRedIv.setImageResource(R.mipmap.waring_bell_red_checked);//红色级别
                bellOrangeIv.setImageResource(R.mipmap.waring_bell_orange_uncheck);//橙色级别
                bellBlueIv.setImageResource(R.mipmap.waring_bell_blue_uncheck);//蓝色级别
                break;
            case -1:
                bellRedIv.setImageResource(R.mipmap.waring_bell_red_uncheck);//红色级别
                bellOrangeIv.setImageResource(R.mipmap.waring_bell_orange_uncheck);//橙色级别
                bellBlueIv.setImageResource(R.mipmap.waring_bell_blue_uncheck);//蓝色级别
                break;
        }
    }

    /**
     * 显示网页
     */
    private void showWebView () {
        if (View.GONE != webView.getVisibility()) return;
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
        int height = getMoveDy();
        webView.setVisibility(View.VISIBLE);
        Animation translateAnimation = new TranslateAnimation(0,0,height,0);//初始化
        translateAnimation.setInterpolator(decelerateInterpolator);
        translateAnimation.setDuration(200);  //设置动画时间
        webView.startAnimation(translateAnimation);

        barUpLl.setVisibility(View.GONE);
    }

    /**
     * 获得移动距离
     * @return
     */
    private int getMoveDy() {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        headRl.measure(w, h);
        int headRlheight = headRl.getMeasuredHeight();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        int height = screenHeight - headRlheight;
        return height;
    }
    /**
     * 隐藏网页
     */
    private void hideWebView () {
        if (View.GONE == webView.getVisibility()) return;
        int height = getMoveDy();
        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
        Animation translateAnimation = new TranslateAnimation(0,0,0,height);//初始化
        translateAnimation.setInterpolator(accelerateInterpolator);
        translateAnimation.setDuration(200);  //设置动画时间
        webView.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                webView.setVisibility(View.GONE);
                barUpLl.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 获取新闻
     * @param newsEntity
     */
    private void requestNews(NewsEntity newsEntity) {
        if (null == newsEntity) {
            return;
        }
        String url = Protocol.CPCD;
        String customer_id = UserData.getInstance().getCustomerId(this);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("customer_id", customer_id);
        String news_id = newsEntity.getNewsId();
        map.put("news_id", news_id);
        String font_size = Mysharedperferences.getIinstance().getString(this, MoreActivity.key);
        if (MyUtils.isEmpty(font_size)) {
            font_size = MoreActivity.SMALL;
        }
        map.put("font_size", font_size);
        showWaitDialog();
        doRequestString(url, map, new RequestResult() {
            @Override
            public void onResponse(String str) {
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    boolean success = jsonObject.optBoolean("success");
                    String message = jsonObject.optString("message");
                    if (success) {
                        NewsData.getInstance(WarningActivity.this).saveData(WarningActivity.this, str);
                        startNewsActivity();
                    } else {
                        showToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideWaitDialog();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
            }
        });
    }

    /**
     * 打开新闻页
     */
    private void startNewsActivity() {
        Intent intent = new Intent();
        intent.setClass(this, NewsPageActivity.class);
        startActivity(intent);
    }

    /**
     * 根据级别查询
     * @param level
     */
    private void requestWarningListBuyLevel(int level) {
        NewsListData.getInstance().clearSaveData(this);
        newsAdapter.notifyDataSetChanged();
        NewsListData.getInstance().setWarningColor(level);
        NewsListData.getInstance().setPage(-1);
        requestWarningNewList();
    }

    /**
     * 请求预警新闻列表
     */
    private void requestWarningNewList() {
        String url = Protocol.CWPCL;
        String customer_id = UserData.getInstance().getCustomerId(this);
        String function_id = NewsListData.getInstance().getFunctionId(this);
        long page = NewsListData.getInstance().getPage(this);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("customer_id", customer_id);
        map.put("ht_id", function_id);
        map.put("page", (page+1)+"");
        int warningColor = NewsListData.getInstance().getWarningColor();
        if (-1 != warningColor) {
            map.put("warning_color",warningColor+"");
        }
        showWaitDialog();
        doRequestString(url, map, new BaseActivity.RequestResult() {
            @Override
            public void onResponse(String str) {
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    boolean success = jsonObject.optBoolean("success");
                    String message = jsonObject.optString("message");
                    if (success) {

                        if (!TextUtils.isEmpty(message)) {
                            showToast(message);
                            hideWaitDialog();
                            return;
                        }

                        NewsListData.getInstance().addData(WarningActivity.this, str);
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        showToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideWaitDialog();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
            }
        });
    }


}
