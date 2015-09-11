package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.CardData;
import cc.horizoom.ssl.xwyq.DataManager.FunctionListData;
import cc.horizoom.ssl.xwyq.DataManager.NewsListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.DataManager.entity.CardEntity;
import cc.horizoom.ssl.xwyq.DataManager.entity.FunctionEntity;
import cc.horizoom.ssl.xwyq.MainNewsPage.view.CardsView;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.network.volley.VolleyError;

/**
 * Created by pizhuang on 2015/9/8.
 * 主页带选项卡的新闻分类列表
 */
public class MainNewsPageActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout settingRl;//设置

    private RelativeLayout leftRl;//向左按钮

    private RelativeLayout rightRl;//向右按钮

    private FrameLayout contentFl;//内容

    private ImageView settingIv;//设置

    private CardsView cardsView;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://请求分类卡片 如果数据为空则需要从新加载
                    requestCCCPCL();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news_page);
        settingRl = (RelativeLayout) findViewById(R.id.settingRl);//设置
        leftRl = (RelativeLayout) findViewById(R.id.leftRl);//向左按钮
        rightRl = (RelativeLayout) findViewById(R.id.rightRl);//向右按钮
        contentFl = (FrameLayout) findViewById(R.id.contentFl);//内容
        settingIv = (ImageView) findViewById(R.id.settingIv);//设置
        settingRl.setOnClickListener(this);
        leftRl.setOnClickListener(this);
        rightRl.setOnClickListener(this);
        ArrayList<CardEntity> data = CardData.getInstance().getCardDatas(MainNewsPageActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<CardEntity> data = CardData.getInstance().getCardDatas(MainNewsPageActivity.this);
        if (data.size() == 0) {
            myHandler.sendEmptyMessage(1);
        } else {
            updataView();
        }
    }

    /**
     * 更新页面数据
     */
    private void updataView() {
        if (cardsView == null) {
            cardsView = new CardsView(this);
            cardsView.updateView(this);
            contentFl.removeAllViews();
            contentFl.addView(cardsView);
        }
    }

    /**
     * 已登录用户卡片列表接口
     */
    private void requestCCCPCL() {
        String url = Protocol.CCCPCL;
        HashMap<String,String> map = new HashMap<String,String>();
        String customer_id = UserData.getInstance().getCustomerId(this);
        map.put("customer_id", customer_id);
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
                        CardData.getInstance().saveData(MainNewsPageActivity.this, str);
                        onRequestCCCPCLSuccess();
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
     * 请求分类卡成功回调
     */
    private void onRequestCCCPCLSuccess() {
        cardsView = null;
        updataView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settingRl:
                showSettingPopUpWindow();
                break;
            case R.id.leftRl:
                cardsView.scrollToLeft();
                break;
            case R.id.rightRl:
                cardsView.scrollToRight();
                break;
        }
    }

    /**
     * 显示设置窗口
     */
    private void showSettingPopUpWindow() {
        SettingPopUpWindow settingPopUpWindow = new SettingPopUpWindow(this);
        settingPopUpWindow.showAsDropDown(settingIv);
    }

    /**
     * 打开新闻页面
     */
    public void startLoginNewsActivity() {
        NewsListData.getInstance().clearSaveData(this);
        Intent intent = new Intent();
        intent.setClass(this,LoginNewsActivity.class);
        startActivity(intent);
    }
}