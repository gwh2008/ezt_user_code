package com.eztcn.user.eztcn.api;

public interface IHttpResult {

	/**
	 * 0为访问接口标记; 1参数为标记是否访问成功;参数2为成功时返回的结果 ; 参数3为访问失败的msg
	 * 
	 * @param object
	 */
	public void result(Object... object);

}
