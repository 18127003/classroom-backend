drop table if exists CLASSROOM;
drop table if exists ACCOUNT;


create table ACCOUNT
(
    id bigint primary key auto_increment not null,
    name varchar(255) character set utf8 collate utf8_unicode_ci,
    password varchar(32),
    version  integer default 0
);

create table CLASSROOM
(
    id bigint primary key auto_increment not null,
    name varchar(255) character set utf8 collate utf8_unicode_ci,
    room varchar(255) character set utf8 collate utf8_unicode_ci,
    part varchar(255) character set utf8 collate utf8_unicode_ci,
    topic varchar(255) character set utf8 collate utf8_unicode_ci,
    code varchar(255),
    version  integer default 0
);

