package com.hibernate4all.tutorial.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false)
	private String name;

	private String description;

	private Certification certification;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "movie")
	private List<Review> reviews = new ArrayList<>();

	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Award> awards = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "movie_genre", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private Set<Genre> genres = new HashSet<>();

	public Movie addGenre(Genre genre) {
		if (genre != null) {
			this.genres.add(genre);
			genre.getMovies().add(this);
		}
		return this;
	}

	public Movie removieGenre(Genre genre) {
		if (genre != null) {
			this.genres.remove(genre);
			genre.getMovies().remove(this);
		}
		return this;
	}

	public Set<Genre> getGenres() {
		return Collections.unmodifiableSet(genres);
	}

	public Movie addReview(Review review) {
		if (review != null) {
			this.reviews.add(review);
			review.setMovie(this);
		}
		return this;
	}

	public Movie removieReview(Review review) {
		if (review != null) {
			this.reviews.remove(review);
			review.setMovie(null);
		}
		return this;
	}

	public List<Review> getReviews() {
		return Collections.unmodifiableList(reviews);
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public Certification getCertification() {
		return certification;
	}

	public Movie setCertification(Certification certification) {
		this.certification = certification;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Movie setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Movie setDescription(String description) {
		this.description = description;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(31);
	}

	public List<Award> getAwards() {
		return Collections.unmodifiableList(awards);
	}

	public Movie addAward(Award award) {
		if (award != null) {
			this.awards.add(award);
			award.setMovie(this);
		}
		return this;
	}

	public Movie removeAward(Award award) {
		if (award != null) {
			this.awards.remove(award);
			award.setMovie(null);
		}
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((!(obj instanceof Movie))) {
			return false;
		}
		Movie other = (Movie) obj;
		if (id == null && other.getId() == null) {
			return Objects.equals(name, other.getName()) && Objects.equals(description, other.getDescription())
					&& Objects.equals(certification, other.getCertification());
		}
		return id != null && Objects.equals(id, other.getId());
	}

	@Override
	public String toString() {
		return "Movie [id=" + id + ", name=" + name + ", description=" + description + ", certification="
				+ certification + "]";
	}

}
