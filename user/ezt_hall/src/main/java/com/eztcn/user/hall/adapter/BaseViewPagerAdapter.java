package com.eztcn.user.hall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * @Author: lizhipeng
 * @Data: 16/5/4 下午1:34
 * @Description: viewpager 使用fragment时的适配器
 */
public class BaseViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mData;
    private List<String> mTitleData;
    public BaseViewPagerAdapter(FragmentManager fm, List<Fragment> data, List<String> title) {
        super(fm);
        mData = data;
        mTitleData = title;
    }

    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mTitleData.get(position);
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitleData==null||position>mTitleData.size()-1){
            return null;
        }else{
            return mTitleData.get(position);
        }

    }
}
