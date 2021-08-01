package com.hibernate4all.tutorial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hibernate4all.tutorial.domain.MovieDetails;

public interface MovieDetailsRepository extends JpaRepository<MovieDetails, Long>, MovieDetailsRepositoryExtended {

	@Override
	@EntityGraph(attributePaths = { "movie" })
	List<MovieDetails> findAll();

}
