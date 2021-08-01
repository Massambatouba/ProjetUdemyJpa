package com.hibernate4all.tutorial.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hibernate4all.tutorial.config.PersistenceConfigTest;
import com.hibernate4all.tutorial.domain.Certification;
import com.hibernate4all.tutorial.domain.Movie;
import com.hibernate4all.tutorial.domain.Review;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceConfigTest.class })
@SqlConfig(dataSource = "dataSourceH2", transactionManager = "transactionManager")
@Sql({ "/datas/datas-test.sql" })
public class MovieRepositoryTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepositoryTest.class);

	@Autowired
	private MovieRepository repository;

	@Test
	public void save_casNominal() {
		Movie movie = new Movie().setName("Inception")
				.setDescription("test")
				.setCertification(Certification.INTERDIT_MOINS_12);
		repository.persist(movie);
		assertThat(movie.getId()).as("le movie aurait du être persisté").isNotNull();
	}

	@Test
	public void associationSave_casNominal() {
		Movie movie = new Movie().setName("Fight Club")
				.setCertification(Certification.INTERDIT_MOINS_12)
				.setDescription("Le fight club n'existe pas");
		Review review1 = new Review().setAuthor("max").setContent("super film !");
		Review review2 = new Review().setAuthor("jp").setContent("au top!");
		movie.addReview(review1);
		movie.addReview(review2);
		repository.persist(movie);
		assertThat(review1.getId()).as("les reviews aurait du être persistées par cascade").isNotNull();
	}

	@Test
	public void update_casNotFound() {
		assertThrows(JpaObjectRetrievalFailureException.class, () -> {
			Movie movie = new Movie();
			movie.setId(-10L);
			movie.setName("Inception 2");
			repository.update(movie);
		});
	}

	@Test
	public void merge_casSimule() {
		Movie movie = new Movie();
		movie.setName("Inception 2");
		movie.setId(-1L);
		Movie mergedMovie = repository.merge(movie);
		assertThat(mergedMovie.getName()).as("le nom du film n'a pas été mis à jour").isEqualTo("Inception 2");
	}

	@Test
	public void find_casNominal() {
		Movie memento = repository.find(-2L);
		assertThat(memento.getName()).as("mauvais film récupéré").isEqualTo("Memento");
		assertThat(memento.getCertification()).as("le converter n'a pas fonctionné")
				.isEqualTo(Certification.INTERDIT_MOINS_12);
	}

	@Test
	public void getAll_casNominal() {
		List<Movie> movies = repository.getAll();
		assertThat(movies).as("l'ensemble des films n'a pas été récupéré").hasSize(2);
	}

	@Test
	public void remove_casNominal() {
		repository.remove(-2L);

		List<Movie> movies = repository.getAll();
		assertThat(movies).as("le film n'a pas été supprimé").hasSize(1);
	}

	@Test
	public void getReference_casNominal() {
		Movie movie = repository.getReference(-2L);
		assertThat(movie.getId()).as("la référence n'a pas été correctement chargée").isEqualTo(-2L);
	}

	@Test
	public void getReference_fail() {
		assertThrows(LazyInitializationException.class, () -> {
			Movie movie = repository.getReference(-2L);
			LOGGER.trace("movie name : " + movie.getName());
			assertThat(movie.getId()).as("la référence n'a pas été correctement chargée").isEqualTo(-2L);
		});
	}

}
