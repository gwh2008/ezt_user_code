package com.eztcn.user.hall.model;

import java.util.List;

/**
 * @Author: lizhipeng
 * @Data: 16/6/8 下午6:14
 * @Description:
 */
public class OneThousandResponse implements IModel{

    /**
     * msg : 获取成功
     * flag : true
     * data : [{"title":"高考倒计时，调整考前心态很关键","subTitle":"      面对即将到来的高考，孩子睡不好、吃不好、每次模拟考后都会发现有不该错的错题，他们会担心、","imageUrl":"/image/files/20160603/1464942580179.jpg","articleUrl":"/ezt_p1/html/article_preview.html?ArticleID=318"},{"title":"涂了防晒霜就不怕晒了？夏季护肤躲开四个误区","subTitle":"　　随着生活水平的提高，护肤不再只是\u201c爱美一族\u201d日常的\u201c必修课\u201d，也是普通人远离皮肤病、追求健康的目","imageUrl":"/image/files/20160603/1464932870880.jpg","articleUrl":"/ezt_p1/html/article_preview.html?ArticleID=302"},{"title":"陪孩子时玩手机 拖累孩子注意力","subTitle":"　　现在许多家长陪伴孩子时并不专心，经常做别的事情比如玩手机。美国一项研究显示，这样可能导致孩子在注","imageUrl":"/image/files/20160602/1464852141613.jpg","articleUrl":"/ezt_p1/html/article_preview.html?ArticleID=289"},{"title":"宝宝过量吃盐有这些危害","subTitle":"　　盐，作为我们日常生活中的常见的调味品，很多妈妈在煮饭的时候都会加进去让饭菜更有味道，但是，宝宝吃","imageUrl":"/image/files/20160602/1464851623207.jpg","articleUrl":"/ezt_p1/html/article_preview.html?ArticleID=287"},{"title":"\u201c第三年龄\u201d--肠道年龄","subTitle":"　你是否经常被便秘、腹泻、腹痛、痔疮等问题困扰？是否体味加重，并出现难闻口气？是否感到免疫力下降，频","imageUrl":"/image/files/20160530/1464587490040.jpg","articleUrl":"/ezt_p1/html/article_preview.html?ArticleID=240"},{"title":"樱桃靓美 三类人切记要忌食","subTitle":"　　樱桃是立夏前后上市较早的一种乔木果实，被称为夏果第一鲜。樱桃颜色艳丽、味道甘美、营养丰富，既含碳","imageUrl":"/image/files/20160530/1464586844439.jpg","articleUrl":"/ezt_p1/html/article_preview.html?ArticleID=237"},{"title":"抽筋、撞到麻筋、踢到脚趾\u2026要痛死了怎么办？","subTitle":"　　虽然说疼痛只是一种相对的感觉，但不小心切到手指或是脚指头踢到门槛上还是会让人痛苦不堪。美国媒体健","imageUrl":"/image/files/20160527/1464327302372.jpg","articleUrl":"/ezt_p1/html/article_preview.html?ArticleID=236"},{"title":"人体免疫系统衰老趋势图绘出","subTitle":"　　人们常说，年纪大了免疫力会下降。那么随着年龄的增长，人体内的免疫系统是如何发生变化的？衰老有没有","imageUrl":"/image/files/20160527/1464318391282.jpg","articleUrl":"/ezt_p1/html/article_preview.html?ArticleID=235"},{"title":"抓住时机狠狠地给健康充电","subTitle":"　　夏初，万物都紧紧抓住这一黄金时光生长。被称为\u201c万物之灵\u201d的人类，也应抓住时机，狠狠给自己的健康充","imageUrl":"/image/files/20160527/1464316698627.jpg","articleUrl":"/ezt_p1/html/article_preview.html?ArticleID=234"},{"title":"医学关键数字","subTitle":"　　64%\n\n　　英国皇家心理医师学会和伦敦国王学院精神病学研究所专家发现，在18岁~24岁人群中，","imageUrl":"/image/files/20160527/1464316566616.jpg","articleUrl":"/ezt_p1/html/article_preview.html?ArticleID=233"}]
     * apistatus : STATUS_SUCCEDED
     * apimessage : null
     */

    private String msg;
    private boolean flag;
    private String apistatus;
    private Object apimessage;
    /**
     * title : 高考倒计时，调整考前心态很关键
     * subTitle :       面对即将到来的高考，孩子睡不好、吃不好、每次模拟考后都会发现有不该错的错题，他们会担心、
     * imageUrl : /image/files/20160603/1464942580179.jpg
     * articleUrl : /ezt_p1/html/article_preview.html?ArticleID=318
     */

    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getApistatus() {
        return apistatus;
    }

    public void setApistatus(String apistatus) {
        this.apistatus = apistatus;
    }

    public Object getApimessage() {
        return apimessage;
    }

    public void setApimessage(Object apimessage) {
        this.apimessage = apimessage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String title;
        private String subTitle;
        private String imageUrl;
        private String articleUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getArticleUrl() {
            return articleUrl;
        }

        public void setArticleUrl(String articleUrl) {
            this.articleUrl = articleUrl;
        }
    }
}
