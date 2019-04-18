package com.example.acm.controller;

import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.common.SysConst;
import com.example.acm.entity.User;
import com.example.acm.service.deal.CompetitionDealService;
import com.example.acm.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xgg on 2019/2/18.
 */
@Controller
@RequestMapping("/competition")
public class CompetitionController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(CompetitionController.class);

    @Autowired
    private CompetitionDealService competitionDealService;

    @RequestMapping("addCompetition")
    @ResponseBody
    public ResultBean addCompetition(@RequestParam(value = "competitionTitle", required = true) String competitionTitle,
                                     @RequestParam(value = "competitionBody", required = true) String competitionBody,
                                     HttpServletRequest request, HttpServletResponse response) {
        try{
            User user = getUserIdFromSession(request);
            if (user == null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }
            if (StringUtils.isNull(competitionTitle)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "比赛名称不为空");
            }
            if (competitionTitle.length()>100) {
                return new ResultBean(ResultCode.PARAM_ERROR, "比赛长度不超过50");
            }
            if (StringUtils.isNull(competitionBody)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "比赛内容不为空");
            }
            return competitionDealService.addCompetition(user, competitionTitle, competitionBody);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("updateCompetition")
    @ResponseBody
    public ResultBean updateCompetition(@RequestParam(value = "competitionId", required = true) long competition,
                                     @RequestParam(value = "competitionTitle", required = true) String competitionTitle,
                                     @RequestParam(value = "competitionBody", required = true) String competitionBody,
                                     HttpServletRequest request, HttpServletResponse response) {
        try{
            User user = getUserIdFromSession(request);
            if (user == null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }
            if (StringUtils.isNull(competitionTitle)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "比赛名称不为空");
            }
            if (competitionTitle.length()>100) {
                return new ResultBean(ResultCode.PARAM_ERROR, "比赛长度不超过50");
            }
            if (StringUtils.isNull(competitionBody)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "比赛内容不为空");
            }
            return competitionDealService.updateCompetition(user, competition, competitionTitle, competitionBody);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("/selectCompetition")
    @ResponseBody
    public ResultBean selectCompetition(@RequestParam(value = "competitionTitle", defaultValue = "", required = false) String competitionTitle,
                                     @RequestParam(value="aOrs", defaultValue = "1", required = false) int aOrs,
                                     @RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNum,
                                     @RequestParam(value="order", defaultValue = "createDate", required = false) String order,
                                     @RequestParam(value="pageSize", defaultValue="10", required = false) int pageSize,
                                     HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = getUserIdFromSession(request);
            if (user == null) {
                //return new ResultBean(ResultCode.SESSION_OUT);
                user = new User();
                user.setUserId(2);
            }

            return competitionDealService.selectCompetition(competitionTitle, aOrs,  order,  pageNum,  pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("deleteCompetition")
    @ResponseBody
    public ResultBean deleteCompetition(@RequestParam(value = "competitionId", required = true) long competition,
                                        HttpServletRequest request, HttpServletResponse response) {
        try{
            User user = getUserIdFromSession(request);
            if (user == null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }
            if (user.getAuth()<SysConst.ADMIN) {
                return new ResultBean(ResultCode.USER_NOT_ADMIN);
            }
            return competitionDealService.deleteCompetition(user, competition);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("doneCompetition")
    @ResponseBody
    public ResultBean doneCompetition(@RequestParam(value = "competitionId", required = true) long competition,
                                        HttpServletRequest request, HttpServletResponse response) {
        try{
            User user = getUserIdFromSession(request);
            if (user == null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }
            if (user.getAuth()<SysConst.ADMIN) {
                return new ResultBean(ResultCode.USER_NOT_ADMIN);
            }
            return competitionDealService.doneCompetition(user, competition);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("detailCompetition")
    @ResponseBody
    public ResultBean detailCompetition(@RequestParam(value = "competitionId", required = true) long competition,
                                        HttpServletRequest request, HttpServletResponse response) {
        try{
            User user = getUserIdFromSession(request);
            if (user == null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }
            return competitionDealService.detailCompetition(user, competition);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("joinCompetition")
    @ResponseBody
    public ResultBean joinCompetition(@RequestParam(value = "competitionId", required = true) long competition,
                                        HttpServletRequest request, HttpServletResponse response) {
        try{
            User user = getUserIdFromSession(request);
            if (user == null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }
            if (user.getAuth()<SysConst.ADMIN) {
                return new ResultBean(ResultCode.USER_NOT_ADMIN);
            }
            return competitionDealService.joinCompetition(user, competition);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("personCompetition")
    @ResponseBody
    public ResultBean personCompetition(@RequestParam(value = "competitionId", required = true) long competition,
                                        @RequestParam(value="aOrs", defaultValue = "1", required = false) int aOrs,
                                        @RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNum,
                                        @RequestParam(value="order", defaultValue = "createDate", required = false) String order,
                                        @RequestParam(value="pageSize", defaultValue="10", required = false) int pageSize,
                                      HttpServletRequest request, HttpServletResponse response) {
        try{
            User user = getUserIdFromSession(request);
            if (user == null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }
            if (user.getAuth()<SysConst.ADMIN) {
                return new ResultBean(ResultCode.USER_NOT_ADMIN);
            }
            return competitionDealService.personCompetition(user, competition, aOrs, order, pageNum, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("userCompetition")
    @ResponseBody
    public ResultBean userCompetition(@RequestParam(value = "userId",defaultValue = "-1", required = false) int userId,
                                        @RequestParam(value="aOrs", defaultValue = "1", required = false) int aOrs,
                                        @RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNum,
                                        @RequestParam(value="order", defaultValue = "createDate", required = false) String order,
                                        @RequestParam(value="pageSize", defaultValue="10", required = false) int pageSize,
                                        HttpServletRequest request, HttpServletResponse response) {
        try{
            User user = getUserIdFromSession(request);
            if (user == null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }
            return competitionDealService.userCompetition(user, userId, aOrs, order, pageNum, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }
}
