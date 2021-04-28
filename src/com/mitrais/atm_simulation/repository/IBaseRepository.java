package com.mitrais.atm_simulation.repository;

import com.mitrais.atm_simulation.exception.NoDataFoundException;

public interface IBaseRepository<T, Y> {
	T findById(Y id) throws NoDataFoundException;
}
