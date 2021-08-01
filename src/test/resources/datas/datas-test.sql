SET REFERENTIAL_INTEGRITY FALSE;
truncate table Movie;
truncate table Review;
SET REFERENTIAL_INTEGRITY TRUE;

insert into Movie (name,certification, id) values ('Inception', 1,-1L);
insert into Movie (name,certification, id) values ('Memento', 2,-2L);