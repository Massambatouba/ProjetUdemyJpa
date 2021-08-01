package com.hibernate4all.tutorial.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false)
	private String name;

	private String description;

	private Certification certification;

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
