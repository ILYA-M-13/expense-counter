drop table if exists expenditure;

create table  expenditure (
 id integer AUTO_INCREMENT PRIMARY KEY,
 user_id integer not null,
 expend DOUBLE not null,
 category varchar(25),
 note varchar(100),
 datetime date not null);