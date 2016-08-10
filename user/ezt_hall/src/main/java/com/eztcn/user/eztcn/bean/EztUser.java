package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 患者实体类
 * @describe
 * @author ezt
 * @created 2015年1月15日
 */
public class EztUser implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
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
    private Integer growthValue;// 会员成长值
    private Integer memberIntegral;// 会员积分
    private int wedLock;// 婚姻状况
    private String medicalNo;// 医保号
    private int profession;// 职业
    private int culturalLeave;// 文化水平
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

    /**
     * 滴滴优惠券显示标记
     */
    private boolean isFirstReg = false;// 新用户
    private boolean isFirstAppoint = false;// 第一次预约

    public boolean isFirstReg() {
        return isFirstReg;
    }

    public void setFirstReg(boolean isFirstReg) {
        this.isFirstReg = isFirstReg;
    }

    public boolean isFirstAppoint() {
        return isFirstAppoint;
    }

    public void setFirstAppoint(boolean isFirstAppoint) {
        this.isFirstAppoint = isFirstAppoint;
    }

    public EztUser() {
        super();
    }

    /**
     * 这是患者id 千万注意
     *
     * @return
     */
    public String getId() {
        return patientId;
    }
    /**
     * 这是患者id 千万注意
     *
     * @return
     */
    public void setId(String id) {
        this.patientId = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getmLevel_id() {
        if (mLevel_id == null) {
            return 0;
        }
        return mLevel_id;
    }

    public void setmLevel_id(Integer mLevel_id) {
        this.mLevel_id = mLevel_id;
    }

    public Integer getGrowthValue() {
        if (growthValue == null) {
            return 0;
        }
        return growthValue;
    }

    public void setGrowthValue(Integer growthValue) {
        this.growthValue = growthValue;
    }

    public Integer getMemberIntegral() {
        if (memberIntegral == null) {
            return 0;
        }
        return memberIntegral;
    }

    public void setMemberIntegral(Integer memberIntegral) {
        this.memberIntegral = memberIntegral;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getIdnoType() {
        return idnoType;
    }

    public void setIdnoType(int idnoType) {
        this.idnoType = idnoType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getWedLock() {
        return wedLock;
    }

    public void setWedLock(int wedLock) {
        this.wedLock = wedLock;
    }

    public String getMedicalNo() {
        return medicalNo;
    }

    public void setMedicalNo(String medicalNo) {
        this.medicalNo = medicalNo;
    }

    public int getProfession() {
        return profession;
    }

    public void setProfession(int profession) {
        this.profession = profession;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

	public int getCulturalLeave() {
		return culturalLeave;
	}

	public void setCulturalLeave(int culturalLeave) {
		this.culturalLeave = culturalLeave;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPatientGrade() {
		return patientGrade;
	}

	public void setPatientGrade(int patientGrade) {
		this.patientGrade = patientGrade;
	}

	public int getPatientRecord() {
		return patientRecord;
	}

	public void setPatientRecord(int patientRecord) {
		this.patientRecord = patientRecord;
	}

	public int getpGrowValue() {
		return pGrowValue;
	}

	public void setpGrowValue(int pGrowValue) {
		this.pGrowValue = pGrowValue;
	}

	public int getDataPerfect() {
		return dataPerfect;
	}

	public void setDataPerfect(int dataPerfect) {
		this.dataPerfect = dataPerfect;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getMessageSet() {
		return messageSet;
	}

	public void setMessageSet(String messageSet) {
		this.messageSet = messageSet;
	}

}
