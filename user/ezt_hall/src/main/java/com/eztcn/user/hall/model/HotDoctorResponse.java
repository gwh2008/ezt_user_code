package com.eztcn.user.hall.model;

import java.util.List;

/**
 * @Author: lizhipeng
 * @Data: 16/6/13 下午5:53
 * @Description: 默认热门医生
 */
public class HotDoctorResponse implements IModel {


    /**
     * data : [{"dcLevel":2,"dcName":"白内障科","deptId":296,"deptcateId":151,"deptdocId":1,"docId":1,"dptProfile":null,"dptTel":"","dptWebsite":"","dptame":"白内障科","edAddress":"","edFree":13,"edGoodat":"白内障、青光眼的诊断与治疗？","edLevel":55,"edLevelName":"主任医师","edName":"刘增业","edPic":"刘增业.jpg","edProfile":"白内障、青光眼、斜视的临床诊断与手术治疗","edSex":0,"edTelephone":"","ehAddress":"天津市南开区复康路251号","ehCity":2,"ehCityName":"组织机构","ehCounty":22,"ehCountyName":null,"ehHosType":470,"ehHosTypeName":"眼科医院","ehLevel":380,"ehLevelName":"三级甲等医院","ehName":"天津医科大学眼科医院","ehProvince":2,"ehProvinceName":"组织机构","ehRemark":null,"ehTel":null,"ehWebsite":"http://www.tmuec.com","hid":23},{"dcLevel":2,"dcName":"白内障科","deptId":296,"deptcateId":151,"deptdocId":2,"docId":2,"dptProfile":null,"dptTel":"","dptWebsite":"","dptame":"白内障科","edAddress":"","edFree":null,"edGoodat":"白内障、玻璃体视网膜疾病的诊断和治疗","edLevel":56,"edLevelName":"副主任医师","edName":"孙靖","edPic":"孙靖.jpg","edProfile":"白内障、青光眼、眼底病。            ","edSex":1,"edTelephone":"","ehAddress":"天津市南开区复康路251号","ehCity":2,"ehCityName":"组织机构","ehCounty":22,"ehCountyName":null,"ehHosType":470,"ehHosTypeName":"眼科医院","ehLevel":380,"ehLevelName":"三级甲等医院","ehName":"天津医科大学眼科医院","ehProvince":2,"ehProvinceName":"组织机构","ehRemark":null,"ehTel":null,"ehWebsite":"http://www.tmuec.com","hid":23},{"dcLevel":2,"dcName":"青光眼科","deptId":297,"deptcateId":150,"deptdocId":4,"docId":4,"dptProfile":null,"dptTel":"","dptWebsite":"","dptame":"青光眼科","edAddress":"","edFree":null,"edGoodat":"青光眼、白内障的诊断与治疗","edLevel":55,"edLevelName":"主任医师","edName":"季健","edPic":"25H398D1559D1341558621416.jpg","edProfile":"主任医师，硕士生导师 中华医学会天津眼科学会会员 世界眼外科学会会员。     毕业于天津医学院医疗系，近15年主要从事青光眼临床工作，治疗多种不同类型的青光眼、白内障数千例。1990年赴前苏联参观莫斯科眼科研究所和敖法隆眼科研究所，并参加第八界国际眼科学术会议并在大会发言，在国内多次参加学术会议，并在大会发言，在国内杂志发表数篇论文，参与撰写眼科专著一部。 ","edSex":1,"edTelephone":"","ehAddress":"天津市南开区复康路251号","ehCity":2,"ehCityName":"组织机构","ehCounty":22,"ehCountyName":null,"ehHosType":470,"ehHosTypeName":"眼科医院","ehLevel":380,"ehLevelName":"三级甲等医院","ehName":"天津医科大学眼科医院","ehProvince":2,"ehProvinceName":"组织机构","ehRemark":null,"ehTel":null,"ehWebsite":"http://www.tmuec.com","hid":23},{"dcLevel":2,"dcName":"青光眼科","deptId":297,"deptcateId":150,"deptdocId":5,"docId":5,"dptProfile":null,"dptTel":"","dptWebsite":"","dptame":"青光眼科","edAddress":"","edFree":13,"edGoodat":"青光眼、白内障的诊断与治疗","edLevel":55,"edLevelName":"主任医师","edName":"汪建涛","edPic":"汪建涛.jpg","edProfile":"  眼科临床博士 毕业于中山大学中山眼科中心，曾在美国著名的Doheny眼科研究所进行博士后工作四年，研究青光眼的发病和神经保护机制，临床上对青光眼早期诊断、晚期青光眼及难治性青光眼的治疗进行过系统研究，在青光眼的诊断和治疗方面积累了丰富的临床经验。先后主持和参与国家863项目，973项目二级子课题，上海市浦江人才计划，美国NIH，美国DOE等多个科研项目。","edSex":0,"edTelephone":"","ehAddress":"天津市南开区复康路251号","ehCity":2,"ehCityName":"组织机构","ehCounty":22,"ehCountyName":null,"ehHosType":470,"ehHosTypeName":"眼科医院","ehLevel":380,"ehLevelName":"三级甲等医院","ehName":"天津医科大学眼科医院","ehProvince":2,"ehProvinceName":"组织机构","ehRemark":null,"ehTel":null,"ehWebsite":"http://www.tmuec.com","hid":23},{"dcLevel":2,"dcName":"青光眼科","deptId":297,"deptcateId":150,"deptdocId":7,"docId":7,"dptProfile":null,"dptTel":"","dptWebsite":"","dptame":"青光眼科","edAddress":"","edFree":8,"edGoodat":"青光眼、白内障等眼科疾病的诊断与治疗","edLevel":56,"edLevelName":"副主任医师","edName":"邢小丽","edPic":"邢小丽.jpg","edProfile":"女，1971年出生，副主任医师，毕业于大连医科大学，博士在读","edSex":1,"edTelephone":"","ehAddress":"天津市南开区复康路251号","ehCity":2,"ehCityName":"组织机构","ehCounty":22,"ehCountyName":null,"ehHosType":470,"ehHosTypeName":"眼科医院","ehLevel":380,"ehLevelName":"三级甲等医院","ehName":"天津医科大学眼科医院","ehProvince":2,"ehProvinceName":"组织机构","ehRemark":null,"ehTel":null,"ehWebsite":"http://www.tmuec.com","hid":23},{"dcLevel":2,"dcName":"屈光科","deptId":298,"deptcateId":157,"deptdocId":8,"docId":8,"dptProfile":null,"dptTel":"","dptWebsite":"","dptame":"屈光与角膜病科","edAddress":"","edFree":null,"edGoodat":"角膜病、白内障及各种近视眼的诊断与治疗","edLevel":55,"edLevelName":"主任医师","edName":"赵少贞","edPic":"25H399D1563D1330499960324.jpg","edProfile":"赵少贞，医学博士 ，教授，硕士研究生导师，天津医科大学眼视光学院眼科教研室主任，继续医学教育委员会主任，屈光与角膜病科主任。1999年当选为天津市科研技术骨干，2000年当选为天津市跨世纪人才。2004年当选校级优秀教师。2005年获医大三育人优秀奖。2007年荣获天津市五一劳动奖章。目前为世界眼外科协会会员，中华医学会眼科分会会员。被特聘为中国侨联科技顾问。天津市医学会医疗事故技术鉴定专家。天津市劳动保障学会医疗保险分会专家。全国医用设备资格考试PRK医师专业委员会委员。赵少贞教授曾多次在国外进修学习：1994--1996赴新加坡国立大学眼科学系， 新加坡国立眼科中心和新加坡国立大学接受英国皇家医学会眼科学培训并获博士学位。2001年于美国德克萨斯州立大学医学院眼科专业进修培训。曾多次赴美国，香港，新加坡等地参加国际学术会议交流并发言。      赵少贞教授一方面在临床工作中精益求精，汲取国内外先进的技术和经验；一方面积极拓展工作领域，开展多项基础和临床研究，并承担了多项国际合作项目。共发表论文和参编著作50余篇。填补天津市空白两项。目前已完成国家十五攻关课题子课题一项，国际合作项目1项，天津市卫生局课题等2项，\u201c准分子激光PRK术后泪液中生长因子的含量与近视回退的关系研究\u201d 已完成并鉴定为国内先进。目前与新加坡国立眼科中心合作进行了干眼病的基础科学研究，并获天津市自然科学基金资助。此外还承担了多项天津市自然科学基金项目、天津市教委项目，并于今年由台湾同胞捐资4万美金用于怒江地区角膜疾患的流行病学调查研究与治疗。科研经费达50余万元。      工作能力及其业务专长：角膜病和眼表疾病的诊断、治疗与基础研究；近视眼的治疗与基础研究；复杂及疑难白内障的诊断和手术治疗。","edSex":1,"edTelephone":"","ehAddress":"天津市南开区复康路251号","ehCity":2,"ehCityName":"组织机构","ehCounty":22,"ehCountyName":null,"ehHosType":470,"ehHosTypeName":"眼科医院","ehLevel":380,"ehLevelName":"三级甲等医院","ehName":"天津医科大学眼科医院","ehProvince":2,"ehProvinceName":"组织机构","ehRemark":null,"ehTel":null,"ehWebsite":"http://www.tmuec.com","hid":23},{"dcLevel":2,"dcName":"屈光科","deptId":298,"deptcateId":157,"deptdocId":9,"docId":9,"dptProfile":null,"dptTel":"","dptWebsite":"","dptame":"屈光与角膜病科","edAddress":"","edFree":13,"edGoodat":"角膜、眼表疾病诊治，白内障手术，屈光手术，青少年近视防治,角膜接触镜治疗","edLevel":55,"edLevelName":"主任医师","edName":"魏瑞华","edPic":"魏瑞华.jpg","edProfile":" 医学博士，主任医师，硕士研究生导师。天津医科大学新世纪人才，中华医学会眼科学分会青年委员，中华医学会眼科学分会视光学组委员，美国白内障屈光手术医师学会会员，美国视觉及眼科研究协会会员，先后接受亚太地区及中国LASIK准分子激光屈光手术高级培训并获得资格证书。曾赴新加坡国立大学攻读硕士研究生，在美国南加州大学Doheny 眼科研究所完成博士课题。2001-2005年于新加坡全国眼科中心和新加坡眼科研究所临床进修及科研工作，连续4年获得新加坡卫生局批准的临床医师资格证书，掌握白内障超声乳化术、角膜移植术和青光眼手术。      主要从事角膜病、白内障、屈光手术、干眼和儿童近视防控，完成角膜移植手术，白内障超声乳化手术，屈光手术，青光眼手术上万例，同时擅长各种角膜接触镜验配及并发症处理。承担天津医科大学眼科和眼视光学教学工作，天津医科大学眼科医院眼库角膜捐献工作。参与完成多项国际课题，包括近视眼研究、屈光手术、角膜地形图研究、圆锥角膜研究、常见眼病流行病调查及干眼病研究。主持承担国家自然科学基金1项、天津市自然科学课题1项、天津市教委重点课题1项，与天津大学合作国家自然科学课题1项。发表专业论文30余篇， SCI文章17篇，其中第一作者SCI论文7篇。参编眼科论著2部。 ","edSex":1,"edTelephone":"","ehAddress":"天津市南开区复康路251号","ehCity":2,"ehCityName":"组织机构","ehCounty":22,"ehCountyName":null,"ehHosType":470,"ehHosTypeName":"眼科医院","ehLevel":380,"ehLevelName":"三级甲等医院","ehName":"天津医科大学眼科医院","ehProvince":2,"ehProvinceName":"组织机构","ehRemark":null,"ehTel":null,"ehWebsite":"http://www.tmuec.com","hid":23},{"dcLevel":2,"dcName":"屈光科","deptId":298,"deptcateId":157,"deptdocId":10,"docId":10,"dptProfile":null,"dptTel":"","dptWebsite":"","dptame":"屈光与角膜病科","edAddress":"","edFree":8,"edGoodat":"角膜、屈光及白内障的诊断与治疗","edLevel":56,"edLevelName":"副主任医师","edName":"黄悦","edPic":"25H399D1565D1407924382028.jpg","edProfile":"中华医学会会员，曾任援非洲刚果（布）中国医疗队眼科医生，于当地完成白内障、青光眼手术数百例。  接受中国卫生部LASIK准分子激光屈光手术高级培训并获得资格证书。从事眼科临床和教研工作十余年，主攻屈光角膜及白内障专业，对眼表疾病、白内障诊治有丰富经验，对近视、远视、老视、散光等屈光不正的发生、发展机制有深入研究。对准分子手术病人的筛选、术后并发症的处理具有丰富的临床经验。","edSex":1,"edTelephone":"","ehAddress":"天津市南开区复康路251号","ehCity":2,"ehCityName":"组织机构","ehCounty":22,"ehCountyName":null,"ehHosType":470,"ehHosTypeName":"眼科医院","ehLevel":380,"ehLevelName":"三级甲等医院","ehName":"天津医科大学眼科医院","ehProvince":2,"ehProvinceName":"组织机构","ehRemark":null,"ehTel":null,"ehWebsite":"http://www.tmuec.com","hid":23},{"dcLevel":1,"dcName":"眼科","deptId":299,"deptcateId":6,"deptdocId":11,"docId":11,"dptProfile":null,"dptTel":"","dptWebsite":"","dptame":"玻璃体视网膜与眼外伤科","edAddress":"","edFree":50,"edGoodat":"玻璃体、视网膜疾病的诊断与治疗","edLevel":55,"edLevelName":"主任医师","edName":"李筱荣","edPic":"25H400D1566D1330499933730.jpg","edProfile":"李筱荣，中共党员，天津医科大学眼科医院院长、眼视光学院院长、眼科研究所所长。教授、医学博士、博士生导师、香港理工大学视光学院客座教授。天津市劳动模范，天津市优秀共产党员，天津医科大学跨世纪人才，天津医科大学2002、2008年度优秀教师。中华医学会眼科学分会委员，中华医学会眼科学分会专家会员，中华医学会眼科学分会眼底病学组委员，天津医学会眼科学会常委，中国医师协会眼科医师分会委员，国际视网膜基金会委员，中国侨联特聘专家。     专业特长：玻璃体视网膜手术及眼底病的诊断与治疗；糖尿病眼病的防治与研究；白内障的临床和研究，特别是白内障超声乳化手术；眼前后节疾病的激光治疗和联合手术，干细胞在视网膜疾病中的应用研究。     承担国家自然科学基金3项（其中2项已结题）、天津市科技支撑计划重点项目1项、十五攻关课题子课题1项，教育部博士点基金1项。课题获得国际奖项（防盲治盲方面）2次、天津市科技进步三等奖2项、天津市自然科学奖三等奖1项（第四），天津市卫生系统引进应用新技术填补空白项目5项，天津医科大学科技成果奖2项。发表专业论文130余篇，其中SCI文章14篇；国家级专利3项；主编、副主编、参编专著18部，2010年作为副主编参与编写《眼病学》教材。","edSex":0,"edTelephone":"","ehAddress":"天津市南开区复康路251号","ehCity":2,"ehCityName":"组织机构","ehCounty":22,"ehCountyName":null,"ehHosType":470,"ehHosTypeName":"眼科医院","ehLevel":380,"ehLevelName":"三级甲等医院","ehName":"天津医科大学眼科医院","ehProvince":2,"ehProvinceName":"组织机构","ehRemark":null,"ehTel":null,"ehWebsite":"http://www.tmuec.com","hid":23},{"dcLevel":1,"dcName":"眼科","deptId":299,"deptcateId":6,"deptdocId":12,"docId":12,"dptProfile":null,"dptTel":"","dptWebsite":"","dptame":"玻璃体视网膜与眼外伤科","edAddress":"","edFree":8,"edGoodat":"玻璃体视网膜疾病、白内障的诊断及治疗","edLevel":56,"edLevelName":"副主任医师","edName":"胡博杰","edPic":"胡博杰.jpg","edProfile":"胡博杰，中共党员，中华医学会眼科学分会会员，中国微循环学会眼微循环专业委员会青年委员，天津医科大学眼科医院临床党支部书记，玻璃体视网膜科主任助理, 医学硕士，副主任医师。专业特长：玻璃体视网膜手术及眼底病的诊断与治疗，白内障超声乳化手术，眼前后节疾病的激光治疗。2000年毕业于中国医科大学日文临床专业 ，曾在新加坡全国眼科中心，北京大学人民医院眼科进修学习。现主持省级课题一项，完成局级科研一项，参加国家自然科学基金一项，省部级课题一项，参与天津市科委三等奖2项。第一作者发表文章十余篇，其中SCI收录2篇。参编《荧光素眼底血管造影手册》，《白内障显微手术彩色图谱》，《糖尿病眼病》专著三篇。基础研究方向糖尿病    视网膜病变的基础研究及临床。曾获中华眼科杂志2006年天津医科大学德高医粹青年先锋奖，2008年博士伦杯白内障手术精选优胜奖， 2013年新疆维吾尔族自治区：第七批省市优秀援疆干部人才（省级），天津市优秀援疆干部（市级）等荣誉。 ","edSex":0,"edTelephone":"","ehAddress":"天津市南开区复康路251号","ehCity":2,"ehCityName":"组织机构","ehCounty":22,"ehCountyName":null,"ehHosType":470,"ehHosTypeName":"眼科医院","ehLevel":380,"ehLevelName":"三级甲等医院","ehName":"天津医科大学眼科医院","ehProvince":2,"ehProvinceName":"组织机构","ehRemark":null,"ehTel":null,"ehWebsite":"http://www.tmuec.com","hid":23}]
     * detailMsg : 成功
     * flag : true
     * msg : 成功
     * number : 2000
     */

