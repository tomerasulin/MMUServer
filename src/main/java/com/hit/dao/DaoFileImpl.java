package com.hit.dao;

import com.hit.dm.DataModel;
import java.lang.Long;
import java.lang.String;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {

	private String m_filePath;
	private Map<Long, DataModel<T>> IDs;

	public DaoFileImpl(String filePath) {
		m_filePath = filePath;
		IDs = new HashMap<>(100);// dont know exactly limit.
	}

	@Override
	public void delete(DataModel<T> entity) throws IllegalArgumentException {
		// TODO Auto-generated method stub

		try {
			String entityToDel;
			String line;
			entityToDel = entity.toString();
			File file = new File(m_filePath);
			File temp = File.createTempFile("file", ".txt");

			BufferedReader reader = new BufferedReader(new FileReader(file));
			BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
			// we remove the relevant data by copy the original file to new file
			// without the data we want to remove.
			while ((line = reader.readLine()) != null) {
				if (line.equals(entityToDel))
					line.replaceAll(entityToDel, "Null");
				else {
					writer.write(line);
					writer.newLine();
				}
			}
			reader.close();
			writer.close();
			file.delete();
			temp.renameTo(file);

		}

		catch (IllegalArgumentException e) {
			e.getMessage();
			// System.out.println("given entity is null!");
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		}

	}

	@Override
	public DataModel<T> find(Long id) throws IllegalArgumentException {
		// TODO Auto-generated method stub

		DataModel<T> res = null;
		try {
			res = IDs.get(id);
		}

		catch (IllegalArgumentException e) {
			System.out.println("ID is null");
		}
		return res;
	}

	@Override
	public void save(DataModel<T> entity) {
		// TODO Auto-generated method stub
		BufferedWriter writer = null;
		try {
			T tempCon;
			String idToSave, contantToSave;
			idToSave = Long.toString(entity.getDataModelId());
			tempCon = entity.getContent();
			contantToSave = (String) tempCon;//.toString();
			IDs.put(entity.getDataModelId(), entity); // hash for fast find

			writer = new BufferedWriter(new FileWriter(m_filePath, true));
			writer.write(idToSave + " " + contantToSave);// all the file will be write on this order
			writer.newLine();
			writer.close();
		}

		catch (IOException e) {
			System.err.println(e);
		}
	}

}
