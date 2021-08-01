package com.hibernate4all.tutorial.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hibernate4all.tutorial.domain.Movie;
import com.hibernate4all.tutorial.repository.MovieRepository;

@Service
public class MovieService {

	@Autowired
	private MovieRepository repository;

	@Transactional
	public void updateDescription(Long id, String description) {
		Movie movie = repository.findById(id).get();
		movie.setDescription(description);
	}
}
