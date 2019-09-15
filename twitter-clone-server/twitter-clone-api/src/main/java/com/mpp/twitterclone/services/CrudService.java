package com.mpp.twitterclone.services;

import java.util.List;

/**
 * Created by Jonathan on 9/8/2019.
 */

public interface CrudService<T, ID> {

	List<T> findAll();

	T findById(ID id);

	T create(T object);

	T update(T object, ID id, String currentUsername);

	void delete(T object, String currentUsername);

	void deleteById(ID id, String currentUsername);
}
