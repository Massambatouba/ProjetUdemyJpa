SET REFERENTIAL_INTEGRITY FALSE;
truncate table Movie_Genre;
truncate table Movie;
truncate table Review;
truncate table Genre;
SET REFERENTIAL_INTEGRITY TRUE;

insert into Movie (name,certification, id) values ('Inception', 1,-1L);
insert into Movie (name,certification, id) values ('Memento', 2,-2L);

insert into Review (author, content, movie_id, id) values ('max', 'au top !', -1L, -1L);
insert into Review (author, content, movie_id, id) values ('ernest', 'bof bof', -1L, -2L);

insert into Genre (name, id) values ('Action', -1L);