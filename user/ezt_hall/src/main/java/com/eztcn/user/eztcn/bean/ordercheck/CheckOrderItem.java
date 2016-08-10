/**
 * 
 */
package com.eztcn.user.eztcn.bean.ordercheck;

import java.io.Serializable;
import java.util.List;

import com.eztcn.user.eztcn.bean.Hospital;

/**
 * @author Liu Gang
 * 
 *         2016年3月14日 下午3:10:19 预约检查项目实体
 */
public class CheckOrderItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Hospital hospital;
	private Guider guider;
	/**
	 * 检查项Id
	 */
	private String checkId;
	/**
	 * 图标id
	 */
	private int iconId;
	/**
	 * 名称
	 */
	private String nameStr;
	/**
	 * 地址
	 */
	private String addr;
	/**
	 * 花费
	 */
	private String costStr;
	/**
	 * 日期
	 */
	private String dateStr;
	/**
	 * 时间
	 */
	private String timeStr;
	/**
	 * 注意事项
	 */
	private List<String> caresStr;
	/**
	 * 预约类型
	 */
	private String orderTypeStr;
	/**
	 * 是否使用e护帮服务
	 */
	private String forceUse;
	/**
	 * 是否被选中
	 */
	private boolean isChecked;
	private String hosId;
	private String hosName;
	private String baseCostStr;
	public String getBaseCostStr() {
		return baseCostStr;
	}

	public void setBaseCostStr(String baseCostStr) {
		this.baseCostStr = baseCostStr;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public String getNameStr() {
		return nameStr;
	}

	public void setNameStr(String nameStr) {
		this.nameStr = nameStr;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	

	public String getCostStr() {
		return costStr;
	}

	public void setCostStr(String costStr) {
		this.costStr = costStr;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public List<String> getCaresStr() {
		return caresStr;
	}

	public void setCaresStr(List<String> caresStr) {
		this.caresStr = caresStr;
	}

	public String getOrderTypeStr() {
		return orderTypeStr;
	}

	public void setOrderTypeStr(String orderTypeStr) {
		this.orderTypeStr = orderTypeStr;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public Guider getGuider() {
		return guider;
	}

	public void setGuider(Guider guider) {
		this.guider = guider;
	}

}
