package com.llh.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * The author ou on 2015/7/6.
 */
public class OrderListModel extends BaseModel {

    public String  orderCode;//	String		订单流水号
    public String  merchantId;//	String		连锁中心id
    public String  mealTime	;//Integer		餐饮标记
    public String  IsDistr;//(暂时没用)	Integer	1：送餐 0：不送餐	是否送货
    public String  phone;//(暂时没用)	String	131******91	手机号
    public String  orderAddress;//(暂时没用)	String	北京海淀黄庄	送货地址
    public String  customerName;//	String	禹力	收货人姓名
    public String  proPrice;//	double		消费金额
    public String  addTime;//	String		下单时间
    public String  mealType;//	Integer	0 单品 1 套餐 2 混合	单品/套餐
    public String  remark;//	String 		描述信息
    public String  isScope;//	int	0 今天 1 明天 2 后天	餐饮时间
    public String  cancel;//	int	1 可以取消 0不能取消 2已取消	取消状态
    public String  payStatus;//	int 	0 未支付 1 已支付	支付状态
    public ArrayList<WapDietInfoModel> wapDietInfoList	=new  ArrayList<WapDietInfoModel>();// 餐饮详情



}
