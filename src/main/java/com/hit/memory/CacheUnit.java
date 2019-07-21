package com.hit.memory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.hit.algorithm.IAlgoCache;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;

public class CacheUnit<T> {

	private IAlgoCache<Long, DataModel<T>> m_algo;
	private IDao<Long, DataModel<T>> m_dao;
	public static int totalSwaps = 0;
	public CacheUnit(IAlgoCache<Long, DataModel<T>> algo, IDao<Long, DataModel<T>> dao) {
		m_algo = algo;
		m_dao = dao;
	}

	public DataModel<T>[] getDataModels(Long[] ids) throws ClassNotFoundException, IOException {
		List<DataModel<T>> res = new LinkedList<DataModel<T>>();

		DataModel<T> dataTempReplace, dataReplace;

		for (Long id : ids) {
			DataModel<T> current = m_algo.getElement(id);
			if (current == null) // dosen't exist on cache
			{ // so exist on file. we check if the cache full, if it is we do DM replacements,
				// if no we do DM Faults.
				dataReplace = m_dao.find(id);
				dataTempReplace = m_algo.putElement(id, dataReplace); // replace on cache according to algo
				if (dataTempReplace != null) { // cache is full
					current = dataTempReplace;
					m_dao.save(current); // update dao on change
					totalSwaps++;
				} else {
					current = dataReplace;
				}
			}
			res.add(current);
		}

		return res.toArray(((DataModel<T>[]) new DataModel<?>[res.size()]));

	}
}
