package com.hibernate4all.tutorial.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Genre {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	@ManyToMany(mappedBy = "genres")
	private Set<Movie> movies = new HashSet<>();

	private Genre() {
	}

	public Genre(String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Set<Movie> getMovies() {
		return movies;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((!(obj instanceof Genre))) {
			return false;
		}
		Genre other = (Genre) obj;
		return Objects.equals(name, other.getName());
	}

	@Override
	public String toString() {
		return "Genre [id=" + id + ", name=" + name + "]";
	}

}
