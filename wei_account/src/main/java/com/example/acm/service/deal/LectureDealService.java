package com.example.acm.service.deal;

import com.example.acm.common.ResultBean;
import com.example.acm.entity.User;

/**
 * Created by ggg on 2019/3/11.
 */
public interface LectureDealService {

    ResultBean addLecture(User user, String lectureTitle, String lectureBody, String date);

    ResultBean updateLecture(User user, long lectureId, String lectureTitle, String lectureBody, String date);

    ResultBean selectLecture(User user, String lectureTitle, int aOrs, int pageNum,String order, int pageSize);

    ResultBean selectUserLecture(User user, int userId, int aOrs, int pageNum,String order, int pageSize);

    ResultBean selectLecturePerson(User user, long lectureId, int aOrs, int pageNum,String order, int pageSize);

    ResultBean lectureDetail(User user, long lectureId);

    ResultBean applyLecture(User user, long lectureId);

    ResultBean doneLecture(User user, long lectureId);

    ResultBean deleteLecture(User user, long lectureId);

    ResultBean applyOrNot(User user, long lectureId);
}
