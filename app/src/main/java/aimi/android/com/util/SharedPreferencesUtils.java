package aimi.android.com.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.growalong.util.util.GALogger;

import java.lang.reflect.Type;

import aimi.android.com.MyApplication;

/**
 * 作者 : Created by zhangsen on 2019/4/29
 * 邮箱 : zhangsen839705693@163.com
 * sp工具类
 */
public class SharedPreferencesUtils {
    private static SharedPreferences sharedPreferences;
    private static String TAG = "SharedPreferencesUtils";
    private static String SP_NAME = "aimi_sp";

    /* 初始化 sp 模块
     * name: sp 文件名字,不通的进程可以通过名字来使用不通的文件来存储
     * */
    public static void init() {
        if (sharedPreferences == null) {
            synchronized (SharedPreferencesUtils.class) {
                if (sharedPreferences == null) {
                    sharedPreferences = MyApplication.appContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
                }
            }
        }
    }

    /**
     * 储存一个对象
     *
     * @param key
     * @param object
     */
    public static void putObject(String key, Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        GALogger.d(TAG, "json:  " + json);
        putString(key, json);
    }

    /**
     * 获取包含泛型对象
     *
     * @param key
     * @param type 如果你要获取 List<Person> 这个对象, type应该传入(new TypeToken<List<Person>>() {}.getType())
     * @param <T>
     * @return
     */
    public static <T> T getObject(String key, Type type) {
        String json = getString(key);
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置String类型值
     */
    public static void putString(String key, String value) {
        try {
            init();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "putString() error, key=" + key + ", value=" + value);
        }
    }

    /**
     * 设置long类型值
     */
    public static void putLong(String key, long value) {
        try {
            init();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "putLong() error, key=" + key + ", value=" + value);
        }
    }

    /**
     * 设置int类型值
     */
    public static void putInt(String key, int value) {
        try {
            init();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "putInt() error, key=" + key + ", value=" + value);
        }
    }

    /**
     * 设置Boolean类型值
     */
    public static void putBoolean(String key, boolean value) {
        try {
            init();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "putBoolean() error, key=" + key + ", value=" + value);
        }
    }

    /**
     * 设置Float类型值
     */
    public static void putFloat(String key, float value) {
        try {
            init();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "putFloat() error, key=" + key + ", value=" + value);
        }
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为""
     */
    public static String getString(String key) {
        try {
            return getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "getString() error, key=" + key);
        }
        return null;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为""
     */
    public static String getString(String key, String defaultValue) {
        try {
            init();
            return sharedPreferences.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "getString() error, key=" + key);
        }
        return null;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为false
     */
    public static boolean getBoolean(String key) {
        try {

            return getBoolean(key, false);
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "getBoolean() error, key=" + key);
        }
        return false;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为false
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        try {
            init();
            return sharedPreferences.getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "getBoolean() error, key=" + key);
        }
        return false;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     */
    public static int getInt(String key) {
        try {
            return getInt(key, 0);
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "getInt() error, key=" + key);
        }
        return -1;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     */
    public static int getInt(String key, int defaultValue) {
        try {
            init();
            return sharedPreferences.getInt(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "getInt() error, key=" + key);
        }
        return -1;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     */
    public static long getLong(String key) {
        try {
            return getLong(key, 0L);
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "getLong() error, key=" + key);
        }
        return -1;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     */
    public static long getLong(String key, Long defaultValue) {
        try {
            init();
            return sharedPreferences.getLong(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "getLong() error, key=" + key);
        }
        return -1;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     */
    public static float getFloat(String key) {
        try {
            return getFloat(key, 0f);
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "getFloat() error, key=" + key);
        }
        return 0;
    }

    /**
     * 获取key相对应的value，如果不设默认参数，默认值为0
     */
    public static float getFloat(String key, Float defaultValue) {
        try {
            init();
            return sharedPreferences.getFloat(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "getFloat() error, key=" + key);
        }
        return 0;
    }


    /**
     * 判断是否存在此字段
     */
    public static boolean has(String key) {
        try {
            init();
            return sharedPreferences.contains(key);
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "has() error, key=" + key);
        }
        return false;
    }

    /**
     * 删除sharedPreferences文件中对应的Key和value
     */
    public static boolean remove(String key) {
        try {
            init();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            return editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            GALogger.d(TAG, "remove() error, key=" + key);
        }
        return false;
    }
}
