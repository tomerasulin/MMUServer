package com.hit.services;

import java.io.IOException;
import java.util.ArrayList;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.SecondChance;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;

public class CacheUnitService<T> {
	private static final String PATH = "/Users/tomer/Desktop/TestCache.txt";
	private static final int CAHCE_CAPACITY = 60;
	private CacheUnit<T> m_cache;
	private IAlgoCache<Long, DataModel<T>> mIAlgo;
	private IDao<Long, DataModel<T>> mDao;
	private Long[] m_long;
	private int numOfDM = 0;
	private int numOfRequests = 0;

	public CacheUnitService() {
		mIAlgo = new SecondChance<>(CAHCE_CAPACITY);
		mDao = new DaoFileImpl<>(PATH);
		m_cache = new CacheUnit<T>(mIAlgo, mDao);
	}

	public boolean update(DataModel<T>[] dataModels) {
		ArrayList<DataModel<T>> temp = new ArrayList();
		for (int i = 0; i < dataModels.length; i++) {
			DataModel<T> curr = dataModels[i];
			if (mIAlgo.getElement(curr.getDataModelId()) != null) {
				temp.add(curr);
			}
		}

		boolean isOk = temp.size() == dataModels.length;
		if (isOk) {
			for (DataModel<T> dm : dataModels) {
				mIAlgo.removeElement(dm.getDataModelId());
				mIAlgo.putElement(dm.getDataModelId(), dm);
				mDao.save(dm);
			}
		}
		numOfDM += dataModels.length;
		incrementNumOfRequest();
		return isOk;
	}

	public boolean delete(DataModel<T>[] dataModels) {
		boolean res = false; // if we don't remove it will stay false
		Long key;
		DataModel<T> delteModel;
		m_long = new Long[dataModels.length];
		for (int i = 0; i < dataModels.length; i++) //
			m_long[i] = dataModels[i].getDataModelId();
		for (int i = 0; i < dataModels.length; i++) {
			key = m_long[i];
			if (mIAlgo.getElement(key) != null)// it means the ID we want to delete exist on cache
			{
				mIAlgo.removeElement(key);
				res = true;
			} else if ((delteModel = mDao.find(key)) != null)// it means the ID we want to delete exist on file
			{
				mDao.delete(delteModel);
				res = true;
			}
		}
		numOfDM += dataModels.length;
		incrementNumOfRequest();
		return res;
	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		DataModel<T>[] dataModel_arr = null;
		m_long = new Long[dataModels.length];
		for (int i = 0; i < dataModels.length; i++) {
			mDao.save(dataModels[i]);
			m_long[i] = dataModels[i].getDataModelId();
		}
		try {
			dataModel_arr = m_cache.getDataModels(m_long);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		numOfDM += dataModels.length;
		incrementNumOfRequest();
		return dataModel_arr;

	}

	public String getStatistics() {
		return new StringBuilder().append("Capacity:" + CAHCE_CAPACITY + "\n").append("Algorithm: SecondChance\n")
				.append("Total number of requests:").append(numOfRequests).append("\n")
				.append("Total number of DataModel (GET/UPDATE/DELETE requests):").append(numOfDM).append("\n")
				.append("Total number of DataModel swaps(from Cahce to Disk):").append(CacheUnit.totalSwaps).toString();
	}

	public int getNumOfRequests() {
		return numOfRequests;
	}

	public void setNumOfRequests(int numOfRequests) {
		this.numOfRequests = numOfRequests;
	}

	public void incrementNumOfRequest() {
		numOfRequests++;
	}
}
