package com.hibernate4all.tutorial.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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

import com.hibernate4all.tutorial.config.PersistenceConfig;
import com.hibernate4all.tutorial.domain.Award;
import com.hibernate4all.tutorial.domain.Certification;
import com.hibernate4all.tutorial.domain.Genre;
import com.hibernate4all.tutorial.domain.Movie;
import com.hibernate4all.tutorial.domain.MovieDetails;
import com.hibernate4all.tutorial.domain.Review;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceConfig.class })
@SqlConfig(dataSource = "dataSource", transactionManager = "transactionManager")
@Sql({ "/datas/datas-test.sql" })
public class MovieRepositoryTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovieRepositoryTest.class);

	@Autowired
	private MovieRepository repository;
	
	@Test
	public void Review_ratingValidation() {
		Review review1 = new Review().setAuthor("max").setContent("super film !").setRating(12);
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<Review>> errors = validator.validate(review1);
		assertThat(errors).as("le rating aurait du provoquer une erreur").hasSize(1);
	}

	@Test
	public void createMovie_withAward() {
		Award award = new Award().setName("Best Motion Picture of the Year").setYear(2011);
		Movie movie = new Movie().setName("Inception")
				.setCertification(Certification.INTERDIT_MOINS_12)
				.addAward(award);
		repository.persist(movie);
		assertThat(award.getId()).as("Award aurait du être persisté avec Movie").isNotNull();
	}
	
	@Test
	public void save_casNominal() {
		Movie movie = new Movie().setName("Inception")
				.setDescription("test")
				.setCertification(Certification.INTERDIT_MOINS_12);
		repository.persist(movie);
		assertThat(movie.getId()).as("le movie aurait du être persisté").isNotNull();
	}
	
	@Test
	public void addMovieDetails_casNominal() {
		MovieDetails details = new MovieDetails().setPlot("Intrigue du film Memento trés longue !");
		repository.addMovieDetails(details, -2L);
		assertThat(details.getId()).as("l'entité MovieDetails aurait du être persistée").isNotNull();
	}

	@Test
	public void save_withGenres() {
		Movie movie = new Movie();
		movie.setName("The Social Network");
		Genre bio = new Genre("Biography");
		Genre drama = new Genre("Drama");
		movie.addGenre(bio).addGenre(drama);
		repository.persist(movie);
		assertThat(bio.getId()).as("l'entité aurait du être persistée avec le Movie").isNotNull();
	}

	@Test
	public void merge_withExistingGenre() {
		Movie movie = new Movie();
		movie.setName("The Social Network");
		Genre bio = new Genre("Biography");
		Genre drama = new Genre("Drama");
		Genre action = new Genre("Action");
		action.setId(-1L);
		movie.addGenre(bio).addGenre(drama).addGenre(action);
		Movie m =repository.merge(movie);
		assertThat(m.getGenres()).as("l'entité persistée devrait avoir 3 genres").hasSize(3);
		assertThat(m.getGenres()).as("tous les genres associés à l'entité devrait avoir un id").allMatch(g -> g.getId() != null);
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
	public void associationGet_casNominal() {
		assertThrows(LazyInitializationException.class, () -> {
			Movie movie = repository.find(-1L);
			LOGGER.trace("nombre de reviews : " + movie.getReviews().size());
		});
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
	
	@Test
	public void findByName() {
		List<Movie> result = repository.findByName("Inception");
		assertThat(result).as("il ne devrait y avoir qu'un film correspondant au critère").hasSize(1);
		assertThat(result.get(0).getName()).as("mauvais film récupéré").isEqualTo("Inception");
	}

	@Test
	public void findWithCertificatqueryion_casNominal() {
		List<Movie> result = repository.findWithCertification("<=", Certification.INTERDIT_MOINS_12);
		assertThat(result).as("il devrait y avoir 2 films correspondants au critère de recherche").hasSize(2);
	}
	
	@Test
	public void getMoviesWithAwardsAndReviews_casNominal() {
		List<Movie> m = repository.getMoviesWithAwardsAndReviews();
		assertThat(m).as("il devrait y avoir 2 films récupérés").hasSize(2);
		Movie inception = m.stream().filter(movie -> movie.getId().equals(-1L)).findFirst().get();
		assertThat(inception.getReviews()).as("les reviews n'ont pas été correctement récupérées").hasSize(2);
		assertThat(inception.getAwards()).as("les awards n'ont pas été correctement récupérées").hasSize(4);
	}
}
