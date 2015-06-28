package com.ipcamer.app;

import com.llh.utils.ACache;

import android.app.Application;

public class MyApplication extends Application {

	private static MyApplication app;
	ACache foodcache, product, servercache;
	public static final String product_List_name = "productList_list";
	public static final String shop_List_name = "foot_cache";
	public static final String server_List_name = "server_cache";

	@Override
	public void onCreate() {
		app = this;
		super.onCreate();
//		product = ACache.get(getApplicationContext(), product_List_name);
		foodcache = ACache.get(getApplicationContext(), shop_List_name);
//		servercache = ACache.get(getApplicationContext(), server_List_name);
	}

	public static MyApplication getMyApplication() {
		return app;
	}

	public ACache getFoodListCache() {
		return foodcache;
	}

	public ACache getProductCache() {
		return product;
	}

	public ACache getServerCache() {
		return servercache;
	}
}
