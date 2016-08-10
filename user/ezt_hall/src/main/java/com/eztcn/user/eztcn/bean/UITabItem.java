package com.eztcn.user.eztcn.bean;

import com.eztcn.user.eztcn.customView.tabview.UITabIconView;

import android.view.View;
import android.widget.TextView;


/**
 * @title 底部导航栏实体
 * @describe
 * @author ezt
 * @created 2015年4月28日
 */
public final class UITabItem {  
    public View parent;
    public UITabIconView iconView;//图片
    public TextView labelView;//标签,如首页,我
    public View tipView;//以后有红点之类的
}
