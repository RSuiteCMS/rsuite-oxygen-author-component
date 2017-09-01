package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.helpers;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;



public class SizeLimitLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

	/** UID */
	private static final long serialVersionUID = -9101346015593858566L;
	private AtomicLong size = new AtomicLong(0);
	
	private long MAX_SIZE = 3 * 1024 * 1024;
	
	@Override
	public V put(K key, V value) {
		
		if (value instanceof SchemaEntry){
			size.addAndGet(((SchemaEntry)value).getSize());
		}
		
		return super.put(key, value);
	}
	
	@Override
	public V remove(Object key) {
		V value = get(key);
		if (value instanceof SchemaEntry){
			size.addAndGet(((SchemaEntry)value).getSize() * -1);
		}
		
		
		return super.remove(key);
	}
	
	@Override
	public void clear() {
		super.clear();
		size = new AtomicLong(0);
	}
	
	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		return size.get() > MAX_SIZE;
	}

	public long getMemorySize() {
		return size.get();
	}
		
}
