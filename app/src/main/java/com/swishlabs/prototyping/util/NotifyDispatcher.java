package com.swishlabs.prototyping.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A event dispatch policy. It is like {@link }
 *
 * @param <T>
 */
public class NotifyDispatcher<T> {
		
	private Map<T, Set<IDataSourceListener<T>>> mDataSourceListenerMap = new ConcurrentHashMap<T, Set<IDataSourceListener<T>>>();
		
	public void registerDataListener(T t, IDataSourceListener<T> listener) {
		if (listener == null) {
			return;
		}
		Set<IDataSourceListener<T>> listetenerList = mDataSourceListenerMap.get(t);
		if (listetenerList == null) {
			listetenerList = new HashSet<IDataSourceListener<T>>();
			mDataSourceListenerMap.put(t, listetenerList);
		}
		listetenerList.add(listener);
	}

	public void unRegisterDataListener(IDataSourceListener<T> listener) {
		Set<Entry<T, Set<IDataSourceListener<T>>>> sets = mDataSourceListenerMap.entrySet();
		for (Entry<T, Set<IDataSourceListener<T>>> item : sets) {
			Set<IDataSourceListener<T>> listenerList = item.getValue();
			listenerList.remove(listener);
		}
	}
	
	/**
	 * Notify a data source changed for each registered listener by speical
	 * type.
	 * 
	 * @param type
	 */
	public void notifyDataChanged(T type) {
		Set<IDataSourceListener<T>> sets = mDataSourceListenerMap.get(type);
		if (sets != null) {
			for (IDataSourceListener<T> listener : sets) {
				if (listener != null) {
					listener.onChange(type);
				}
			}
		}
	}
	
	/**
	 * A callback interface.
	 *
	 * @param <T>
	 */
	public interface IDataSourceListener<T> {				
		void onChange(T type);
		
	}
	
}