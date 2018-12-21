package com.example.acm.service.deal.Impl;

import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.common.SysConst;
import com.example.acm.entity.User;
import com.example.acm.service.UserService;
import com.example.acm.service.deal.UserDealService;
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
            if (user==null) {
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

    public ResultBean register(String mobile, String username, String number, String password){
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

            userService.addUser(user);
            return new ResultBean(ResultCode.SUCCESS);

        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }
}
