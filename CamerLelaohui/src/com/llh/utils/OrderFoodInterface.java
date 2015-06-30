package com.llh.utils;

import com.llh.entity.FoodModel;

/**
 * The author ou on 2015/6/30.
 */
public interface OrderFoodInterface {
    public void callBack();
    public void refreshShoppingList(FoodModel foodModel);
}
