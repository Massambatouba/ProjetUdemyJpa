truncate table Review,Movie,Genre,Movie_Genre,Movie_Details,Award;

insert into Movie (name,certification, id) values ('Inception', 1,-1);
insert into Movie (name,certification, id) values ('Memento', 2,-2);

insert into Review (author, content, movie_id, id) values ('max', 'au top !', -1, -1);
insert into Review (author, content, movie_id, id) values ('ernest', 'bof bof', -1, -2);

insert into Genre (name, id) values ('Action', -1);