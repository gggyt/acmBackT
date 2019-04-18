package com.example.acm.service.deal.Impl;

import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.common.SysConst;
import com.example.acm.entity.ApplyCompetition;
import com.example.acm.entity.Competition;
import com.example.acm.entity.User;
import com.example.acm.service.ApplyCompetitionService;
import com.example.acm.service.CompetitionService;
import com.example.acm.service.UserService;
import com.example.acm.service.deal.CompetitionDealService;
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
 * Created by xgg on 2019/2/18.
 */
@Service
public class CompetitionDealImpl implements CompetitionDealService{

    private static final Logger LOG = LoggerFactory.getLogger(CompetitionDealImpl.class);

    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplyCompetitionService applyCompetitionService;

    public ResultBean addCompetition(User user, String competitionTitle, String competitionBody){
        try {
            Long bigInteger = new Long(UUIDUtil.getNumUUID());
            Date now = new Date();
            Competition competition = new Competition();
            competition.setCompetitionId(bigInteger);
            competition.setCompetitionTitle(competitionTitle);
            competition.setCompetitionBody(competitionBody);
            competition.setCreateUser(user.getUserId());
            competition.setCreateDate(now);
            competition.setUpdateUser(user.getUserId());
            competition.setUpdateDate(now);
            competition.setIsEffective(SysConst.LIVE);

            competitionService.addCompetition(competition);
            return new ResultBean(ResultCode.SUCCESS, bigInteger);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error((e.getMessage()));
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean detailCompetition(User user, long competitionId) {
        try {
            List<Map<String, Object>> competitions = competitionService.findCompetition2MapListByCompetitionId(competitionId);
            if (competitions.size()==0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在该校赛");
            }
            Map<String, Object> competition = competitions.get(0);
            if ((int)competition.get("isEffective")==SysConst.NOT_PASS) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在该校赛");
            }
            competition.put("createDate", DateUtils.convDateToStr((Date) competition.get("createDate"), "yyyy-MM-dd HH:mm:ss"));
            competition.put("updateDate", DateUtils.convDateToStr((Date) competition.get("updateDate"), "yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("competitionId", competitionId);
            map.put("isEffective", SysConst.LIVE);
            List<ApplyCompetition> applyCompetitions = applyCompetitionService.findApplyCompetitionListByQuery(map);
            competition.put("joinUserNum", applyCompetitions.size());
            return new ResultBean(ResultCode.SUCCESS, competition);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error((e.getMessage()));
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }
    public ResultBean updateCompetition(User user, long competitionId, String competitionTitle, String competitionBody){
        try {
            List<Competition> competitions = competitionService.findCompetitionListByCompetitionId(competitionId);
            if (competitions.size()==0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在该校赛");
            }
            Competition competition = competitions.get(0);
            if (competition.getIsEffective()==SysConst.NOT_PASS) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在该校赛");
            }
            competition.setCompetitionTitle(competitionTitle);
            competition.setCompetitionBody(competitionBody);
            competition.setUpdateDate(new Date());
            competition.setUpdateUser(user.getUserId());
            competitionService.updateCompetitionByCompetitionId(competitionId, competition);

            return new ResultBean(ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error((e.getMessage()));
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean selectCompetition(String competitionTitle, int aOrs, String order, int pageNum, int pageSize){
        try {
            Map<String, Object> map = new HashMap<>();
            if (pageNum < 0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "页码不能小于0");
            }
            if (pageSize < 0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "一页展示数量不能小于0");
            }
            int start = (pageNum - 1) * pageSize;
            int limit = pageSize;
            map.put("competitionTitle", competitionTitle);
            map.put("start", start);
            map.put("limit", limit);
            map.put("order", order);
            if (aOrs == 1) {
                map.put("aOrS", "DESC");
            } else {
                map.put("aOrS", "ASC");
            }
            map.put("isEffective", SysConst.LIVE);
            List<Map<String, Object>> list = competitionService.findCompetitionMapListByQuery(map);

            if (list.size() >0) {
                for (Map<String, Object> mapTemp : list) {
                    mapTemp.put("createDate", DateUtils.convDateToStr((Date) mapTemp.get("createDate"), "yyyy-MM-dd HH:mm:ss"));
                    mapTemp.put("updateDate", DateUtils.convDateToStr((Date) mapTemp.get("updateDate"), "yyyy-MM-dd HH:mm:ss"));
                    List<User> users = userService.findUserListByUserId(Integer.parseInt(mapTemp.get("createUser").toString()));
                    mapTemp.put("createUser", users.get(0).getUsername());;
                    List<User> users1 = userService.findUserListByUserId(Integer.parseInt(mapTemp.get("updateUser").toString()));
                    mapTemp.put("updateUser", users1.get(0).getUsername());;
                }
            }

            int allNum = competitionService.countCompetitionMapListByQuery(map);

            ListPage<List<Map<String, Object>>> listPage = ListPage.createListPage(pageNum, pageSize, allNum, list);

            return new ResultBean(ResultCode.SUCCESS, listPage);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error((e.getMessage()));
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean deleteCompetition(User user, long competitionId){
        try{
            List<Competition> competitions = competitionService.findCompetitionListByCompetitionId(competitionId);
            if (competitions.size()==0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在校赛");
            }
            Competition competition = competitions.get(0);
            if (competition.getIsEffective()==SysConst.NOT_PASS) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在该校赛");
            }
            competition.setIsEffective(SysConst.NOT_LIVE);
            competition.setUpdateDate(new Date());
            competition.setUpdateUser(user.getUserId());
            competitionService.updateCompetitionByCompetitionId(competitionId, competition);

            return new ResultBean(ResultCode.SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error((e.getMessage()));
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean doneCompetition(User user, long competitionId){
        try{
            List<Competition> competitions = competitionService.findCompetitionListByCompetitionId(competitionId);
            if (competitions.size()==0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在校赛");
            }
            Competition competition = competitions.get(0);
            if (competition.getIsEffective()==SysConst.NOT_PASS) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在该校赛");
            }
            competition.setUpdateDate(new Date());
            competition.setUpdateUser(user.getUserId());
            competition.setIsDone(0);
            competitionService.updateCompetitionByCompetitionId(competitionId, competition);

            return new ResultBean(ResultCode.SUCCESS);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error((e.getMessage()));
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean joinCompetition(User user, long competitionId){
        try{
            List<Competition> competitions = competitionService.findCompetitionListByCompetitionId(competitionId);
            if (competitions.size()==0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在校赛");
            }
            Competition competition = competitions.get(0);
            if (competition.getIsEffective()==SysConst.NOT_PASS) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在该校赛");
            }
            if (competition.getIsDone()==0) {
                    return new ResultBean(ResultCode.PARAM_ERROR, "该比赛已结束");
                }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("competitionId", competitionId);
            map.put("joinUser", user.getUserId());
            map.put("isEffective", SysConst.LIVE);
            List<ApplyCompetition> applyCompetitions = applyCompetitionService.findApplyCompetitionListByQuery(map);
            if (applyCompetitions.size()!=0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "您已报名");
            }
            ApplyCompetition applyCompetition = new ApplyCompetition();
            applyCompetition.setCompetitionId(competitionId);
            applyCompetition.setJoinUser(user.getUserId());
            applyCompetition.setCreateDate(new Date());
            applyCompetition.setIsEffective(SysConst.LIVE);
            applyCompetitionService.addApplyCompetition(applyCompetition);

            return new ResultBean(ResultCode.SUCCESS, "报名成功");
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error((e.getMessage()));
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean personCompetition(User user, long competitionId, int aOrs, String order, int pageNum, int pageSize){
        try{
            List<Competition> competitions = competitionService.findCompetitionListByCompetitionId(competitionId);
            if (competitions.size()==0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在校赛");
            }
            Competition competition = competitions.get(0);
            if (competition.getIsEffective()==SysConst.NOT_PASS) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在该校赛");
            }

            Map<String, Object> map = new HashMap<>();
            if (pageNum < 0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "页码不能小于0");
            }
            if (pageSize < 0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "一页展示数量不能小于0");
            }
            int start = (pageNum - 1) * pageSize;
            int limit = pageSize;
            map.put("competitionId", competitionId);
            map.put("start", start);
            map.put("limit", limit);
            map.put("order", order);
            if (aOrs == 1) {
                map.put("aOrS", "DESC");
            } else {
                map.put("aOrS", "ASC");
            }
            map.put("isEffective", 1);
            List<Map<String, Object>> list = userService.findUserByCompetitionId(map);
            if (list.size() >0) {
                for (Map<String, Object> mapTemp : list) {
                    mapTemp.put("createDate", DateUtils.convDateToStr((Date) mapTemp.get("createDate"), "yyyy-MM-dd HH:mm:ss"));
                    }
            }

            int allNum = userService.countUserByCompetitionId(map);

            ListPage<List<Map<String, Object>>> listPage = ListPage.createListPage(pageNum, pageSize, allNum, list);
            return new ResultBean(ResultCode.SUCCESS, listPage);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error((e.getMessage()));
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean userCompetition(User user, int userId, int aOrs, String order, int pageNum, int pageSize){
        try {
            Map<String, Object> map = new HashMap<>();
            if (pageNum < 0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "页码不能小于0");
            }
            if (pageSize < 0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "一页展示数量不能小于0");
            }
            int start = (pageNum - 1) * pageSize;
            int limit = pageSize;
            map.put("joinUser", userId);
            if (userId==-1) {
                map.put("joinUser", user.getUserId());
            }
            map.put("isEffective", SysConst.LIVE);
            List<Map<String, Object>> list1 = competitionService.findCompetitionMapListByUser(map);
            int allNum = list1.size();
            map.put("start", start);
            map.put("limit", limit);
            map.put("order", order);
            if (aOrs == 1) {
                map.put("aOrS", "DESC");
            } else {
                map.put("aOrS", "ASC");
            }
            List<Map<String, Object>> list = competitionService.findCompetitionMapListByUser(map);

            if (list.size() >0) {
                for (Map<String, Object> mapTemp : list) {
                    mapTemp.put("createDate", DateUtils.convDateToStr((Date) mapTemp.get("createDate"), "yyyy-MM-dd HH:mm:ss"));
                    mapTemp.put("updateDate", DateUtils.convDateToStr((Date) mapTemp.get("updateDate"), "yyyy-MM-dd HH:mm:ss"));
                    List<User> users = userService.findUserListByUserId(Integer.parseInt(mapTemp.get("createUser").toString()));
                    mapTemp.put("createUser", users.get(0).getUsername());;
                    List<User> users1 = userService.findUserListByUserId(Integer.parseInt(mapTemp.get("updateUser").toString()));
                    mapTemp.put("updateUser", users1.get(0).getUsername());;
                }
            }


            ListPage<List<Map<String, Object>>> listPage = ListPage.createListPage(pageNum, pageSize, allNum, list);

            return new ResultBean(ResultCode.SUCCESS, listPage);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error((e.getMessage()));
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }
}
