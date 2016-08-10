/**
 * 
 */
package com.eztcn.user.eztcn.activity.home;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @title
 * @describe
 * @author ezt
 * @created 2015年11月11日
 */
public class Activity_Dargon extends FinalActivity {
	@ViewInject(R.id.ic_dragon_text)
	private TextView text1;
	
	@ViewInject(R.id.ic_dragon_textbt)
	private TextView ic_dragon_textbt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dragonintro);
		ViewUtils.inject(Activity_Dargon.this);
//		loadTitleBar(true, "医指通龙卡", null);
		loadTitleBar(true, "银行医管家", null);
		Spanned html1 = Html
				.fromHtml("<div id='u13' class='text'><p><span>&nbsp; &nbsp; &nbsp; </span><span>龙卡医指通联名借记卡由中国建设银行天津市分行与天津市上线科技有限公司（医指通）联合发行，该联名借记卡除具备龙卡借记卡全部金融功能及使用范围以外，持卡人还可享受医指通医疗健康管理服务。</span></p><p><span>医指通健康龙卡是医指通在全国首推的专门针对建行用户发行的银行卡，具有储蓄财富回报及健康管理服务功能，用户只需每年缴纳一定费用的年费即可获得银行稳定的存款利息，及由医指通云就医平台提供的个性化的健康管理服务。“医指通”是首个省级预约挂号统一平台，全网覆盖天津全市39家三甲医院，医指通社区云端覆盖全国539个网点。目前，医指通云就医平台拥有2000多万就医会员，将逐步开放天津市代谢病医院、天津血液病研究院等全国知名医院的快捷就医服务。</span></p><p><span>持本卡用户可以享受专属的电话医生、个性化的就医安排、家庭医生健康管理服务、专属导诊服务等。</span></p><p><span>&nbsp;</span></p><p><span> 详情请咨询客服专席：400-6511-86</span><span>9</span><span>&nbsp;&nbsp; </span></p></div></div>");
		text1.setText(html1);
		Spanned htmlDown = Html
				.fromHtml("  <div id='u38' class='ax_文本'>"

						+ "<!-- Unnamed () -->"
						+ "<div id='u39' class='text'>"
						+ "<p style='text-align:center;'><span style='font-family:'Arial Negreta', 'Arial';font-weight:700;text-align:center;'>服务流程</span></p><p style='text-align:center;'><span style='font-family:'Arial Normal', 'Arial';font-weight:400;text-align:center;'>&nbsp;</span></p><p style='text-align:left;'><span style='font-family:'Arial Normal', 'Arial';font-weight:400;text-align:left;'>1、用户拨打医指通建行龙卡专属健康专线400-6511-869；&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; </span></p><p style='text-align:left;'><span style='font-family:'Arial Normal', 'Arial';font-weight:400;text-align:left;'>2、专席客服将在最快时间内按照用户指定的医院、科室、医生进行预约挂号；&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; </span></p><p style='text-align:left;'><span style='font-family:'Arial Normal', 'Arial';font-weight:400;text-align:left;'>3、当患者指定的医生约满或特殊原因停诊时，客服专席将电话通知用户取消该次预约，并推荐相关科室专家，患者可自由选择改约其他专家号或改期预约，（预约不成功，不扣减次数）；</span></p><p style='text-align:left;'><span style='font-family:'Arial Normal', 'Arial';font-weight:400;text-align:left;'>4、预约成功后，医指通系统自动将就诊时间、医院、科室及医生信息的预约短信发送至用户预留手机；</span></p><p style='text-align:left;'><span style='font-family:'Arial Normal', 'Arial';font-weight:400;text-align:left;'>5、就诊前一日健康秘书与患者取得联系，确认就诊时间、地点，并叮嘱患者携带就医必备物品。</span></p>"
						+ "</div>");
		ic_dragon_textbt.setText(htmlDown);
	}
}
