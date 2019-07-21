package com.hit.memory;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.memory.CacheUnit;

import junit.framework.TestCase;

import com.hit.dm.DataModel;
import java.lang.Long;
import java.lang.String;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class CacheUnitTest extends TestCase {
	private String pathFile;
	
	public CacheUnitTest() {
		pathFile = "/Users/tomer/Desktop/TestCache.txt";
	}
	
	@Test
	public void getDataModelsTest() {
		
		IAlgoCache<Long, DataModel<String>> algoT=new LRUAlgoCacheImpl<>(5);
		IDao <Long,DataModel<String>> daoT=new DaoFileImpl<>(pathFile);
		CacheUnit<String> cacheT=new CacheUnit<>(algoT, daoT);		
		File f=new File(pathFile);
		try {
			PrintWriter out= new PrintWriter(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataModel<String> DM1= new DataModel<>((long)1,"test1");
		DataModel<String> DM2= new DataModel<>((long)2,"test2");
		DataModel<String> DM3= new DataModel<>((long)3,"test3");
		DataModel<String> DM4= new DataModel<>((long)4,"test4");
		DataModel<String> DM5= new DataModel<>((long)5,"test5");
		DataModel<String> DM6= new DataModel<>((long)6,"test6");
		DataModel<String> DM7= new DataModel<>((long)7,"test7");		
		DataModel<String> DM8= new DataModel<>((long)8,"test8");

		daoT.save(DM1);
		daoT.save(DM2);
		daoT.save(DM3);
		daoT.save(DM4);
		daoT.save(DM5);
		daoT.save(DM6);
		daoT.save(DM7);
		daoT.save(DM8);

		
		/*algoT.putElement((long) 1, DM1);
		algoT.putElement((long) 2, DM2);
		algoT.putElement((long) 3, DM3);
		algoT.putElement((long) 4, DM4);
		algoT.putElement((long) 5, DM5);
		algoT.putElement((long) 6, DM6);
		algoT.putElement((long) 7, DM7);
		algoT.putElement((long) 8, DM8);*/

		
		/*out.print("1"+" "+"test1"+'\n');
		out.print("2"+" "+"test2"+'\n');
		out.print("3"+" "+"test3"+'\n');
		out.print("4"+" "+"test4"+'\n');
		out.print("5"+" "+"test5"+'\n');
		out.print("6"+" "+"test6"+'\n');
		out.print("7"+" "+"test7"+'\n');
		out.print("8"+" "+"test8"+'\n');
		out.close();*/
		
		Long[] IDs=new Long[] {(long)1,(long)2,(long)3,(long)4,(long)5,(long)6,(long)7,(long)8};
		DataModel[]test;
		
		try {
			test=cacheT.getDataModels(IDs);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		daoT.delete(DM3);

	}	
}


