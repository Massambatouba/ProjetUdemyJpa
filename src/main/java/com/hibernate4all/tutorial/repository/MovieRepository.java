package com.hibernate4all.tutorial.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.hibernate4all.tutorial.domain.Movie;
import com.hibernate4all.tutorial.domain.MovieDetails;

@Repository
public class MovieRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	public void persist(Movie movie) {
		entityManager.persist(movie);
	}

	@Transactional
	public void addMovieDetails(MovieDetails movieDetails, Long idMovie) {
		Movie movieRef = getReference(idMovie);
		movieDetails.setMovie(movieRef);
		entityManager.persist(movieDetails);
	}

	public Movie find(Long id) {
		return entityManager.find(Movie.class, id);
	}

	public List<Movie> getAll() {
		return entityManager.createQuery("from Movie", Movie.class).getResultList();
	}

	@Transactional
	public Movie merge(Movie movie) {
		return entityManager.merge(movie);
	}

	@Transactional
	public Movie update(Movie movie) {
		entityManager.getReference(Movie.class, movie.getId());
		return entityManager.merge(movie);
	}

	@Transactional
	public boolean remove(Long l) {
		boolean result = false;
		if (l != null) {
			Movie movie = entityManager.find(Movie.class, l);
			if (movie != null) {
				entityManager.remove(movie);
				result = true;
			}
		}
		return result;
	}

	public Movie getReference(Long l) {
		return entityManager.getReference(Movie.class, l);
	}
}
