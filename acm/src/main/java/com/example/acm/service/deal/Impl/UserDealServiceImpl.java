package com.example.acm.service.deal.Impl;

import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.common.SysConst;
import com.example.acm.entity.User;
import com.example.acm.service.UserService;
import com.example.acm.service.deal.UserDealService;
import com.example.acm.utils.DateUtils;
import com.example.acm.utils.ListPage;
import javafx.beans.binding.ObjectExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ggg on 2018/11/25.
 */
@Service
public class UserDealServiceImpl implements UserDealService{

    private static final Logger LOG = LoggerFactory.getLogger(UserDealServiceImpl.class);
    @Autowired
    private UserService userService;

    public ResultBean webLogin(String username, String passwrod) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("username", username);
            List<User> user = userService.findUserListByQuery(map);
            if (user.size()==0) {
                return new ResultBean(ResultCode.HAS_NO_THIS_USER);
            }
            if (user.get(0).getPassword().equals(passwrod)) {
                return new ResultBean(ResultCode.SUCCESS);
            } else {
                return new ResultBean(ResultCode.PWD_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean register(String mobile, String username, String number, String password, String openId){
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("mobile", mobile);
            map.put("isEffective", SysConst.USE);
            List<User> userMobiles = userService.findUserListByQuery(map);
            if (userMobiles.size()!=0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "该手机号已注册");
            }
            map.clear();
            map.put("username", username);
            map.put("isEffective", SysConst.USE);
            List<User> userNames = userService.findUserListByQuery(map);
            if (userNames.size()!=0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "该用户名已注册");
            }
            map.clear();
            map.put("studentId", number);
            map.put("isEffective", SysConst.USE);
            List<User> userId = userService.findUserListByQuery(map);
            if (userId.size()!=0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "该学号已注册");
            }

            User user = new User();
            user.setMobile(mobile);
            user.setUsername(username);
            user.setStudentId(new Long(number));
            user.setPassword(password);
            user.setAuth(SysConst.NOT_PASS);
            user.setCreateDay(new Date());
            user.setIsEffective(SysConst.USE);
            user.setOpenId(openId);

            userService.addUser(user);
            return new ResultBean(ResultCode.SUCCESS);

        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean selectUsers(User user, String name, int aOrs, String order, int pageNum, int pageSize){
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("username", name);

            if (pageNum < 0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "页码不能小于0");
            }
            if (pageSize < 0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "一页展示数量不能小于0");
            }
            int start = (pageNum - 1) * pageSize;
            int limit = pageSize;
            map.put("start", start);
            map.put("limit", limit);
            map.put("order", order);
            if (aOrs == 1) {
                map.put("aOrS", "DESC");
            } else {
                map.put("aOrS", "ASC");
            }
            map.put("isEffective", 1);
            List<Map<String, Object>> users = userService.findUserMapListByQuery(map);

            int index = -1;
            int num=0;
            if (users.size() >0) {
                for (Map<String, Object> mapTemp : users) {
                    mapTemp.put("createDate", DateUtils.convDateToStr((Date) mapTemp.get("createDay"), "yyyy/MM/dd HH:mm:ss"));
                    mapTemp.remove("password");
                    if (mapTemp.get("userId").equals(2L)) {
                        index = num;
                        break;
                    }
                    num++;
                }
            }
            if (index!=-1) {
                users.remove(index);
            }
            int allNum = userService.countUserMapListByQuery(map);

            ListPage<List<Map<String, Object>>> listPage = ListPage.createListPage(pageNum, pageSize, allNum, users);

            return new ResultBean(ResultCode.SUCCESS, listPage);
        }catch(Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean changeAuth(User user, int userId, int auth){
        try {
            List<User> users = userService.findUserListByUserId(userId);
            if (users.size()==0) {
                return new ResultBean(ResultCode.HAS_NO_THIS_USER);
            }
            User userN = users.get(0);

            userN.setAuth(auth);

            userService.updateUserByUserId(userId, userN);
            return new ResultBean(ResultCode.SUCCESS);
        }catch(Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean userInfo(User user, int userId){
        try {
            List<User> users = userService.findUserListByUserId(userId);
            if (users.size()==0) {
                return new ResultBean(ResultCode.HAS_NO_THIS_USER);
            }
            User userN = users.get(0);

            return new ResultBean(ResultCode.SUCCESS, userN);
        }catch(Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }


    public ResultBean updateUserImage(User user, int userId, String image){
        try {
            List<User> users = userService.findUserListByUserId(userId);
            if (users.size()==0) {
                return new ResultBean(ResultCode.HAS_NO_THIS_USER);
            }
            User userN = users.get(0);
            userN.setImage(image);
            userService.updateUserByUserId(userId, userN);

            return new ResultBean(ResultCode.SUCCESS);
        }catch(Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    public ResultBean updateUserInfo(User user, int userId, String username, String realname, String password, String mobile,
                                     long studentId, int grade, int classNum){
        try {
            List<User> users = userService.findUserListByUserId(userId);
            if (users.size()==0) {
                return new ResultBean(ResultCode.HAS_NO_THIS_USER);
            }
            User userN = users.get(0);
            userN.setUsername(username);
            userN.setRealname(realname);
            userN.setPassword(password);
            userN.setMobile(mobile);
            userN.setStudentId(studentId);
            userN.setGrade(grade);
            userN.setClassNum(classNum);

            userService.updateUserByUserId(userId, userN);

            return new ResultBean(ResultCode.SUCCESS);
        }catch(Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

}
