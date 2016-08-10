package com.eztcn.user.eztcn.utils.http;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * @title
 * @describe
 * @author ezt
 * @created 2015��3��5��
 */
public class RequestCache {

	// TODO cache lifeTime

	private static int CACHE_LIMIT = 10;

	@SuppressWarnings("unchecked")
	private LinkedList history;
	private Hashtable<String, Object> cache;

	@SuppressWarnings("unchecked")
	public RequestCache() {
		history = new LinkedList();
		cache = new Hashtable<String, Object>();
	}

	@SuppressWarnings("unchecked")
	public void put(String url, Object data) {
		history.add(url);
		// too much in the cache, we need to clear something
		if (history.size() > CACHE_LIMIT) {
			String old_url = (String) history.poll();
			cache.remove(old_url);
		}
		cache.put(url, data);
	}

	public Object get(String url) {
		return cache.get(url);
	}
}
