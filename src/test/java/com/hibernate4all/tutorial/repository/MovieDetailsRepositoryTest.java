package com.hibernate4all.tutorial.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hibernate4all.tutorial.domain.MovieDetails;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql({ "/datas/datas-test.sql" })
public class MovieDetailsRepositoryTest {

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private MovieDetailsRepository repository;

	@Test
	public void addMovieDetails_casNominal() {
		MovieDetails details = new MovieDetails().setPlot("Intrigue du film Memento trés longue !");
		repository.addMovieDetails(details, -2L);
		assertThat(details.getId()).as("l'entité MovieDetails aurait du être persistée").isNotNull();
	}


	@Test
	@Sql({ "/datas/datas-test-n+1.sql" })
	public void getAllMovieDetails_casNominal() {
		Statistics stats = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
		stats.setStatisticsEnabled(true);
		stats.clear();
		List<MovieDetails> result = repository.findAll();
		assertThat(result).as("l'ensemble des movie details n'a pas été récupéré").hasSize(3);
		assertThat(stats.getPrepareStatementCount()).as("n+1 select sur la requete").isEqualTo(1);

	}
	
}
