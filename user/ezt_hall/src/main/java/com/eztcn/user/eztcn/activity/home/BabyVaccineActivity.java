package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.List;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.widget.ListView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.BabyVaccineAdapter;
import com.eztcn.user.eztcn.bean.Vaccine;

public class BabyVaccineActivity extends FinalActivity {
	/**
	 * 宝宝疫苗listView
	 */
	@ViewInject(R.id.list_baby_vanc)
	private ListView lvBabyVac;
	private BabyVaccineAdapter babyVaccineAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baby_vaccine);
		ViewUtils.inject(BabyVaccineActivity.this);
		loadTitleBar(true, "宝宝疫苗", null);

	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		babyVaccineAdapter = new BabyVaccineAdapter(mContext);
		lvBabyVac.setAdapter(babyVaccineAdapter);
		loadVaccineDate();
	}

	/**
	 * 加载宝宝疫苗数据
	 */
	private void loadVaccineDate() {
		List<Vaccine> vaccine_list = new ArrayList<Vaccine>();
		Vaccine vaccine = new Vaccine("24小时内", "乙肝", "第一针", "乙肝", "上臂三角肌，肌内注射");
		vaccine_list.add(vaccine);
		Vaccine vaccine1_0 = new Vaccine("1个月", "卡介苗", "初种", " 结核病",
				" 上臂三角肌中部，皮内");
		vaccine_list.add(vaccine1_0);
		Vaccine vaccine1_1 = new Vaccine("1个月", "乙肝", "第二针", "乙肝", "上臂三角肌,肌内注射");
		vaccine_list.add(vaccine1_1);
		Vaccine vaccine2_0 = new Vaccine("2个月", "糖丸", "第一针", "小儿麻痹", "口服");
		vaccine_list.add(vaccine2_0);
		Vaccine vaccine3_0 = new Vaccine("3个月", "糖丸", "第二针", "小儿麻痹", "口服");
		vaccine_list.add(vaccine3_0);
		Vaccine vaccine3_1 = new Vaccine("3个月", "百白破三联", "第一针", "百日咳、白喉、破伤风",
				"臂部外上1/4或上臂三角肌，肌内注射");
		vaccine_list.add(vaccine3_1);
		Vaccine vaccine4_0 = new Vaccine("4个月", "糖丸", "第三针", "小儿麻痹", "口服");
		vaccine_list.add(vaccine4_0);
		Vaccine vaccine4_1 = new Vaccine("4个月", "百白破三联", "第二针", "百日咳、白喉、破伤风",
				"臂部外上1/4或上臂三角肌，肌内注射");
		vaccine_list.add(vaccine4_1);
		Vaccine vaccine5_0 = new Vaccine("5个月", "百白破三联", "第三针", "百日咳、白喉、破伤风",
				"臂部外上1/4或上臂三角肌，肌内注射");
		vaccine_list.add(vaccine5_0);
		Vaccine vaccine6_0 = new Vaccine("6个月", "乙肝", "第三针", "乙肝", "上臂三角肌,肌内注射");
		vaccine_list.add(vaccine6_0);
		Vaccine vaccine6_1 = new Vaccine("6个月", "A群流脑", "第一针", "A群流行性脑炎",
				"上臂外侧三角肌附着处，皮下注射");
		vaccine_list.add(vaccine6_1);
		Vaccine vaccine8_0 = new Vaccine("8个月", "麻风", "第一针", "麻疹、风疹",
				"上臂外侧三角肌附着处，皮下注射");
		vaccine_list.add(vaccine8_0);
		Vaccine vaccine8_1 = new Vaccine("8个月", "乙脑减毒", "第一针", "流行病性乙型脑炎",
				"上臂外侧三角肌附着处，皮下注射");
		vaccine_list.add(vaccine8_1);
		Vaccine vaccine9_0 = new Vaccine("9个月", "A群流脑", "第二针", " A群流行性脑炎",
				"上臂外侧三角肌附着处，皮下注射");
		vaccine_list.add(vaccine9_0);
		Vaccine vaccine1doc5_0 = new Vaccine("1.5周岁", "百白破三联", "第四针",
				"百日咳、白喉、破伤风", "臂部外上1/4或上臂三角肌，肌内注射");
		vaccine_list.add(vaccine1doc5_0);
		Vaccine vaccine1doc5_1 = new Vaccine("1.5周岁", "麻腮风", "第二针",
				"麻疹、风疹、腮腺炎", "上臂外侧三角肌附着处，皮下注射");
		vaccine_list.add(vaccine1doc5_1);
		Vaccine vaccine1doc5_2 = new Vaccine("1.5周岁", "甲肝灭活", "第一针", "甲型肝炎",
				"上臂外侧三角肌附着处，皮下注射");
		vaccine_list.add(vaccine1doc5_2);
		Vaccine vaccine2doc0_0 = new Vaccine("2周岁", "乙脑减毒", "第二针", "流行病性乙型脑炎",
				"上臂外侧三角肌附着处，皮下注射");
		vaccine_list.add(vaccine2doc0_0);
		Vaccine vaccine2doc0_1 = new Vaccine("2周岁", "甲肝灭活", "第二针", "甲型肝炎",
				"上臂外侧三角肌附着处，皮下注射");
		vaccine_list.add(vaccine2doc0_1);
		Vaccine vaccine3doc0 = new Vaccine("3周岁", "A+C群流脑", "第一针", "A、C群流行性脑炎",
				"上臂外侧三角肌附着处，皮下注射");
		vaccine_list.add(vaccine3doc0);
		Vaccine vaccine4doc0 = new Vaccine("4周岁", "糖丸", "第四针", "小儿麻痹", "口服");
		vaccine_list.add(vaccine4doc0);
		Vaccine vaccine6doc0 = new Vaccine("4周岁", "A+C群流脑", " 第二针",
				" A、C群流行性脑炎", "上臂外侧三角肌附着处，皮下注射");
		vaccine_list.add(vaccine6doc0);
		Vaccine vaccine6doc1 = new Vaccine("4周岁", "二联", "一针", "白喉、破伤风",
				"上臂三角肌，肌内注射");
		vaccine_list.add(vaccine6doc1);
		babyVaccineAdapter.setList(vaccine_list);
	}
}
