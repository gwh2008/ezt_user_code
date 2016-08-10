/**
 * 
 */
package com.eztcn.user.eztcn.bean;

import java.io.Serializable;
import java.util.List;

import android.graphics.Bitmap;

import com.eztcn.user.eztcn.bean.compent.IntentParams;

/**
 * @author Liu Gang
 * 
 *         2016年1月4日 下午2:33:21
 * 
 */
public class Function implements Serializable, Comparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	/**
	 * 是否开通
	 */
	private int isOpen;
	private int DrawableId;

	public int getDrawableId() {
		return DrawableId;
	}

	public void setDrawableId(int drawableId) {
		DrawableId = drawableId;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	private Bitmap imageBitmap;
	private String jumpLink; // 跳转到哪个Activity ep. Class.forName “”
	private List<IntentParams> intentParamList; // 跳转时候用的参数 ep. (key,value)

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}

	public String getJumpLink() {
		return jumpLink;
	}

	public void setJumpLink(String jumpLink) {
		this.jumpLink = jumpLink;
	}

	public List<IntentParams> getIntentParamList() {
		return intentParamList;
	}

	public void setIntentParamList(List<IntentParams> intentParamList) {
		this.intentParamList = intentParamList;
	}

	// 重写
	public int compareTo(Object obj) {
		Function f = (Function) obj;
		if (f.isOpen > this.isOpen) {
			return 1;
		} else if (f.isOpen < this.isOpen) {
			return -1;
		} else {
			return 0;
		}
	}

}
