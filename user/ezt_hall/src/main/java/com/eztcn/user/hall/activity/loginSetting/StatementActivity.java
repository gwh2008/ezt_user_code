package com.eztcn.user.hall.activity.loginSetting;
import android.os.Bundle;
import android.widget.TextView;
import com.eztcn.user.R;
import com.eztcn.user.hall.activity.BaseActivity;
import java.io.IOException;
import java.io.InputStream;
/**
 * Created by zll on 2016/6/6.
 * 使用协议和免责声明界面
 */
public class StatementActivity extends BaseActivity {
    private TextView text_statement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "使用协议和免责声明",null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_statement;
    }

    @Override
    protected void initView() {
        text_statement = (TextView) findViewById(R.id.text_statement);
        text_statement.setText(readAssetsFileString()+"");
    }

    @Override
    protected void initData() {

    }
    public String readAssetsFileString() {
        String str = null;
        try {
            InputStream is = getAssets().open("new_statement.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        } catch (IOException e) {
   // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }
}
