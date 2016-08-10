/**
 * 
 */
package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @author Liu Gang
 * 
 *         2015年10月10日 上午9:49:47
 * 
 */
public interface ValideApi {
	public void getValide(HashMap<String, Object> params,
			final IHttpResult callBack);
	public void valideGuideDoc(HashMap<String, Object> params,
			final IHttpResult callBack);
}
