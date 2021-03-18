# canchas schema

# --- !Ups

create table canchas (
 id int(11) not null auto_increment,
 nombre varchar(100) not null,
 numero_max_jugadore int(8) not null,
 primary key (id)
);
 insert into
# --- !Downs

DROP TABLE canchas;
