package com.eztcn.user.eztcn.activity.mine;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.QuestionAdapter;

/**
 * @title 帮助与反馈
 * @describe
 * @author ezt
 * @created 2014年10月29日
 */
public class HelpTicklingActivity extends FinalActivity {

	@ViewInject(R.id.questionList)//, itemClick = "onItemClick"
	private ListView questionList;
	@ViewInject(R.id.tickling)//, click = "onClick"
	private Button tickling;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_ticking);
		ViewUtils.inject(HelpTicklingActivity.this);
		loadTitleBar(true, "帮助与反馈", null);
		initQuestion();
	}

	public void onBackClick(View v) {
		finish();
	}
	
	@OnClick(R.id.tickling)
	private void ticklingClick(View v){
		Intent intent = new Intent(this, TicklingContentActivity.class);
		startActivity(intent);
	}

//	public void onClick(View v) {
//		if (v.getId() == R.id.tickling) {
//			Intent intent = new Intent(this, TicklingContentActivity.class);
//			startActivity(intent);
//		}
//	}

	public void initQuestion() {
		String[] q = getResources().getStringArray(R.array.question);
		String[] r = getResources().getStringArray(R.array.replyContent);
		QuestionAdapter adapter = new QuestionAdapter(getApplicationContext(),
				q, r);
		questionList.setAdapter(adapter);
	}

}
