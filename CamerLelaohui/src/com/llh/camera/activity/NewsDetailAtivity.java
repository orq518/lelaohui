package com.llh.camera.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ipcamer.demo.R;
import com.llh.base.BaseNetActivity;
import com.llh.entity.DeliveryAddressListModel;
import com.llh.entity.DeliveryAddressModel;
import com.llh.entity.NewsInfoListModel;
import com.llh.entity.NewsInfoModel;
import com.llh.entity.NewsListModel;
import com.llh.utils.Constant.RESPONSE_CODE;
import com.tool.Inject.ViewInject;
import com.tool.utils.LogTool;
import com.tool.utils.ToastTool;
import com.yh.materialdesign.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsDetailAtivity extends BaseNetActivity implements View.OnClickListener {
    @ViewInject(id = R.id.left_btn, click = "onClick")
    private ImageButton left_btn;


    NewsListModel model;
    TextView titlebar_text;
    NewsInfoModel newsInfoModel = new NewsInfoModel();
    TextView title, source, time, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = (NewsListModel) getIntent().getSerializableExtra("newsmodel");
    }

    @Override
    public void initView() {
        titlebar_text = (TextView) findViewById(R.id.titlebar_text);
        titlebar_text.setText("资讯详情");
        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time);
        source = (TextView) findViewById(R.id.source);
        detail = (TextView) findViewById(R.id.detail);
    }

    public void getAddress() {
        //获取默认的地址
        Bundle param = new Bundle();
        param.putString("contentID", model.contentId);

//        reqData("/data/getDefaultDeliveryAdd.json", param, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                dialog.dismiss();
//                parserAddressData(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//                dataError(error);
//            }
//        }, this, false);

        reqData("/data/nms/getNewsInfo.json", param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                parserNewsInfoData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                dataError(error);
            }
        }, this, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddress();
    }

    @Override
    public int setLayout() {
        return R.layout.news_info_layout;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_btn:
                startActivity(new Intent(this, AddAddressAtivity.class));
                break;
            case R.id.left_btn:
                finish();
                break;
        }
    }


    private void enroll() {

    }

    /**
     * 解析资讯详情
     *
     * @param response
     */
    protected void parserNewsInfoData(JSONObject response) {
        Log.d("ouou", "response:" + response);
        try {
            JSONObject obj = response.getJSONObject("result");
            String code = obj.getString("code");
//            if (!RESPONSE_CODE.SUCCESS_CODE.equals(code)) {
//                ToastTool.showText(NewsDetailAtivity.this, obj.getString("msg"));
//                return;
//            }
            if(!"1".equals(code)){
                ToastTool.showText(NewsDetailAtivity.this, obj.getString("msg"));
                return;
            }
            Gson gson = new Gson();
            NewsInfoListModel newsInfoListModel = gson.fromJson(obj.toString(),
                    NewsInfoListModel.class);
            if (newsInfoListModel != null && newsInfoListModel.rs != null) {
                newsInfoModel = newsInfoListModel.rs;
                if (newsInfoModel.title != null) {
                    title.setText(newsInfoModel.title);
                } else {
                    title.setText("");
                }
                if (newsInfoModel.source != null) {
                    source.setText("来源：" + newsInfoModel.source);
                } else {
                    source.setText("");
                }
                if (newsInfoModel.addTime != null) {
                    String timeString = newsInfoModel.addTime.trim();//long型转换成的字符串
                    Date date = new Date(Long.parseLong(timeString.trim()));
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String dateString = formatter.format(date);
                    time.setText(dateString);
                } else {
                    time.setText("");
                }
                if (newsInfoModel.content != null) {
                    detail.setText(newsInfoModel.content);
                } else {
                    detail.setText("");
                }

            } else {
                ToastTool.showText(NewsDetailAtivity.this, "获取资讯内容失败");
            }
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
}
