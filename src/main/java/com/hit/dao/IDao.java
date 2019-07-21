package com.hit.dao;

import java.io.Serializable;

public interface IDao <ID extends Serializable,T>{

	void delete (T entity) throws IllegalArgumentException;
	T find(ID id) throws IllegalArgumentException;
	void save (T entity);
}
