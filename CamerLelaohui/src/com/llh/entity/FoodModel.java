package com.llh.entity;

public class FoodModel {
	/**
	 * 产品类别id
	 */
	public String cateId;
	/**
	 * 产品类别    主食、粥、凉菜 
	 */
	public String cateName;//	:	汤/粥
	/**
	* 产品id
	*/
	public String proId;//	3640e5ae-ed91-478f-9666-cee3e1eea160
	/**
	 * 产品名称
	 */
	public String proName;//	:	红薯粥
	/**
	 * 供应商id
	 */
	public String supplierId;//	:	94a198c3-6209-440e-9d0f-0b6dd2fb4721
	/**
	 * 产品价格		
	 */
	public String proPrice;//	:	1.5
	/**
	 * 产品图片	
	 */
	public String proPic;//	:	/images/diet/honhshuzhou.jpg
	/**
	 * 餐饮标记	早中晚
	 */
	public String mealTime;//	:	1
	/**
	 * （0单品1，套餐）		
	 */
	public String mealType;//	:	0
	/**
	 * 描述信息
	 */
	public String remark;//	:	营养丰富

	/**
	 * 购买数量
	 */
	public int buyNum;

}
