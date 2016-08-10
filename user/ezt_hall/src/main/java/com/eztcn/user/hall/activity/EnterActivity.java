package com.eztcn.user.hall.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.adapter.BaseCommonAdapter;
import com.eztcn.user.hall.utils.BitmapToBitmaps;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author: lizhipeng
 * @Data: 16/6/21 上午10:52
 * @Description: 强势入驻界面
 */
public class EnterActivity extends BaseActivity {

    private ListView mListView;
    private TextView mTitle;
    private TextView mLeftBtn;

    @Override
    protected int preView() {
        return R.layout.new_activity_enter;
    }

    @Override
    protected void initView() {
        mLeftBtn = (TextView) findViewById(R.id.left_btn);
        mTitle = (TextView) findViewById(R.id.title_tv);
        mListView = (ListView) findViewById(R.id.enter_activity_lv);
    }

    @Override
    protected void initData() {
        mTitle.setText("强势入驻");
        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Observable<List<Bitmap>> observable = Observable.create(new Observable.OnSubscribe<List<Bitmap>>() {
            @Override
            public void call(Subscriber<? super List<Bitmap>> subscriber) {
                subscriber.onStart();
                subscriber.onNext(BitmapToBitmaps.setBitmap(EnterActivity.this, R.drawable.new_bg_enter, mListView, 15));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Subscriber<List<Bitmap>>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onCompleted() {
                dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                dismissProgressDialog();
            }

            @Override
            public void onNext(List<Bitmap> bitmaps) {
                mListView.setAdapter(new BaseCommonAdapter<Bitmap>(EnterActivity.this, bitmaps) {
                    @Override
                    public void convert(ViewHolder holder, Bitmap item, int position, View convertView, ViewGroup parent) {
                        ImageView img = holder.getView(R.id.new_item_enter_activity_iv);
                        img.setImageBitmap(item);
                    }

                    @Override
                    public ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
                        return ViewHolder.get(mContext, convertView, parent, R.layout.new_item_enter_activity_lv, position);
                    }
                });
            }
        });


    }
}
