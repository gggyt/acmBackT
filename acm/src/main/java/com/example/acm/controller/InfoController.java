package com.example.acm.controller;

import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.common.SysConst;
import com.example.acm.entity.DayDuty;
import com.example.acm.entity.Lecture;
import com.example.acm.entity.User;
import com.example.acm.service.*;
import com.example.acm.service.deal.DayDutyDealService;
import com.example.acm.view.ChartView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by ggg on 2019/4/9.
 */

@Controller
@RequestMapping("/info")
public class InfoController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private FriendurlService friendurlService;
    @Autowired
    private DayDutyService dayDutyService;
    @Autowired
    private LectureService lectureService;
    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private AnnouncementService announcementService;

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
        int allInvitationNum = invitationService.countInvitationListByQuery(map);
        int allFriendNum = friendurlService.countFriendurlListByQuery(map);

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
        map.put("allInvitationNum", allInvitationNum);
        map.put("allFriendNum", allFriendNum);
        map.put("nowDuty", nowDuty);

        return new ResultBean(ResultCode.SUCCESS, map);
    }

    @RequestMapping("selectLecComInfo")
    @ResponseBody
    public ResultBean selectLecComInfo(HttpServletRequest request, HttpServletResponse response) {
        User user = getUserIdFromSession(request);
        if (user == null) {
            // return new ResultBean(ResultCode.SESSION_OUT);
            user = new User();
            user.setUserId(2);
        }
        List<ChartView> chartViews = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("isDone", 0);
        int lecDone = lectureService.countLectureListByQuery(map);
        ChartView lecDoneC = new ChartView("已结束讲座", lecDone);
        chartViews.add(lecDoneC);
        map.put("isDone", 1);
        int lecNotDone = lectureService.countLectureListByQuery(map);
        ChartView lecNotDoneC = new ChartView("正在报名中讲座", lecNotDone);
        chartViews.add(lecNotDoneC);

        map.put("isDone", 0);
        int comDone = competitionService.countCompetitionListByQuery(map);
        ChartView comDoneC = new ChartView("已结束比赛", comDone);
        chartViews.add(comDoneC);
        map.put("isDone", 1);
        int comNotDone = competitionService.countCompetitionListByQuery(map);
        ChartView comNotDoneC = new ChartView("正在报名比赛比赛", comNotDone);
        chartViews.add(comNotDoneC);

        return new ResultBean(ResultCode.SUCCESS, chartViews);
    }

    @RequestMapping("selectNewAnnoInfo")
    @ResponseBody
    public ResultBean selectNewAnnoInfo(HttpServletRequest request, HttpServletResponse response) {
        User user = getUserIdFromSession(request);
        if (user == null) {
            // return new ResultBean(ResultCode.SESSION_OUT);
            user = new User();
            user.setUserId(2);
        }
        List<ChartView> chartViews = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("isEffective", 1);
        int lecDone = newsService.countNewsListByQuery(map);
        ChartView lecDoneC = new ChartView("新闻数", lecDone);
        chartViews.add(lecDoneC);

        int lecNotDone = announcementService.countAnnouncementListByQuery(map);
        ChartView lecNotDoneC = new ChartView("公告数", lecNotDone);
        chartViews.add(lecNotDoneC);



        return new ResultBean(ResultCode.SUCCESS, chartViews);
    }
}