    private String detailMsg;
    private boolean flag;
    private String msg;
    private String number;
    /**
     * dcLevel : 2
     * dcName : 白内障科
     * deptId : 296
     * deptcateId : 151
     * deptdocId : 1
     * docId : 1
     * dptProfile : null
     * dptTel :
     * dptWebsite :
     * dptame : 白内障科
     * edAddress :
     * edFree : 13
     * edGoodat : 白内障、青光眼的诊断与治疗？
     * edLevel : 55
     * edLevelName : 主任医师
     * edName : 刘增业
     * edPic : 刘增业.jpg
     * edProfile : 白内障、青光眼、斜视的临床诊断与手术治疗
     * edSex : 0
     * edTelephone :
     * ehAddress : 天津市南开区复康路251号
     * ehCity : 2
     * ehCityName : 组织机构
     * ehCounty : 22
     * ehCountyName : null
     * ehHosType : 470
     * ehHosTypeName : 眼科医院
     * ehLevel : 380
     * ehLevelName : 三级甲等医院
     * ehName : 天津医科大学眼科医院
     * ehProvince : 2
     * ehProvinceName : 组织机构
     * ehRemark : null
     * ehTel : null
     * ehWebsite : http://www.tmuec.com
     * hid : 23
     */

    private List<DataBean> data;

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int dcLevel;
        private String dcName;
        private int deptId;
        private int deptcateId;
        private int deptdocId;
        private int docId;
        private Object dptProfile;
        private String dptTel;
        private String dptWebsite;
        private String dptame;
        private String edAddress;
        private int edFree;
        private String edGoodat;
        private int edLevel;
        private String edLevelName;
        private String edName;
        private String edPic;
        private String edProfile;
        private int edSex;
        private String edTelephone;
        private String ehAddress;
        private int ehCity;
        private String ehCityName;
        private int ehCounty;
        private Object ehCountyName;
        private int ehHosType;
        private String ehHosTypeName;
        private int ehLevel;
        private String ehLevelName;
        private String ehName;
        private int ehProvince;
        private String ehProvinceName;
        private Object ehRemark;
        private Object ehTel;
        private String ehWebsite;
        private int hid;
        private String ehIsneedPatientCard;

