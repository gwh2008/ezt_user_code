package com.eztcn.user.hall.interfaces;

import com.eztcn.user.hall.model.ResultResponse.WeekTimesCountDataResponse;

import java.util.ArrayList;

public interface IFormListener {
	
	void showNum(int index, ArrayList<WeekTimesCountDataResponse> datas);

}
