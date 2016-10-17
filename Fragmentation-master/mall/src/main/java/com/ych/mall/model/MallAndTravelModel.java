package com.ych.mall.model;

import android.text.TextUtils;
import android.util.Log;

import com.ych.mall.utils.UserCenter;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

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
    static String GOODS_ONE = urlGoods + "one_level_goods";


    //商品列表
    public static void goodsList(StringCallback callback, int page, String id, int type) {
        HashMap<String, String> map = new HashMap<>();
        //if (type == 0)
            HttpModel.newInstance(GOODS_LIST + "&id=" + id + "&page=" + (page + 1)).post(map, callback);
       // else
            //HttpModel.newInstance(GOODS_ONE + "&id=" + id + "&page=" + (page + 1)).post(map, callback);
    }

    static String TRAVEL_LIST = urlTravel + "goods_list";
    static String TRAVEL_ONE = urlTravel + "one_level_tour";

    //旅游列表
    public static void travelList(StringCallback callback, int page, String id, int type) {
        HashMap<String, String> map = new HashMap<>();
        if (type == 0)
            HttpModel.newInstance(TRAVEL_LIST + "&id=" + id + "&page=" + (page + 1)).post(map, callback);
        else
            HttpModel.newInstance(TRAVEL_ONE + "&id=" + id + "&page=" + (page + 1)).post(map, callback);
    }

    static String SHOPCAR_URL = urlGoods + "display_goods_cart";

    //购物车列表
    public static void shopCarList(StringCallback callback, int page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());

        HttpModel.newInstance(SHOPCAR_URL + "&page=" + (page + 1)).post(map, callback);
    }

    static String SHOPCAR_DEL = urlGoods + "del_goods_cart";

    //购物车删除
    public static void shopCarDel(StringCallback callback, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("cart_id", id);
        HttpModel.newInstance(SHOPCAR_DEL).post(map, callback);
    }

    static String SHOPCAR_EDIT = urlGoods + "edit_goods_num";

    //增删购物车数量
    public static void shopCarNum(StringCallback callback, String id, String num) {
        HashMap<String, String> map = new HashMap<>();
        map.put("cart_id", id);
        map.put("goods_num", num);
        HttpModel.newInstance(SHOPCAR_EDIT).post(map, callback);
    }


    static String CLEAR_SHOPCAR = urlGoods + "empty_goods_cart";

    //清空购物车
    public static void clearShopCar(StringCallback callback) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());
        HttpModel.newInstance(CLEAR_SHOPCAR).post(map, callback);
    }

    //创建商品订单
    static String CREATE_ORDER = urlGoods + "create_order";

    public static void createOrder(StringCallback callback, String price_sum, String price_shiji,
                                   String address, String realName, String mobile, String number,
                                   String jifen, String cartId, String jifenA, String jifenB) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());
        map.put("price_sum", price_sum);
        map.put("price_shij", price_shiji);
        map.put("user_address", address);
        map.put("realname", realName);
        map.put("mobile", mobile);
        map.put("num_sum", number);
        map.put("fanli_jifen", jifen);
        map.put("cart_id", cartId);
        map.put("use_jf_limit", jifenA);
        map.put("use_jf_currency", jifenB);
        HttpModel.newInstance(CREATE_ORDER).post(map, callback);
    }


    //创建旅游订单
    static String CREATE_TOUR_ORDER = urlTravel + "create_order";

    public static void createTourOrder(StringCallback callback, String price_sum, String price_shiji,
                                       String realName, String mobile, String number,
                                       String jifen, String goods_id, String jifenB, String date,
                                       String childNum, String childPrice, String adultNum, String adultPrice,
                                       String order_remarks) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());
        map.put("price_sum", price_sum);
        map.put("price_shiji", price_shiji);
        map.put("realname", realName);
        map.put("mobile", mobile);
        map.put("num_sum", number);
        map.put("fanli_jifen", jifen);
        map.put("goods_id", goods_id);
        map.put("use_jf_currency", jifenB);
        map.put("chufa_date", date);
        map.put("price_cr", adultPrice);
        map.put("cr_num", adultNum);
        map.put("price_et", childPrice);
        map.put("et_num", childNum);
        map.put("order_remarks", order_remarks);
        HttpModel.newInstance(CREATE_TOUR_ORDER).post(map, callback);
    }

    static String SETTEL_ACCOUNTS = urlGoods + "to_settle_accounts";

    //结算
    public static void settelAccounts(StringCallback callback, String cartId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());
        map.put("cart_id", cartId);
        HttpModel.newInstance(SETTEL_ACCOUNTS).post(map, callback);
    }

    static String PAY_NOW = urlGoods + "immediate_payment";

    public static void payNow(StringCallback callback, String goodsId, String num) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());
        map.put("goods_id", goodsId);
        if (!TextUtils.isEmpty(num)) {
            map.put("goods_num", num + "");
        }
        HttpModel.newInstance(PAY_NOW).post(map, callback);
    }

    static String GOODS_DETAIL = urlGoods + "goods_details";

    //商品详情
    public static void goodsDetail(StringCallback callback, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());
        HttpModel.newInstance(GOODS_DETAIL).post(map, callback);
    }

    static String GOODS_SEARCH = home + "goods_search";

    //商品搜索
    public static void goodsSearch(StringCallback callback, String title, int page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("title", title);
        HttpModel.newInstance(GOODS_SEARCH + "&page=" + (page + 1) + "&pagesize=10").post(map, callback);

    }

    static String TRAVEL_SEARCH = home + "lvyou_search";

    //旅游搜索
    public static void travelSearch(StringCallback callback, String title, int page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("title", title);
        HttpModel.newInstance(TRAVEL_SEARCH + "&page=" + (page + 1) + "&pagesize=10").post(map, callback);

    }

    static String ADD_SHOPCAR = urlGoods + "add_goods_cart";

    //添加购物车
    public static void addShopCar(StringCallback callback, String id, String title, String point, String price, String num) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());
        map.put("goods_id", id);
        if (title != null)
            map.put("taocan", title);
        else
            map.put("taocan", "");
        map.put("fanli_jifen", point);
        map.put("price_new", price);
        map.put("num", num);
        HttpModel.newInstance(ADD_SHOPCAR).post(map, callback);
    }


    static String TRAVEL_DETAIL = urlTravel + "tour_synopsis";

    //旅游详情
    public static void travelDetail(StringCallback callback, String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());
        HttpModel.newInstance(TRAVEL_DETAIL).post(map, callback);
    }

    static String TRAVEL_RESERVE = urlTravel + "tour_reserve";

    //旅游预定
    public static void travelReseve(StringCallback callback, String goodsId, String date) {
        HashMap<String, String> map = new HashMap<>();
        map.put("goods_id", goodsId);
        map.put("user_id", UserCenter.getInstance().getCurrentUserId());
        map.put("chufa_date", date);
        HttpModel.newInstance(TRAVEL_RESERVE).post(map, callback);
    }

    static String HOME_MALL_URL = home + "index";

    //商城主页
    public static void homeMall(StringCallback callback, int page) {
        HashMap<String, String> map = new HashMap<>();

        HttpModel.newInstance(HOME_MALL_URL + "&page=" + (page + 1) + "&pagesize=10").post(map, callback);
    }

    static String HOME_TRAVEL_URL = home + "tourism_index";

    //旅游主页
    public static void homeTravel(StringCallback callback, int page) {
        HashMap<String, String> map = new HashMap<>();

        HttpModel.newInstance(HOME_TRAVEL_URL + "&page=" + (page + 1) + "&pagesize=10").post(map, callback);
    }

    static String UPDATE = home + "get_version";

    //升级
    public static void update(String code, StringCallback callback) {
        HashMap<String, String> map = new HashMap<>();
        map.put("version_num", code);
        map.put("app_type", "android");
        HttpModel.newInstance(UPDATE).post(map, callback);
    }

    static String AD_TEXT = home + "gonggao";

    //公告
    public static void getAd(StringCallback callback) {
        HashMap<String, String> map = new HashMap<>();
        HttpModel.newInstance(AD_TEXT).post(map, callback);
    }

}



