create table USER
(
    ID         bigint auto_increment primary key,
    NAME       varchar(100),
    TEL        varchar(20) unique,
    AVATAR_URL varchar(1024),
    ADDRESS    VARCHAR(1024),
    CREATED_AT timestamp,
    UPDATED_AT timestamp
);


INSERT INTO USER(ID, NAME, TEL, AVATAR_URL, ADDRESS)
VALUES (1, 'user1', '13912345678', 'http://url', '火星')