        public String getEhIsneedPatientCard() {
            return ehIsneedPatientCard;
        }

        public void setEhIsneedPatientCard(String ehIsneedPatientCard) {
            this.ehIsneedPatientCard = ehIsneedPatientCard;
        }

        public int getDcLevel() {
            return dcLevel;
        }

        public void setDcLevel(int dcLevel) {
            this.dcLevel = dcLevel;
        }

        public String getDcName() {
            return dcName;
        }

        public void setDcName(String dcName) {
            this.dcName = dcName;
        }

        public int getDeptId() {
            return deptId;
        }

        public void setDeptId(int deptId) {
            this.deptId = deptId;
        }

        public int getDeptcateId() {
            return deptcateId;
        }

        public void setDeptcateId(int deptcateId) {
            this.deptcateId = deptcateId;
        }

        public int getDeptdocId() {
            return deptdocId;
        }

        public void setDeptdocId(int deptdocId) {
            this.deptdocId = deptdocId;
        }

        public int getDocId() {
            return docId;
        }

        public void setDocId(int docId) {
            this.docId = docId;
        }

        public Object getDptProfile() {
            return dptProfile;
        }

        public void setDptProfile(Object dptProfile) {
            this.dptProfile = dptProfile;
        }

        public String getDptTel() {
            return dptTel;
        }

