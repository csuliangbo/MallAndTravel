package com.ych.mall.model;

import android.util.Log;

import com.ych.mall.utils.UserCenter;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ych on 2016/9/5.
 */
public class MallAndTravelModel {
    static String urlGoods = Http.SERVER_URL + "goods.php?action=";
    static String urlTravel = Http.SERVER_URL + "tour.php?action=";
    static String home = Http.SERVER_URL + "home.php?action=";

    public static void searchResult(StringCallback callback, int page) {
        Map<String, String> p = new HashMap<String, String>();
        HttpModel hp = HttpModel.newInstance("");

        callback.onResponse("ok", 0);


        //hp.post(p, callback);
    }

    public static void searchTravelResult(StringCallback callback, int page) {
        Map<String, String> p = new HashMap<String, String>();
        HttpModel hp = HttpModel.newInstance("");

        callback.onResponse("ok", 0);

    }

    static String LEFT_SORT = urlGoods + "top_category";


    //一级分类
    public static void sortLeft(StringCallback callback) {
        HashMap<String, String> map = new HashMap<>();
        HttpModel.newInstance(LEFT_SORT).post(map, callback);
    }

    static String LEFT_SORT_TRAVEL = urlTravel + "tour_top_category";


    //一级分类(旅游)
    public static void sortLeftTravel(StringCallback callback) {
        HashMap<String, String> map = new HashMap<>();
        HttpModel.newInstance(LEFT_SORT_TRAVEL).post(map, callback);
    }

    static String RIGHT_SORT = urlGoods + "two_level_class";


    //二级分类
    public static void sortRight(StringCallback callback, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        HttpModel.newInstance(RIGHT_SORT).post(map, callback);
    }

    static String RIGHT_SORT_TRAVEL = urlTravel + "tour_two_level";

    //二级分类(旅游)
    public static void sortRightTravel(StringCallback callback, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        HttpModel.newInstance(RIGHT_SORT_TRAVEL).post(map, callback);
    }

    static String GOODS_LIST = urlGoods + "goods_list";

    //商品列表
    public static void goodsList(StringCallback callback, int page, String id) {
        HashMap<String, String> map = new HashMap<>();

        HttpModel.newInstance(GOODS_LIST + "&id=" + id + "&page=" + (page + 1)).post(map, callback);
    }

    static String TRAVEL_LIST = urlTravel + "goods_list";

    //旅游列表
    public static void travelList(StringCallback callback, int page, String id) {
        HashMap<String, String> map = new HashMap<>();
        HttpModel.newInstance(TRAVEL_LIST + "&id=" + id + "&page=" + (page + 1)).post(map, callback);
    }

    static String SHOPCAR_URL = urlGoods + "display_goods_cart";

    //购物车列表
    public static void shopCarList(StringCallback callback, int page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());
        map.put("page", (page + 1) + "");
        HttpModel.newInstance(SHOPCAR_URL).post(map, callback);
    }

    static String SHOPCAR_DEL = urlGoods + "del_goods_cart";

    //购物车删除
    public static void shopCarDel(StringCallback callback, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("cart_id", id);
        HttpModel.newInstance(SHOPCAR_DEL).post(map, callback);
    }

    static String SHOPCAR_ADD = urlGoods + "add_num";
    static String SHOPCAR_CUT = urlGoods + "reduce_num";

    //增删购物车数量
    public static void shopCarNum(StringCallback callback, int type, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("cart_id", id);
        map.put("goods_num", "1");
        switch (type) {
            case 0:
                HttpModel.newInstance(SHOPCAR_ADD).post(map, callback);
                break;
            case 1:
                HttpModel.newInstance(SHOPCAR_CUT).post(map, callback);
                break;
        }
    }

    static String GOODS_DETAIL = urlGoods + "goods_details";

    //商品详情
    public static void goodsDetail(StringCallback callback, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());
        HttpModel.newInstance(GOODS_DETAIL).post(map, callback);
    }

    //955
    static String ADD_SHOPCAR = urlGoods + "add_goods_cart";

    public static void addShopCar(StringCallback callback, String id, String title) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());
        if (title != null)
            map.put("taocan", title);
        HttpModel.newInstance(GOODS_DETAIL).post(map, callback);
    }

    static String GOODS_SEARCH = home + "goods_search";

    //商品搜索
    public static void goodsSearch(StringCallback callback, String title, int page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("Page", (page + 1) + "");
        HttpModel.newInstance(GOODS_SEARCH).post(map, callback);
    }
}



