package com.example.acm.controller;

import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.entity.User;
import com.example.acm.service.deal.InvitationDealService;
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
 * Created by ggg on 2019/2/5.
 */
@Controller
@RequestMapping("/invitation")
public class InvitationController extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(InvitationController.class);

    @Autowired
    private InvitationDealService invitationDealService;

    @RequestMapping("/addInvitation")
    @ResponseBody
    public ResultBean addInvitation(@RequestParam(value="invitationTitle", required = true) String invitationTitle,
                                    @RequestParam(value="invitationBody", required = true) String invitationBody,
                                    HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = getUserIdFromSession(request);
            if (user == null) {
                //return new ResultBean(ResultCode.SESSION_OUT);
                user = new User();
                user.setUserId(2);
            }
            if (StringUtils.isNull(invitationTitle)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "标题不为空");
            }
            if (invitationTitle.length()>30) {
                return new ResultBean(ResultCode.PARAM_ERROR, "标题过长");
            }
            if (StringUtils.isNull(invitationBody)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "内容不为空");
            }
            return invitationDealService.addInvitation(user, invitationTitle, invitationBody);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("/selectInvitation")
    @ResponseBody
    public ResultBean selectInvitation(@RequestParam(value="invitationTitle", defaultValue = "", required = false) String invitationTitle,
                                       @RequestParam(value="aOrs", defaultValue = "1", required = false) int aOrs,
                                       @RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNum,
                                       @RequestParam(value="order", defaultValue = "updateDate", required = false) String order,
                                       @RequestParam(value="pageSize", defaultValue="10", required = false) int pageSize,
                                        @RequestParam(value = "getMy", defaultValue = "-1", required = false) int getMy,
                                    HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = getUserIdFromSession(request);
            if (user == null) {
                //return new ResultBean(ResultCode.SESSION_OUT);
                user = new User();
                user.setUserId(2);
            }

            return invitationDealService.selectInvitation(user, invitationTitle, aOrs, pageNum, order, pageSize, getMy);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("/invitationDetail")
    @ResponseBody
    public ResultBean invitationDetail(@RequestParam(value = "invitationId", required = true) long invitationId,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = getUserIdFromSession(request);
            if (user == null) {
                //return new ResultBean(ResultCode.SESSION_OUT);
                user = new User();
                user.setUserId(2);
            }

            return invitationDealService.invitationDetail(user, invitationId);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping("/deleteInvitation")
    @ResponseBody
    public ResultBean deleteInvitation(@RequestParam(value = "invitationId", required = true) long invitationId,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = getUserIdFromSession(request);
            if (user == null) {
                //return new ResultBean(ResultCode.SESSION_OUT);
                user = new User();
                user.setUserId(2);
            }

            return invitationDealService.deleteInvitation(user, invitationId);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }
}
