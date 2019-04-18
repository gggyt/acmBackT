package com.example.acm.controller;

import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.common.SysConst;
import com.example.acm.entity.DayDuty;
import com.example.acm.entity.User;
import com.example.acm.service.*;
import com.example.acm.view.ChartView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by xgg on 2019/4/9.
 */

@Controller
@RequestMapping("/info")
public class InfoController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private DayDutyService dayDutyService;


    @RequestMapping("/getIndexInfo")
    @ResponseBody
    public ResultBean getIndexInfo(HttpServletRequest request, HttpServletResponse response) {
        User user = getUserIdFromSession(request);
        if (user == null) {
            // return new ResultBean(ResultCode.SESSION_OUT);
            user = new User();
            user.setUserId(2);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("isEffective", SysConst.LIVE);

        int allUserNum = userService.countUserMapListByQuery(map);


        Date today = new Date();
        Calendar c=Calendar.getInstance();
        c.setTime(today);
        int weekday=c.get(Calendar.DAY_OF_WEEK);
        weekday = (weekday+7-1)%7;
        if (weekday==0) {
            weekday=7;
        }
        List<DayDuty> dayDuties = dayDutyService.findDayDutyListByDayDutyId(weekday);


        String nowDuty =dayDuties.get(0).getDutyUserNames();

        map.put("allUserNum", allUserNum);
        map.put("nowDuty", nowDuty);

        return new ResultBean(ResultCode.SUCCESS, map);
    }


}
