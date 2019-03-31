package com.example.acm.service.deal.Impl;

import com.example.acm.common.ResultBean;
import com.example.acm.common.ResultCode;
import com.example.acm.common.SysConst;
import com.example.acm.entity.Applylecture;
import com.example.acm.entity.Lecture;
import com.example.acm.entity.Photo;
import com.example.acm.entity.User;
import com.example.acm.service.ApplylectureService;
import com.example.acm.service.LectureService;
import com.example.acm.service.UserService;
import com.example.acm.service.deal.LectureDealService;
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
 * Created by ggg on 2019/3/11.
 */
@Service
public class LectureDealImpl implements LectureDealService {

    private static final Logger LOG = LoggerFactory.getLogger(LectureDealImpl.class);

    @Autowired
    private LectureService lectureService;

    @Autowired
    private ApplylectureService applylectureService;

    @Autowired
    private UserService userService;

    public ResultBean addLecture(User user, String lectureTitle, String lectureBody){
        try {
            Long bigInteger = new Long(UUIDUtil.getNumUUID());
            Lecture lecture = new Lecture();

            lecture.setLectureId(bigInteger);
            lecture.setLectureTitle(lectureTitle);
            lecture.setLectureBody(lectureBody);
            lecture.setCreateUser(user.getUserId());
            lecture.setCreateDate(new Date());
            lecture.setIsDone(SysConst.LIVE);
            lecture.setUpdateUser(user.getUserId());
            lecture.setUpdateDate(new Date());
            lecture.setIsFirst(0);
            lecture.setIsEffective(SysConst.LIVE);

            lectureService.addLecture(lecture);

            return new ResultBean(ResultCode.SUCCESS, bigInteger);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL);
        }

    }

    public ResultBean updateLecture(User user, long lectureId, String lectureTitle, String lectureBody){
        try {
            List<Lecture> lectures = lectureService.findLectureListByLectureId(lectureId);
            if (lectures.size()<1) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在的讲座id");
            }
            Lecture lec = lectures.get(0);
            if (lec.getIsDone()==SysConst.NOT_USE) {
                return new ResultBean(ResultCode.PARAM_ERROR, "该讲座已结束");
            }
            lec.setLectureTitle(lectureTitle);
            lec.setLectureBody(lectureBody);
            lectureService.updateLectureByLectureId(lectureId, lec);

            return new ResultBean(ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL);
        }
    }

    public ResultBean selectLecture(User user, String lectureTitle, int aOrs, int pageNum,String order, int pageSize){
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
            map.put("lectureTitle", lectureTitle);
            map.put("start", start);
            map.put("limit", limit);
            map.put("order", order);
            //  map.put("isPublic", isPublic);
            if (aOrs == 1) {
                map.put("aOrS", "DESC");
            } else {
                map.put("aOrS", "ASC");
            }
            map.put("isEffective", 1);
            List<Map<String, Object>> list = lectureService.findLectureMapListByQuery(map);

            int allNum = lectureService.countLectureListByQuery(map);
            if (list.size() >0) {
                for (Map<String, Object> mapTemp : list) {
                    mapTemp.put("createDate", DateUtils.convDateToStr((Date) mapTemp.get("createDate"), "yyyy-MM-dd HH:mm:ss"));


                }
            }
            ListPage<List<Map<String, Object>>> listPage = ListPage.createListPage(pageNum, pageSize, allNum, list);

            return new ResultBean(ResultCode.SUCCESS, listPage);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL);
        }
    }

    public ResultBean selectLecturePerson(User user, long lectureId, int aOrs, int pageNum,String order, int pageSize){
        try {
            List<Lecture> lectures = lectureService.findLectureListByLectureId(lectureId);
            if (lectures.size()==0) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在校赛");
            }
            Lecture lecture = lectures.get(0);
            if (lecture.getIsEffective()==SysConst.NOT_PASS) {
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
            map.put("lectureId", lectureId);
            map.put("start", start);
            map.put("limit", limit);
            map.put("order", order);
            //  map.put("isPublic", isPublic);
            if (aOrs == 1) {
                map.put("aOrS", "DESC");
            } else {
                map.put("aOrS", "ASC");
            }
            map.put("isEffective", 1);
            List<Map<String, Object>> list = userService.findUserByLectureId(map);
            if (list.size() >0) {
                for (Map<String, Object> mapTemp : list) {
                    mapTemp.put("createDate", DateUtils.convDateToStr((Date) mapTemp.get("createDate"), "yyyy-MM-dd HH:mm:ss"));
                }
            }

            int allNum = userService.countUserByLectureId(map);

            ListPage<List<Map<String, Object>>> listPage = ListPage.createListPage(pageNum, pageSize, allNum, list);
            return new ResultBean(ResultCode.SUCCESS, listPage);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL);
        }
    }

    public ResultBean lectureDetail(User user, long lectureId){
        try {
            List<Map<String, Object>> lectures = lectureService.findLecture2MapListByLectureId(lectureId);
            if (lectures.size()<1) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在的讲座id");
            }
            Map<String, Object> lecture = lectures.get(0);
            lecture.put("createDate", DateUtils.convDateToStr((Date) lecture.get("createDate"), "yyyy-MM-dd HH:mm:ss"));
            lecture.put("updateDate", DateUtils.convDateToStr((Date) lecture.get("updateDate"), "yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> map = new HashMap<>();
            map.put("isEffective", SysConst.LIVE);
            map.put("lectureId", lectureId);
            List<Applylecture> applylectures = applylectureService.findApplylectureListByQuery(map);
            lecture.put("joinUserNum", applylectures.size());


            return new ResultBean(ResultCode.SUCCESS, lecture);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL);
        }
    }

    public ResultBean applyLecture(User user, long lectureId){
        try {
            List<Lecture> lectures = lectureService.findLectureListByLectureId(lectureId);
            if (lectures.size()<1) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在的讲座id");
            }
            Lecture lec = lectures.get(0);
            if (lec.getIsDone()==SysConst.NOT_LIVE) {
                return new ResultBean(ResultCode.PARAM_ERROR, "该讲座已结束");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("isEffective", SysConst.LIVE);
            map.put("lectureId", lectureId);
            map.put("joinUser",user.getUserId());
            List<Applylecture> applylectures = applylectureService.findApplylectureListByQuery(map);
            if (applylectures.size()>0) {
                return new ResultBean(ResultCode.SYSTEM_FAILED, "你已申请该讲座");
            }
            Applylecture applylecture = new Applylecture();
            applylecture.setLectureId(lectureId);
            applylecture.setJoinUser(user.getUserId());
            applylecture.setCreateDate(new Date());
            applylecture.setIsEffective(SysConst.LIVE);

            applylectureService.addApplylecture(applylecture);

            return new ResultBean(ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL);
        }
    }

    public ResultBean doneLecture(User user, long lectureId){
        try {
            List<Lecture> lectures = lectureService.findLectureListByLectureId(lectureId);
            if (lectures.size()<1) {
                return new ResultBean(ResultCode.PARAM_ERROR, "不存在的讲座id");
            }
            Lecture lec = lectures.get(0);
            if (lec.getIsDone()==SysConst.NOT_USE) {
                return new ResultBean(ResultCode.PARAM_ERROR, "该讲座已结束");
            }
            lec.setIsDone(SysConst.NOT_USE);
            lec.setUpdateDate(new Date());
            lec.setUpdateUser(user.getUserId());

            lectureService.updateLectureByLectureId(lectureId, lec);

            return new ResultBean(ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return new ResultBean(ResultCode.OTHER_FAIL);
        }
    }

    public ResultBean deleteLecture(User user, long lectureId){
        List<Lecture> lectures = lectureService.findLectureListByLectureId(lectureId);
        if (lectures.size()<1) {
            return new ResultBean(ResultCode.PARAM_ERROR, "不存在的讲座id");
        }
        Lecture lec = lectures.get(0);
        if (lec.getIsEffective()==SysConst.NOT_LIVE) {
            return new ResultBean(ResultCode.PARAM_ERROR, "该讲座已删除");
        }
        lec.setIsEffective(SysConst.NOT_LIVE);
        lec.setUpdateDate(new Date());
        lec.setUpdateUser(user.getUserId());

        lectureService.updateLectureByLectureId(lectureId, lec);

        return new ResultBean(ResultCode.SUCCESS);

    }
}
