package com.hibernate4all.tutorial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hibernate4all.tutorial.domain.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryExtended {

	List<Movie> findByName(String searchString);

	List<Movie> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String desc);
}
