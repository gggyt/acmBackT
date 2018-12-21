package com.example.acm.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.lettuce.core.dynamic.annotation.Value;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ImageController {
    private ResourceLoader resourceLoader;

    @RequestMapping("/show")
    public ResponseEntity showPhotos(String fileName){
        System.out.println("-----------");

        try {
            System.out.println("-----------");
            String filePath = "E://pic//"; // 上传后的路径
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            System.out.println("-----------");
            return ResponseEntity.ok(resourceLoader.getResource("file:" + filePath + fileName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    @ResponseBody
    public Object   uploadImg(@RequestParam(value = "myFileName", required = false) MultipartFile file,
                          HttpServletRequest request, HttpServletResponse response) throws Exception{
        System.out.println("*************接收上传文件*************");
        if (file.isEmpty()) {
            System.out.println("文件为空空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名

        String filePath = "E://pic/"; // 上传后的路径
         fileName = UUID.randomUUID() + suffixName; // 新文件名 File dest = new File(filePath + fileName);
        File dest = new File(filePath + fileName);
        System.out.println(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        System.out.println("*************接收上传文件结束*************");

        Map<String, Object> map = new HashMap<>();
        map.put("errno", 0);
        map.put("data", "http://localhost:9999/image/"+fileName);
        String jo = JSONArray.toJSONString(map);
        return jo;
    }
}

