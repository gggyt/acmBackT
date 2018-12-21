package com.example.acm.service.deal;

import com.example.acm.common.ResultBean;

/**
 * Created by ggg on 2018/11/25.
 */
public interface UserDealService {

    public ResultBean webLogin(String username, String password);

    public ResultBean register(String mobile, String username, String number, String password);
}
