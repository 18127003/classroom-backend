drop table if exists CLASSROOM;
drop table if exists ACCOUNT;


create table ACCOUNT
(
    id bigint primary key auto_increment not null,
    first_name varchar(150) character set utf8 collate utf8_unicode_ci,
    last_name varchar(100) character set utf8 collate utf8_unicode_ci,
    name varchar(255) character set utf8 collate utf8_unicode_ci,
    password varchar(255) not null,
    student_id varchar(10) unique,
    email varchar(255) unique not null,
    version  integer default 0
);

create table CLASSROOM
(
    id bigint primary key auto_increment not null,
    name varchar(255) character set utf8 collate utf8_unicode_ci not null,
    room varchar(255) character set utf8 collate utf8_unicode_ci,
    part varchar(255) character set utf8 collate utf8_unicode_ci,
    topic varchar(255) character set utf8 collate utf8_unicode_ci,
    description text character set utf8 collate utf8_unicode_ci,
    code varchar(255) unique,
    creator bigint not null,
    version  integer default 0,
    key fk_creator (creator),
    constraint fk_creator foreign key (creator) references account (id)
);

create table ACCOUNT_CLASSROOM
(
    id bigint not null auto_increment primary key,
    account_id bigint not null,
    classroom_id bigint not null,
    role varchar(10) not null default 'TEACHER',
    hidden boolean default FALSE,
    version integer default 0,
    key fk_account (account_id),
    key fk_classroom (classroom_id),
    constraint fk_account foreign key (account_id) references account (id),
    constraint fk_classroom foreign key (classroom_id) references classroom (id)
)

