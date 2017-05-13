package com.tjing.frame.object;

import java.util.List;
import com.alibaba.fastjson.JSONObject;

public class FormSetting {
	private List<List<JSONObject>> data;

	public List<List<JSONObject>> getData() {
		return data;
	}

	public void setData(List<List<JSONObject>> data) {
		this.data = data;
	}

}
