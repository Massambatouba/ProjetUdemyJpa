package com.hibernate4all.tutorial.repository;

import java.util.List;

import com.hibernate4all.tutorial.domain.Certification;
import com.hibernate4all.tutorial.domain.Movie;

public interface MovieRepositoryExtended {

	Movie update(Movie movie);

	List<Movie> findWithCertification(String operation, Certification certif);

	List<Movie> getMoviesWithAwardsAndReviews();

}