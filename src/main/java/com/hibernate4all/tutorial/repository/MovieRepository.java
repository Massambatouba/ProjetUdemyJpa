package com.hibernate4all.tutorial.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import com.hibernate4all.tutorial.domain.Certification;
import com.hibernate4all.tutorial.domain.Movie;
import com.hibernate4all.tutorial.domain.MovieDetails;
import com.hibernate4all.tutorial.domain.Movie_;

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

	public List<Movie> findByName(String searchString) {
		return entityManager.createQuery("select m from Movie m where m.name = :param", Movie.class)
				.setParameter("param", searchString)
				.getResultList();
	}

	public List<Movie> findWithCertification(String operation, Certification certif) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
		Root<Movie> root = query.from(Movie.class);

		Predicate predicat;
		if ("<".equals(operation)) {
			predicat = builder.lessThan(root.get(Movie_.CERTIFICATION), certif);
		} else if ("<=".equals(operation)) {
			predicat = builder.lessThanOrEqualTo(root.get(Movie_.CERTIFICATION), certif);
		} else if ("=".equals(operation)) {
			predicat = builder.equal(root.get(Movie_.CERTIFICATION), certif);
		} else if (">".equals(operation)) {
			predicat = builder.greaterThan(root.get(Movie_.CERTIFICATION), certif);
		} else if (">=".equals(operation)) {
			predicat = builder.greaterThanOrEqualTo(root.get(Movie_.CERTIFICATION), certif);
		} else {
			throw new IllegalArgumentException("valeur de paramètre de recherche incorrect : " + operation);
		}
		query.where(predicat);

		return entityManager.createQuery(query).getResultList();
	}
	

	@Transactional
	public List<Movie> getMoviesWithReviews() {
		return entityManager
				.createQuery("select distinct m from Movie m left join fetch m.reviews", Movie.class)
				.setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
				.getResultList();
	}
}
