package com.growalong.util.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者 : Created by zhangsen on 2019/4/29
 * 邮箱 : zhangsen839705693@163.com
 */
public class StringUtils {

    public static List<String> byteToList(String[] array){
        List<String> resultList = new ArrayList<>(array.length);
        Collections.addAll(resultList,array);
        return resultList;
    }


    /**
     *  字符串去除换行、空格
     * @return
     */
    public static String replaceLineEmpty(String nets){
            /* 去除      \n 回车(\u000a) \t 水平制表符(\u0009) \s 空格(\u0008)\r 换行(\u000d)*/
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(nets);
           return m.replaceAll("");
    }

    /**
     *  正则截取url中的域名
     * @return
     */
    public static String submitDomain(String nets){
        Pattern p = Pattern.compile("https?://([^:/]+)");
        Matcher m = p.matcher(nets);
        if (!m.find()) {
            return null;
        }
        return m.group(1);
    }

    public static void main(String[] args) throws Exception {
        List<String> list = new ArrayList<>();
        list.clear();
        list.add("http://www.myhost.com:8080/def");
        list.add("http://www.myhost.com/def");
        list.add("https://www.myhost.com/def");
        list.add("https://www.myhost.com:8080/def");
        for (int i = 0; i < list.size(); i++) {
                String s = StringUtils.submitDomain(list.get(i));
                if (s != null)
                    System.err.println(s);
        }
    }
}
