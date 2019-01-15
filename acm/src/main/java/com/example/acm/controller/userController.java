package com.example.acm.controller;

import com.example.acm.authorization.manager.TokenManager;
import com.example.acm.authorization.model.TokenModel;
import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.conf.RedisComponent;
import com.example.acm.entity.User;
import com.example.acm.service.UserService;
import com.example.acm.service.deal.UserDealService;
import com.example.acm.utils.NumUtil;
import com.example.acm.utils.StringUtils;
import com.example.acm.view.UserView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ggg on 2018/11/25.
 */
@Controller
@RequestMapping("/userLogin")
public class userController extends BaseController{

    private static final Logger LOG = LoggerFactory.getLogger(userController.class);
    @Autowired
    private UserDealService userDealService;
    @Autowired
    RedisComponent redisComponent;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenManager tokenManager;

    @RequestMapping(value = "/webLogin", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean login(@RequestParam(value = "username", required = false) String username,
                            @RequestParam(value="password", required = false)String password,
                            HttpServletRequest request, HttpServletResponse response) {
        try {
            if (StringUtils.isNull(username)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "请输入用户名");
            }
            if (StringUtils.isNull(username)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "请输入密码");
            }
            Map<String, Object> map = new HashMap<>();
            LOG.info("-----------token:"+request.getHeader("Authorization"));
            LOG.info("username"+username);
            map.put("username", username);
            map.put("isEffective", 1);
            List<User> user = userService.findUserListByQuery(map);
            if (user==null) {
                return new ResultBean(ResultCode.HAS_NO_THIS_USER);
            }
            if (user.get(0).getPassword().equals(password)) {
                setUserSession(request, response, request.getSession(), user.get(0));
                LOG.info(username);
                LOG.info("userId-----------------"+user.get(0).getUserId());
                if (user.get(0).getAuth()==0) {
                    return new ResultBean(ResultCode.PARAM_ERROR, "用户尚未通过审核");

                }
                TokenModel model = tokenManager.createToken(user.get(0).getUserId());
                return new ResultBean(ResultCode.SUCCESS, model);
            } else {
                return new ResultBean(ResultCode.PWD_ERROR);
            }
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getUserInfo(HttpServletRequest request) {
        try {
            String uId = request.getHeader("Authorization");
            LOG.info("----------------uID"+uId);

            if (request.getHeader("Authorization")==null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }
            User user = getUserIdFromSession(request);
            if (user==null) {
                return new ResultBean(ResultCode.SESSION_OUT);
            }
            if (user.getAuth()==0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "用户尚未通过审核");

            }
            UserView userView = new UserView(user);
            LOG.info(userView.getUsername());
            return new ResultBean(ResultCode.SUCCESS, userView);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean register(@RequestParam("mobile") String mobile,
                               @RequestParam("username") String username,
                               @RequestParam("number") String number,
                               @RequestParam("password") String password,
                               HttpServletRequest request) {
        try {
            if (!NumUtil.isPhone(mobile)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "请输入正确的手机号");
            }
            if (StringUtils.isNull(username)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "请输入用户名");
            }
            if (!NumUtil.isUserName(username)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "请输入3-20字符的用户名");
            }
            if (StringUtils.isNull(number)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "请输入学号");
            }
            if (!NumUtil.isStudent(number)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "学号(仅为数字)长度不符要求");
            }
            if (StringUtils.isNull(password)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "请输入密码");
            }
            if(!NumUtil.isPassword(password)) {
                return new ResultBean(ResultCode.PARAM_ERROR, "请输入6-20字符的密码");

            }
            return userDealService.register(mobile, username, number, password);
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return new ResultBean(ResultCode.SYSTEM_FAILED);
        }

    }
}
