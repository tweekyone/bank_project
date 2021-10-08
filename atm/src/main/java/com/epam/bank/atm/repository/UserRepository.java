package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.User;

public interface UserRepository{
	User getById(long id);	
}
