package com.llh.utils;

public class Constant {
	public static final class REQ_ACTION {
		public static final String INFORMATION_LIST = "/data/getConsultListTop.json";
		public static final String INFO_CONTENT= "/data/showContent.json";
		public static final String BIND_CAMERA = "/data/bindCamera.json";
		public static final String USER_BALANCE = "/data/getCardValueByUserId.json";
		public static final String SERVICE_LIST = "/data/getCameraPackageList.json";
		
		
		

		public static final String MAGCARD_LOGON_ACTION ="/data/loginByCard.json";
		public static final String LOGON_ACTION = "/data/chklogin.json";
		/**
		 * 获取产品、服务或者产品的类别(根据参数的不同，返回不同)
		 */ 
		public static final String CATEGOR_ACTION = "/data/getCate.json";
		/**
		 * 据类别查询其中的服务或产品(两者通用)
		 */
		public static final String PROBYCATEID_ACTION = "/data/getProByCateId.json";
		/**
		 * 获取食物list
		 */
		public static final String GETDIETBYCATEID_ACTION = "/data/getDietByCateId.json";
		/**
		 * 订餐
		 */
		public static final String FOOT_ACTION = "/data/getDietCategory.json";
		/**
		 * 为产品下单
		 */
		public static final String PLACEPROORDER_ACTION = "/data/placeProOrder.json";
		/**
		 * 为食品下单
		 */
		public static final String FOOT_ORDER_ACTION = "/data/placeDiet.json";
		/**
		 * 查询食品的订单
		 */
		public static final String QUERY_FOOT_ORDER_ACTION = "/data/orderedDiet.json";
		public static final String QUERY_SHOP_ORDER_ACTION = "/data/orderedProduct.json";
	

	}
	public static final class RESPONSE_CODE {
		/**
		 * 失败
		 */
		public static final String FAIL_CODE = "0";
		
		public static final String ERORR_CODE = "1";
		/**
		 * 成功
		 */
		public static final String SUCCESS_CODE = "2";
	}
	public static final class CACHE_KEY {
		/**
		 * �̳�����
		 */
		public static final String PRODUCT_TYPE_KEY = "PRODUCT_TYPE_KEY";
		/**
		 *  ��������
		 */
		public static final String SERVICE_TYPE_KEY = "SERVICE_TYPE_KEY";
		/**
		 * 明天
		 */
		public static final String FOOT_TOMORROW_KEY = "FOOT_TOMORROW_KEY";
		/**
		 * 后天
		 */
		public static final String FOOT_AFTERTOMORROW_KEY = "FOOT_AFTERTOMORROW_KEY";
		public static final String FOOT_TODAY_KEY = "FOOT_TODAY_KEY";
		/**
		 * 成功
		 */
		public static final String SUCCESS_CODE = "2";
	}
}