        public void setDptTel(String dptTel) {
            this.dptTel = dptTel;
        }

        public String getDptWebsite() {
            return dptWebsite;
        }

        public void setDptWebsite(String dptWebsite) {
            this.dptWebsite = dptWebsite;
        }

        public String getDptame() {
            return dptame;
        }

        public void setDptame(String dptame) {
            this.dptame = dptame;
        }

        public String getEdAddress() {
            return edAddress;
        }

        public void setEdAddress(String edAddress) {
            this.edAddress = edAddress;
        }

        public int getEdFree() {
            return edFree;
        }

        public void setEdFree(int edFree) {
            this.edFree = edFree;
        }

        public String getEdGoodat() {
            return edGoodat;
        }

        public void setEdGoodat(String edGoodat) {
            this.edGoodat = edGoodat;
        }

        public int getEdLevel() {
            return edLevel;
        }

        public void setEdLevel(int edLevel) {
            this.edLevel = edLevel;
        }

        public String getEdLevelName() {
            return edLevelName;
        }

        public void setEdLevelName(String edLevelName) {
            this.edLevelName = edLevelName;
        }

        public String getEdName() {
            return edName;
        }

        public void setEdName(String edName) {
            this.edName = edName;
        }

        public String getEdPic() {
            return edPic;
        }

