package com.example.acm.controller;

import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.entity.User;
import com.example.acm.service.deal.ImpressionDealService;
import com.example.acm.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yxk on 2019/4/11.
 */
@Controller
@RequestMapping("/impression")
public class ImpressionController extends BaseController{

    @Autowired
    private ImpressionDealService impressionDealService;

    @RequestMapping("/addImpression")
    @ResponseBody
    public ResultBean addImpression(@RequestParam(value = "userId", required = true) int userId,
                                    @RequestParam(value = "impressionTitle", required = true) String impressionTitle,
                                    HttpServletRequest request, HttpServletResponse response) {
        User user = getUserIdFromSession(request);
        if (user == null) {
            //return new ResultBean(ResultCode.SESSION_OUT);
            user = new User();
            user.setUserId(2);
        }

        if (StringUtils.isNull(impressionTitle)) {
            return new ResultBean(ResultCode.PARAM_ERROR, "印象不能为空");
        }

        return impressionDealService.addImpression(userId, impressionTitle, user);
    }

    @RequestMapping("/deleteImpression")
    @ResponseBody
    public ResultBean deleteImpression(@RequestParam(value = "impressionId", required = true) long impressionId,
                                    HttpServletRequest request, HttpServletResponse response) {
        User user = getUserIdFromSession(request);
        if (user == null) {
            //return new ResultBean(ResultCode.SESSION_OUT);
            user = new User();
            user.setUserId(2);
        }

        return impressionDealService.deleteImpression(impressionId, user);
    }

    @RequestMapping("/selectUserImpression")
    @ResponseBody
    public ResultBean selectUserImpression(@RequestParam(value = "username", defaultValue = "", required = false) String uxername,
                                           @RequestParam(value="aOrs", defaultValue = "1", required = false) int aOrs,
                                           @RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNum,
                                           @RequestParam(value="order", defaultValue = "createDay", required = false) String order,
                                           @RequestParam(value="pageSize", defaultValue="10", required = false) int pageSize,
                                       HttpServletRequest request, HttpServletResponse response) {
        User user = getUserIdFromSession(request);
        if (user == null) {
            //return new ResultBean(ResultCode.SESSION_OUT);
            user = new User();
            user.setUserId(2);
        }

        return impressionDealService.selectImpression(user, uxername, aOrs, order, pageNum, pageSize);
    }

    @RequestMapping("/userImpression")
    @ResponseBody
    public ResultBean userImpression(@RequestParam(value = "userId", defaultValue = "-1", required = false) int userId,
                                       HttpServletRequest request, HttpServletResponse response) {
        User user = getUserIdFromSession(request);
        if (user == null) {
            //return new ResultBean(ResultCode.SESSION_OUT);
            user = new User();
            user.setUserId(2);
        }

        return impressionDealService.userImpression(userId, user);
    }

    @RequestMapping("/agreeImpression")
    @ResponseBody
    public ResultBean agreeImpression(@RequestParam(value = "impressionId", required = true) long impressionId,
                                       HttpServletRequest request, HttpServletResponse response) {
        User user = getUserIdFromSession(request);
        if (user == null) {
            //return new ResultBean(ResultCode.SESSION_OUT);
            user = new User();
            user.setUserId(2);
        }

        return impressionDealService.agreeImpression(impressionId, user);
    }

    @RequestMapping("/interestImpression")
    @ResponseBody
    public ResultBean interextImpression(@RequestParam(value = "userId", defaultValue = "-1", required = false) int userId,
                                     HttpServletRequest request, HttpServletResponse response) {
        User user = getUserIdFromSession(request);
        if (user == null) {
            //return new ResultBean(ResultCode.SESSION_OUT);
            user = new User();
            user.setUserId(2);
        }

        return impressionDealService.interestImpression(userId, user);
    }
}
