package com.hibernate4all.tutorial.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hibernate4all.tutorial.config.PersitenceConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersitenceConfig.class })
@SqlConfig(dataSource = "dataSourceH2", transactionManager = "transactionManager")
@Sql({ "/datas/datas-test.sql" })
public class MovieServiceTest {

	@Autowired
	private MovieService movieService;

	@Test
	public void updateDescription_casNominal() {
		movieService.updateDescription(-2L, "super film mais j'ai oublié le pitch");
	}

}