        public void setEdPic(String edPic) {
            this.edPic = edPic;
        }

        public String getEdProfile() {
            return edProfile;
        }

        public void setEdProfile(String edProfile) {
            this.edProfile = edProfile;
        }

        public int getEdSex() {
            return edSex;
        }

        public void setEdSex(int edSex) {
            this.edSex = edSex;
        }

        public String getEdTelephone() {
            return edTelephone;
        }

        public void setEdTelephone(String edTelephone) {
            this.edTelephone = edTelephone;
        }

        public String getEhAddress() {
            return ehAddress;
        }

        public void setEhAddress(String ehAddress) {
            this.ehAddress = ehAddress;
        }

        public int getEhCity() {
            return ehCity;
        }

        public void setEhCity(int ehCity) {
            this.ehCity = ehCity;
        }

        public String getEhCityName() {
            return ehCityName;
        }

        public void setEhCityName(String ehCityName) {
            this.ehCityName = ehCityName;
        }

        public int getEhCounty() {
            return ehCounty;
        }

        public void setEhCounty(int ehCounty) {
            this.ehCounty = ehCounty;
        }

        public Object getEhCountyName() {
            return ehCountyName;
        }

        public void setEhCountyName(Object ehCountyName) {
            this.ehCountyName = ehCountyName;
        }

