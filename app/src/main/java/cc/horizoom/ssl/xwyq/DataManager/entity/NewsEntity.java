package cc.horizoom.ssl.xwyq.DataManager.entity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.myframe.view.deletelistview.MessageItem;

/**
 * Created by pi on 15-9-3.
 * 新闻实体类
 */
public class NewsEntity extends MessageItem{

//    "news_id": "150870",
//            "title": "湖南临湘市长涉嫌吸毒被查 消息称其产生幻觉后报警",
//            "source": "",
//            "push_level": "5",
//            "publish_time": "0000-00-00 00:00:00",
//            "status": "0"

    private String newsId;

    private String title;

    private String source;

    private int pupushLevel;

    private String publishTime;

    private String status;

    private int likeNums;

    public NewsEntity(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            newsId = jsonObject.optString("news_id");
            title = jsonObject.optString("title");
            source = jsonObject.optString("spider_site");
            pupushLevel = jsonObject.optInt("warning_leve");
            publishTime = jsonObject.optString("publish_time");
            likeNums = jsonObject.optInt("like_nums");
            status = jsonObject.optString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getLikeNums() { return likeNums; }

    public String getNewsId() {
        return newsId;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public int getPupushLevel() {
        return pupushLevel;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getStatus() {
        return status;
    }
}
