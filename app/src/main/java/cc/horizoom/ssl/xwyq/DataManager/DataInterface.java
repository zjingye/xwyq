package cc.horizoom.ssl.xwyq.DataManager;

import cn.com.myframe.BaseActivity;

/**
 * Created by pizhuang on 2015/9/6.
 */
public interface DataInterface {
    /**
     * 保存数据
     * @param baseActivity
     * @param json
     */
    public void saveData(BaseActivity baseActivity,String json);

    /**
     * 清空数据
     */
    public void clearData();

    /**
     * 解析数据
     */
    public void analyze(BaseActivity baseActivity);

    /**
     * 清除缓存数据
     * @param baseActivity
     */
    public void clearSaveData(BaseActivity baseActivity);

}