        public int getEhHosType() {
            return ehHosType;
        }

        public void setEhHosType(int ehHosType) {
            this.ehHosType = ehHosType;
        }

        public String getEhHosTypeName() {
            return ehHosTypeName;
        }

        public void setEhHosTypeName(String ehHosTypeName) {
            this.ehHosTypeName = ehHosTypeName;
        }

        public int getEhLevel() {
            return ehLevel;
        }

        public void setEhLevel(int ehLevel) {
            this.ehLevel = ehLevel;
        }

        public String getEhLevelName() {
            return ehLevelName;
        }

        public void setEhLevelName(String ehLevelName) {
            this.ehLevelName = ehLevelName;
        }

        public String getEhName() {
            return ehName;
        }

        public void setEhName(String ehName) {
            this.ehName = ehName;
        }

        public int getEhProvince() {
            return ehProvince;
        }

        public void setEhProvince(int ehProvince) {
            this.ehProvince = ehProvince;
        }

        public String getEhProvinceName() {
            return ehProvinceName;
        }

        public void setEhProvinceName(String ehProvinceName) {
            this.ehProvinceName = ehProvinceName;
        }

        public Object getEhRemark() {
            return ehRemark;
        }

        public void setEhRemark(Object ehRemark) {
            this.ehRemark = ehRemark;
        }

        public Object getEhTel() {
            return ehTel;
        }

        public void setEhTel(Object ehTel) {
            this.ehTel = ehTel;
        }

        public String getEhWebsite() {
            return ehWebsite;
        }

        public void setEhWebsite(String ehWebsite) {
            this.ehWebsite = ehWebsite;
        }

        public int getHid() {
            return hid;
        }

        public void setHid(int hid) {
            this.hid = hid;
        }
    }

    @Override
    public String toString() {
        return "HotDoctorResponse{" +
                "detailMsg='" + detailMsg + '\'' +
                ", flag=" + flag +
                ", msg='" + msg + '\'' +
                ", number='" + number + '\'' +
                ", data=" + data +
                '}';
    }
}
