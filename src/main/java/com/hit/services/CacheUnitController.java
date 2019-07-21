package com.hit.services;

import com.hit.dm.DataModel;

public class CacheUnitController<T> {
	private CacheUnitService<T> service;
	
	public CacheUnitController() {
		service = new CacheUnitService<T>();
	}
	
	public synchronized boolean update(DataModel<T>[] dataModels) {
		return service.update(dataModels);
	}
	
	public synchronized boolean delete(DataModel<T>[] dataModels) {
		return service.delete(dataModels);
	}
	
	public synchronized DataModel<T>[] get(DataModel<T>[] dataModels){
		return service.get(dataModels);
	}
	
	public String getStatistic() {
		return service.getStatistics();
	}
}
