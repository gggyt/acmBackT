package com.example.acm.service.deal.Impl;

import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.common.SysConst;
import com.example.acm.entity.Invitation;
import com.example.acm.entity.User;
import com.example.acm.service.InvitationService;
import com.example.acm.service.UserService;
import com.example.acm.service.deal.InvitationDealService;
import com.example.acm.utils.DateUtils;
import com.example.acm.utils.ListPage;
import com.example.acm.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ggg on 2019/2/5.
 */
@Service
public class InvitationDealImpl implements InvitationDealService{

    private static final Logger LOG = LoggerFactory.getLogger(InvitationDealImpl.class);

    @Autowired
    public InvitationService invitationService;
    @Autowired
    private UserService userService;

    public ResultBean addInvitation(User user, String invitationTitle, String invitationBody){
        try {
            Invitation invitation = new Invitation();
            Long bigInteger = new Long(UUIDUtil.getNumUUID());
            invitation.setInvitationId(bigInteger);
            invitation.setInvitationTitle(invitationTitle);
            invitation.setInvitationBody(invitationBody);
            invitation.setCreateUser(user.getUserId());
            invitation.setCreateDate(new Date());
            invitation.setUpdateUser(user.getUserId());
            invitation.setUpdateDate(new Date());
            invitation.setReadNum(0);
            invitation.setAgreeNum(0);
            invitation.setIsFirst(SysConst.NOT_FIRST);
            invitation.setIsGreate(SysConst.NOT_GREATE);
            invitation.setIsEffective(SysConst.LIVE);

            invitationService.addInvitation(invitation);
            return new ResultBean(ResultCode.SUCCESS, bigInteger);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean selectInvitation(User user, String invitationTitle, int aOrs, int pageNum, String order, int pageSize, int getMy){
        try{
            Map<String, Object> map = new HashMap<>();
            if (pageNum < 0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "页码不能小于0");
            }
            if (pageSize < 0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "一页展示数量不能小于0");
            }
            int start = (pageNum - 1) * pageSize;
            int limit = pageSize;
            map.put("invitationTitle", invitationTitle);
            map.put("start", start);
            map.put("limit", limit);
            map.put("order", order);
            if (aOrs == 1) {
                map.put("aOrS", "DESC");
            } else {
                map.put("aOrS", "ASC");
            }
            if (getMy!=-1) {
                map.put("createUser", user.getUserId());
            }
            map.put("isEffective", SysConst.LIVE);
            List<Map<String, Object>> list = invitationService.findInvitationMapListByQuery(map);
            int allNum = invitationService.countInvitationMapListByQuery(map);
            if (list.size() >0) {
                for (Map<String, Object> mapTemp : list) {
                    mapTemp.put("createDate", DateUtils.convDateToStr((Date) mapTemp.get("createDate"), "yyyy-MM-dd HH:mm:ss"));
                    mapTemp.put("updateDate", DateUtils.convDateToStr((Date) mapTemp.get("updateDate"), "yyyy-MM-dd HH:mm:ss"));
                    List<User> users = userService.findUserListByUserId(Integer.parseInt(mapTemp.get("createUser").toString()));
                    mapTemp.put("createUser", users.get(0).getUsername());
                    mapTemp.put("imageUrl", users.get(0).getImage());
                }
            }


            ListPage<List<Map<String, Object>>> listPage = ListPage.createListPage(pageNum, pageSize, allNum, list);

            return new ResultBean(ResultCode.SUCCESS, listPage);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean invitationDetail(User user, long invitationId){
        try{

            List<Map<String, Object>> invitations= invitationService.findInvitation2MapListByInvitationId(invitationId);
            if (invitations.size()==0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在该帖子");
            }
            Map<String, Object> invitation = invitations.get(0);
            List<User> users = userService.findUserListByUserId((Integer)invitation.get("createUser"));
            invitation.put("createDate", DateUtils.convDateToStr((Date) invitation.get("createDate"), "yyyy-MM-dd HH:mm:ss"));

            invitation.put("createUser", users.get(0).getUsername());
            invitation.put("imageUrl", users.get(0).getImage());

            return new ResultBean(ResultCode.SUCCESS, invitation);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean deleteInvitation(User user, long invitationId){
        try{

            List<Invitation> invitations= invitationService.findInvitationListByInvitationId(invitationId);
            if (invitations.size()==0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在该帖子");
            }
            Invitation invitation = invitations.get(0);
            invitation.setIsEffective(SysConst.NOT_LIVE);
            invitationService.updateInvitationByInvitationId(invitationId, invitation);

            return new ResultBean(ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }
}
