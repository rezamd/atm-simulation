package com.mitrais.atm_simulation.repository;

import com.mitrais.atm_simulation.exception.NoDataFoundException;
import com.mitrais.atm_simulation.model.Account;

public interface IBaseRepository<T, Y> {
	T findById(Y id) throws NoDataFoundException;

	Account update(Account account) throws NoDataFoundException;
}
