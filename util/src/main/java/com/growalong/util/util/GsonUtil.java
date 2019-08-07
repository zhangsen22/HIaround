package com.growalong.util.util;

import com.google.gson.Gson;

/**
 * Created by yz on 2018/12/19 7:06 PM
 * Describe:
 */
public class GsonUtil {

    private Gson mGson;

    private static GsonUtil sInstance;

    private GsonUtil() {
        mGson = new Gson();
    }

    public static GsonUtil getInstance() {
        if (sInstance == null) {
            sInstance = new GsonUtil();
        }
        return sInstance;
    }

    public Gson getGson() {

        return mGson;
    }
    /**
     * @param <T>
     * @param jsonStr json字符串
     * @param cls     需要转换成的类
     * @return
     * @Title: getServerBean
     * @Description: 将一个json字符串转换成对象
     */
    public <T> T getServerBean(String jsonStr, Class<T> cls) {
        T obj = null;
        try {
            obj = mGson.fromJson(jsonStr, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * Object 转换成 json字符串
     * @param obj
     * @return
     */
    public String objTojson(Object obj){
        String json = mGson.toJson(obj);
        return json;
    }

}
