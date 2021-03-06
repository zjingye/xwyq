package cc.horizoom.ssl.xwyq.MainNewsPage;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.FunctionListData;
import cc.horizoom.ssl.xwyq.DataManager.entity.FunctionEntity;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.popupWindow.MyPopupWindow;

/**
 * Created by pizhuang on 2015/9/6.
 * 更能分类选择窗口
 */
public class FunctionListPopUpWindow extends MyPopupWindow {

    private ListView myList;

    private FunctionListAdapter functionListAdapter;//适配器

    public FunctionListPopUpWindow(BaseActivity baseActivity) {
        super(baseActivity);
    }

    @Override
    public View getContentView() {
        ArrayList<FunctionEntity> data = FunctionListData.getInstance().getData(baseActivity);

        LayoutInflater layoutInflater = LayoutInflater.from(baseActivity);
        View contentView = layoutInflater.inflate(R.layout.list_function,null);
        myList = (ListView) contentView.findViewById(R.id.functionList);
        functionListAdapter = new FunctionListAdapter(data);
        myList.setAdapter(functionListAdapter);
        return contentView;
    }

    /**
     * 设置监听器
     * @param onItemClickListener
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        myList.setOnItemClickListener(onItemClickListener);
    }

    class FunctionListAdapter extends BaseAdapter{

        private ArrayList<FunctionEntity> mydata;

        public FunctionListAdapter(ArrayList<FunctionEntity> data) {
            mydata = data;
        }

        @Override
        public int getCount() {
            return mydata.size();
        }

        @Override
        public Object getItem(int i) {
            return mydata.get(i);
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
                view = layoutInflater.inflate(R.layout.list_item_function,null);
                holderView.functionTv = (TextView) view.findViewById(R.id.functionTv);
                holderView.headImg = (ImageView) view.findViewById(R.id.headImg);
                view.setTag(holderView);
            } else {
                holderView = (HolderView) view.getTag();
            }
            FunctionEntity functionEntity = mydata.get(i);
            String functionId = functionEntity.getFunctionId();
            if ("37".equals(functionId) || "38".equals(functionId)) {
                holderView.headImg.setImageResource(R.mipmap.icon_list_07);
            } else {
                holderView.headImg.setImageResource(R.mipmap.icon_list_10);
            }
            holderView.functionTv.setText(functionEntity.getName());
            return view;
        }

        class HolderView {
            public ImageView headImg;
            public TextView functionTv;
        }
    }
}
