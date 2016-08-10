package com.eztcn.user.hall.db.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by lx on 2016/6/8.
 *保存user对象。
 */
@Table(name="user")
public class UserDao extends BaseDb {

    @Column(name="growthValue")
    private int growthValue;// 会员成长值

    @Column(name="memberIntegral")
    private int memberIntegral;// 会员积分

    @Column(name="wedLock")
    private int wedLock;// 婚姻状况

    @Column(name="medicalNo")
    private String medicalNo;// 医保号

    @Column(name="profession")
    private int profession;// 职业

    @Column(name = "culturalLeave")
    private int culturalLeave;// 文化水平

    @Column(name="address")
    private String address;// 地址


    private String otherPhone;// 其他联系电话方式(不能与注册号码一致)
    private String password;//
    private String familyId;// 家庭成员ID
    private int province;// 省
    private int cityId;// 常住城市
    private String email;// 邮箱
    private int patientGrade;// 患者等级
    private int patientRecord;// 患者积分
    private int pGrowValue;// 患者成长值
    private int dataPerfect;// 信息完善度
    private int category;// 类别
    private String messageSet;// 消息提醒设置 用于记录用户不需要提醒的各类消息

    private String euEmail;//邮箱
    private int epProvince;//省份
    private int epCity;//城市
    private int epCounty;//区

    /**
     * 滴滴优惠券显示标记
     */
    private boolean isFirstReg = false;// 新用户
    private boolean isFirstAppoint = false;// 第一次预约

    private String patientId;// 患者id
    private int userId;// 用户id
    private String photo;// 头像
    private String mobile;// 手机号
    private String userName;// 姓名
    private String nickName;// 昵称
    private String userNo;// 会员号
    private int idnoType;// 证件类型
    private String idCard;// 身份证号
    private int sex;
    private int age;
    private String birthday;
    private Integer mLevel_id;// 会员等级id


}
