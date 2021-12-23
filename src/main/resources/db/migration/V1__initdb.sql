create table ACCOUNT
(
    id bigint primary key auto_increment not null,
    first_name varchar(150) character set utf8 collate utf8_unicode_ci,
    last_name varchar(100) character set utf8 collate utf8_unicode_ci,
    name varchar(255) character set utf8 collate utf8_unicode_ci,
    password varchar(255) not null,
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
    constraint unique (account_id, classroom_id),
    constraint fk_account foreign key (account_id) references account (id),
    constraint fk_classroom foreign key (classroom_id) references classroom (id)
);

create table ASSIGNMENT
(
    id bigint not null auto_increment primary key ,
    name varchar(255) character set utf8 collate utf8_unicode_ci,
    description text character set utf8 collate utf8_unicode_ci,
    points int default 100,
    deadline datetime,
    created_at datetime not null,
    position int not null,
    classroom bigint not null,
    creator bigint not null,
    version integer default 0,
    key fk_assignment_classroom (classroom),
    key fk_assignment_creator (creator),
    constraint fk_assignment_creator foreign key (creator) references ACCOUNT (id),
    constraint fk_assignment_classroom foreign key (classroom) references CLASSROOM (id)
);

create table STUDENT_INFO
(
    id bigint not null auto_increment primary key,
    student_id varchar(10) not null unique,
    name varchar(255) character set utf8 collate utf8_unicode_ci not null ,
    account_id bigint,
    version integer default 0,
    key fk_student_account (account_id),
    constraint fk_student_account foreign key (account_id) references ACCOUNT (id)
);

create table STUDENT_INFO_CLASSROOM
(
    id bigint not null auto_increment primary key,
    student_info bigint not null,
    classroom_id bigint not null,
    version integer default 0,
    key fk_student_classroom (classroom_id),
    key fk_student_info_classroom (student_info),
    unique key (student_info, classroom_id),
    constraint fk_student_info_classroom foreign key (student_info) references STUDENT_INFO (id),
    constraint fk_student_classroom foreign key (classroom_id) references CLASSROOM (id)
);

create table GRADE_COMPOSITION
(
    id bigint not null auto_increment primary key,
    assignment_id bigint,
    status varchar(20) default 'GRADING',
    version integer default 0,
    key fk_grade_assignment (assignment_id),
    constraint fk_grade_assignment foreign key (assignment_id) references ASSIGNMENT(id)
);

create table SUBMISSION
(
    id bigint not null auto_increment primary key,
    student_info bigint not null,
    classroom_id bigint not null,
    grade integer,
    grade_composition bigint not null,
    version integer default 0,
    key fk_submission_student (student_info, classroom_id),
    key fk_submission_assignment (grade_composition),
    constraint fk_submission_student foreign key (student_info, classroom_id) references STUDENT_INFO_CLASSROOM (student_info, classroom_id),
    constraint fk_submission_assignment foreign key (grade_composition) references GRADE_COMPOSITION (id)
);

create table ADMIN
(
    id bigint not null auto_increment primary key,
    name varchar(255) character set utf8 collate utf8_unicode_ci,
    password varchar(255) not null,
    email varchar(255) unique not null,
    version  integer default 0
);

create table LOCKED_ACCOUNT
(
    id bigint not null auto_increment primary key,
    account_id bigint not null,
    version integer default 0,
    key fk_locked_account_id (account_id),
    constraint fk_locked_account_id foreign key (account_id) references ACCOUNT (id)
);

create table GRADE_REVIEW
(
    id bigint not null auto_increment primary key,
    request_by bigint not null,
    submission bigint not null,
    expect_grade integer,
    explanation text character set utf8 collate utf8_unicode_ci,
    status varchar(20) default 'PENDING',
    version integer default 0,
    key fk_request_account (request_by),
    key fk_review_submission (submission),
    constraint fk_request_account foreign key (request_by) references ACCOUNT (id),
    constraint fk_review_submission foreign key (submission) references SUBMISSION (id)
);

create table COMMENT
(
    id bigint not null auto_increment primary key,
    grade_review bigint not null,
    author bigint not null,
    content text character set utf8 collate utf8_unicode_ci,
    version integer default 0,
    constraint fk_comment_author foreign key (author) references ACCOUNT (id)
);

insert into ADMIN (id,name, email, password, version) values
    (1, 'Hai Dang', 'hdang@gmail.com','', 0)
;

