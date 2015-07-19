package com.llh.camera.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ipcamer.app.MyApplication;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.entity.DeliveryAddressListModel;
import com.llh.entity.DeliveryAddressModel;
import com.llh.entity.FoodModel;
import com.llh.entity.NewModel;
import com.llh.entity.NewsListModel;
import com.llh.net.NetManager;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.llh.utils.ImageManager;
import com.llh.utils.OrderFoodInterface;
import com.llh.utils.utils;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class NewsAtivity extends BaseNetActivity implements View.OnClickListener {
    @ViewInject(id = R.id.left_btn, click = "onClick")
    List<NewsListModel> newlist = new ArrayList<NewsListModel>();
    NewsAdapter newsAdapter;
    ListView listView;
    TextView titlebar_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        titlebar_text = (TextView) findViewById(R.id.titlebar_text);
        titlebar_text.setText("资讯");
        listView= (ListView) findViewById(R.id.listview);
        newsAdapter=new NewsAdapter(this);
        listView.setAdapter(newsAdapter);
        getNewsList();
    }

    public void getNewsList() {


        Bundle param = new Bundle();
		param.putString("cateId", "61");
		param.putString("page", "1");
		param.putString("pageSize", "20");

        reqData("/data/nms/getNewsList.json", param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                parserNewsListData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                dataError(error);
            }
        }, this, false);
//        reqData("/data/getConsultList.json", param, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                dialog.dismiss();
//                parserNewsListData(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//                dataError(error);
//            }
//        }, this, false);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public int setLayout() {
        return R.layout.newslist_layout;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_btn:
                break;
            case R.id.left_btn:
                finish();
                break;
        }
    }


    private void enroll() {

    }

    /**
     * 获取地址列表
     *
     * @param response
     */
    protected void parserNewsListData(JSONObject response) {
        Log.d("ouou", "response:" + response);
        try {
            JSONObject obj = response.getJSONObject("result");
            String code = obj.getString("code");
//            if (!RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
//                ToastTool.showText(NewsAtivity.this, obj.getString("msg"));
//                return;
//            }
            Gson gson = new Gson();
            NewModel newlistModel = gson.fromJson(obj.toString(),
                    NewModel.class);
            Logout.d("##newlistModel.rs:"+newlistModel.rs);
            Logout.d("##newlistModel.rs:"+newlistModel.rs.size());
            newlist.clear();
            newlist.addAll(newlistModel.rs);
            newsAdapter.setData((ArrayList<NewsListModel>) newlist);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void parserData(JSONObject response) {

    }

    @Override
    protected void dataError(VolleyError error) {
        LogTool.e(error.toString());

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        //resultCode就是在B页面中返回时传的parama，可以根据需求做相应的处理
//        if (arg0 == 1100 && arg1 == RESULT_OK && arg2 != null) {
//            getAddress();
//        }
    }

    class NewsAdapter extends BaseAdapter {

        private ArrayList<NewsListModel> array = new ArrayList<NewsListModel>();
        private LayoutInflater inflater;
        Context context;

        public NewsAdapter(Context context) {
            inflater = LayoutInflater.from(MyApplication.getMyApplication());
            this.context = context;
        }

        public void setData(ArrayList<NewsListModel> data) {
            array.clear();
            array.addAll(data);
            this.notifyDataSetChanged();
            Logout.d("##data.size():" + data.size());
        }


        @Override
        public int getCount() {

            Logout.d("array.size():"+array.size());
            return array.size();
        }

        @Override
        public Object getItem(int position) {
            return array.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.news_list_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);// 绑定ViewHolder对象
                holder.title= (TextView) convertView.findViewById(R.id.title);
                holder.subTitle= (TextView) convertView.findViewById(R.id.subTitle);
            } else {
                holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
            }
            NewsListModel model=array.get(position);
            holder.title.setText(model.title);
            holder.subTitle.setText(model.subTitle);
            return convertView;
        }

        class ViewHolder {
            TextView title;
            TextView subTitle;
//            TextView food_price;
//            ImageView food_im;
//            ImageView food_add;
//            ImageView im_reduce;

        }
    }
}
