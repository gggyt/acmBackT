package com.example.acm.controller;

import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.common.SysConst;
import com.example.acm.entity.User;
import com.example.acm.service.deal.LectureDealService;
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
 * Created by ggg on 2019/3/11.
 */
@Controller
@RequestMapping("/lecture")
public class LectureController  extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private LectureDealService lectureDealService;

    @RequestMapping("/addLecture")
    @ResponseBody
    public ResultBean addLecture(@RequestParam(value = "lectureTitle", required = true) String lectureTitle,
                                 @RequestParam(value = "lectureBody", required = true) String lectureBody,
                                 @RequestParam(value = "date", required = true) String date,
                                 HttpServletRequest request, HttpServletResponse response) {
        try{
            User user = getUserIdFromSession(request);
            if (user == null) {
                //return new ResultBean(ResultCode.SESSION_OUT);
                user = new User();
                user.setUserId(2);
            }

            return lectureDealService.addLecture(user, lectureTitle, lectureBody, date);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("/updateLecture")
    @ResponseBody
    public ResultBean updateLecture(@RequestParam(value = "lectureId", required = true) long lectureId,
                                    @RequestParam(value = "lectureTitle", required = true) String lectureTitle,
                                    @RequestParam(value = "lectureBody", required = true) String lectureBody,
                                    @RequestParam(value = "date", required = true) String date,
                                    HttpServletRequest request, HttpServletResponse response) {
        try{
            User user = getUserIdFromSession(request);
            if (user == null) {
                //return new ResultBean(ResultCode.SESSION_OUT);
                user = new User();
                user.setUserId(2);
            }

            return lectureDealService.updateLecture(user, lectureId, lectureTitle, lectureBody,  date);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("/selectLecture")
    @ResponseBody
    public ResultBean selectLecture(@RequestParam(value="lectureTitle", defaultValue = "",required = false) String lectureTitle,
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

            return lectureDealService.selectLecture(user, lectureTitle, aOrs, pageNum, order, pageSize);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL, "系统异常");
        }
    }

    @RequestMapping("/selectUserLecture")
    @ResponseBody
    public ResultBean selectUserLecture(@RequestParam(value="userId", defaultValue = "-1",required = false) int userId,
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

            return lectureDealService.selectUserLecture(user, userId, aOrs, pageNum, order, pageSize);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL, "系统异常");
        }
    }

    @RequestMapping("/personLecture")
    @ResponseBody
    public ResultBean personLecture(@RequestParam(value="lectureId", defaultValue = "",required = false) long lectureId,
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

            return lectureDealService.selectLecturePerson(user, lectureId, aOrs, pageNum, order, pageSize);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL, "系统异常");
        }
    }

    @RequestMapping("/lectureDetail")
    @ResponseBody
    public ResultBean lectureDetail(@RequestParam(value="lectureId", defaultValue = "",required = false) long lectureId,
                                    HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = getUserIdFromSession(request);
            if (user == null) {
                //return new ResultBean(ResultCode.SESSION_OUT);
                user = new User();
                user.setUserId(2);
            }

            return lectureDealService.lectureDetail(user, lectureId);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL, "系统异常");
        }
    }

    @RequestMapping("/applyLecture")
    @ResponseBody
    public ResultBean applyLecture(@RequestParam(value="lectureId", defaultValue = "",required = false) long lectureId,
                                    HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = getUserIdFromSession(request);
            if (user == null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }

            return lectureDealService.applyLecture(user, lectureId);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL, "系统异常");
        }
    }

    @RequestMapping("/doneLecture")
    @ResponseBody
    public ResultBean doneCompetition(@RequestParam(value = "lectureId", required = true) long lectureId,
                                      HttpServletRequest request, HttpServletResponse response) {
        try{
            User user = getUserIdFromSession(request);
            if (user == null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }
            if (user.getAuth()< SysConst.ADMIN) {
                return new ResultBean(ResultCode.USER_NOT_ADMIN);
            }
            return lectureDealService.doneLecture(user, lectureId);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("/deleteLecture")
    @ResponseBody
    public ResultBean deleteLecture(@RequestParam(value = "lectureId", required = true) long lectureId,
                                      HttpServletRequest request, HttpServletResponse response) {
        try{
            User user = getUserIdFromSession(request);
            if (user == null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }
            if (user.getAuth()< SysConst.ADMIN) {
                return new ResultBean(ResultCode.USER_NOT_ADMIN);
            }
            return lectureDealService.deleteLecture(user, lectureId);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("/applyOrNot")
    @ResponseBody
    public ResultBean applyOrNot(@RequestParam(value="lectureId", defaultValue = "",required = false) long lectureId,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = getUserIdFromSession(request);
            if (user == null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }

            return lectureDealService.applyOrNot(user, lectureId);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL, "系统异常");
        }
    }
}
