package com.mitrais.atm_simulation.repository;

import java.util.List;

public interface IBaseRepository<T, Y> {
	T getById(Y id);
	List<T> getAll();
	T save(T object);
	int update(T persistenceObject);
	int delete(T persistenceObject);
	
}
