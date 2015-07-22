package com.llh.base;

import org.json.JSONObject;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.llh.net.NetManager;
import com.llh.view.CustomProgress;

public abstract class BaseNetActivity extends BaseActivity {
    private NetManager netManager = null;
    public CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        netManager = NetManager.getInstance(this);
        android.util.Log.d("xcq", "this page: " + this);
        super.onCreate(savedInstanceState);
    }

    protected void reqData(String action, Bundle param) {
        dialog = CustomProgress.show(this, "正在努力加载中.....", true, null);
        netManager.reqData(action, param, responseListener, errorListener,
                this, false);
    }

    /**
     * 另一种联网方式  by ou
     * @param action
     * @param param
     * @param responseListener
     * @param errorListener
     * @param tag
     * @param isServcice
     */
    protected void reqData(String action, Bundle param,
                           Response.Listener<JSONObject> responseListener,
                           Response.ErrorListener errorListener, Object tag, boolean isServcice) {
        dialog = CustomProgress.show(this, "正在努力加载中.....", true, null);
        netManager.reqData(action, param, responseListener, errorListener,
                tag, isServcice);
    }
    /**
     * 另一种联网方式  by ou
     * @param action
     * @param param
     * @param responseListener
     * @param errorListener
     * @param tag
     * @param isServcice
     */
    protected void reqData(String action, Bundle param,
                           Response.Listener<JSONObject> responseListener,
                           Response.ErrorListener errorListener, Object tag, boolean isServcice,boolean isShowDialog) {
        if(isShowDialog) {
            dialog = CustomProgress.show(this, "正在努力加载中.....", true, null);
        }
        netManager.reqData(action, param, responseListener, errorListener,
                tag, isServcice);
    }
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            dataError(error);
        }
    };
    private Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if (dialog!=null){
                dialog.dismiss();
            }

            parserData(response);
        }
    };

    protected abstract void parserData(JSONObject response);

    protected abstract void dataError(VolleyError error);


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        netManager.cancelData(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        netManager.cancelData(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        netManager.cancelData(this);
    }

}
