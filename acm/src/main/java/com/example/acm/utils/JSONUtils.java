//package com.example.acm.utils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * JSON操作工具类
// *
// */
//public class JSONUtils {
//
//    /**
//     * 将JSON串转成实体
//     *
//     * @param jsonStr 待转化JSON串
//     * @param clazz   实体类
//     * @return 指定类型的实体
//     * @version v1.0
//     */
//    public static <T> T parse2Bean(String jsonStr, Class<T> clazz) {
//        if (StringUtils.isNull(jsonStr)) {
//            return null;
//        }
//        return JSON.parseObject(jsonStr, clazz);
//    }
//
//    /**
//     * 将JSON对象转化为实体
//     *
//     * @param obj   待转化JSON对象
//     * @param clazz 实体类
//     * @return 指定类型的实体
//     * @version v1.0
//     */
//    public static <T> T parse2Bean(Object obj, Class<T> clazz) {
//        if (null != obj) {
//            String jsonStr = JSON.toJSONString(obj);
//            return JSON.parseObject(jsonStr, clazz);
//        }
//        return null;
//    }
//
//    /**
//     * 将JSON串转化为Map
//     *
//     * @param json 待转化JSON
//     * @return Map结果集
//     * @version v1.0
//     */
//    public static Map<String, Object> parseJSON2Map(Object json) {
//        if (null != json) {
//            String jsonStr = JSON.toJSONString(json);
//            return JSONObject.parseObject(jsonStr);
//        }
//        return null;
//    }
//
//    /**
//     * 将JSON串转化为Map
//     *
//     * @param json 待转化JSON
//     * @return Map结果集
//     * @version v1.0
//     */
//    public static Map<String, Object> parseJSON2Map(String json) {
//        if (!StringUtils.isNull(json)) {
//            return JSONObject.parseObject(json);
//        }
//        return null;
//    }
//
//    /**
//     * 将JSON对象数组转化为指定类型的数组
//     *
//     * @param objs  JSON对象数组
//     * @param clazz 指定类型
//     * @return 转化对象数组
//     * @version v1.0
//     */
//    public static <T> List<T> parseJSON2List(Object objs, Class<T> clazz) {
//        if (null != objs) {
//            List<T> objArray = JSONArray.parseArray(objs.toString(), clazz);
//            return objArray;
//        }
//        return null;
//    }
//
//    /**
//     * 将JSON对象数组转化为指定类型的数组
//     *
//     * @param objs  JSON对象数组
//     * @param clazz 指定类型
//     * @return 转化对象数组
//     * @version v1.0
//     */
//    public static <T> List<T> parseJSON2List(String objs, Class<T> clazz) {
//        if (!StringUtils.isNull(objs)) {
//            List<T> objArray = JSONArray.parseArray(objs, clazz);
//            return objArray;
//        }
//        return null;
//    }
//
//    /**
//     * 转换为List的map信息
//     *
//     * @param json 待转化JSON
//     * @return list
//     * @version v1.0
//     */
//    public static List<Map<String, Object>> parseJSON2MapList(Object json) {
//        if (null != json) {
//            String jsonStr = JSON.toJSONString(json);
//            return JSON.parseObject(jsonStr,
//                    new TypeReference<List<Map<String, Object>>>() {
//                    });
//        }
//        return null;
//    }
//
//    /**
//     * 转换为List的map信息
//     *
//     * @param json 待转化JSON
//     * @return list
//     * @version v1.0
//     */
//    public static List<Map<String, Object>> parseJSON2MapList(String json) {
//        if (!StringUtils.isNull(json)) {
//            return JSON.parseObject(json,
//                    new TypeReference<List<Map<String, Object>>>() {
//                    });
//        }
//        return null;
//    }
//
//    /**
//     * 转换为任何格式的信息
//     *
//     * @param json          待转化JSON
//     * @param typeReference 任意格式 new TypeReference<T>() {}
//     * @return 任意格式
//     * @version v1.0
//     */
//    public static <T> T parse2Any(Object json,
//                                  TypeReference<T> typeReference) {
//        if (null != json) {
//            String jsonStr = JSON.toJSONString(json);
//            return JSON.parseObject(jsonStr, typeReference);
//        }
//        return null;
//    }
//
//    /**
//     * 转换为任何格式的信息
//     *
//     * @param json          待转化JSON
//     * @param typeReference 任意格式 new TypeReference<T>() {}
//     * @return 任意格式
//     * @version v1.0
//     */
//    public static <T> T parse2Any(String json,
//                                  TypeReference<T> typeReference) {
//        if (!StringUtils.isNull(json)) {
//            return JSON.parseObject(json, typeReference);
//        }
//        return null;
//    }
//
//    /**
//     * 获取指定对象的JSON串
//     *
//     * @param obj
//     * @return
//     * @version v1.0
//     */
//    public static String getJSON(Object obj) {
//        if (null != obj) {
//            return JSON.toJSONString(obj);
//        }
//        return null;
//    }
//
//    public static void main(String[] args) {
//        /*TestA a = new TestA();
//        a.setId("1");
//        a.setName("a:name");
//        String jsonStr = getJSON(a);
//        System.out.println(jsonStr);
//        String jsonStr1 = getJSON(null);
//        System.out.println(jsonStr1);
//        TestA aa = parse2Bean(jsonStr, TestA.class);
//        System.out.println("aa:" + aa.getId() + "/" + aa.getName());
//        TestA aa1 = parse2Bean(null, TestA.class);
//        //        System.out.println("aa1:" + aa1.getId() + "/" + aa1.getName());
//        TestA aaa = parse2Bean(aa, TestA.class);
//        System.out.println("aaa:" + aaa.getId() + "/" + aaa.getName());
//        Map<String, Object> aMap = parseJSON2Map(jsonStr);
//        System.out.println("aMap:" + aMap.get("id") + "/" + aMap.get("name"));
//        List<TestA> aList = new ArrayList<TestA>();
//        aList.add(aa);
//        aList.add(aaa);
//        String aListStr = getJSON(aList);
//        List<TestA> list = parseJSON2List(aListStr, TestA.class);
//        for (TestA testA : list) {
//            System.out
//                    .println("testA:" + testA.getId() + "/" + testA.getName());
//        }*/
//        /*List<Map<String, Object>> b = new ArrayList<Map<String, Object>>();
//        Map<String, Object> a = new HashMap<String, Object>();
//        a.put("a", 1);
//        a.put("b", 2);
//        b.add(a);
//        String c = getJSON(b);
//        List<Map<String, Object>> d = parseJSON2MapList(c);
//        System.out.println(d.get(0).get("a"));*/
//    }
//}
//
///*class TestA {
//    private String id;
//    private String name;
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//}*/
