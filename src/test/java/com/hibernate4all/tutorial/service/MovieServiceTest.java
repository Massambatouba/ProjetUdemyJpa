package com.hibernate4all.tutorial.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql({ "/datas/datas-test.sql" })
public class MovieServiceTest {

	@Autowired
	private MovieService movieService;

	@Test
	public void updateDescription_casNominal() {
		movieService.updateDescription(-2L, "super film mais j'ai oublié le pitch");
	}

}
