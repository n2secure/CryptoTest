CREATE TABLE Security
(securityId text NOT NULL,
securityType text NOT NULL,
optionType  text,
underlyngId text,
expirationDate text,
strikePrice real,
name text);

insert into Security
values
('AAPL','Equity',null,null,null,null,'Apple');

insert into Security
values
('TSLA','Equity',null,null,null,null,'Tesla');

insert into Security
values
('AMZN','Equity',null,null,null,null,'Amazon');

insert into Security
values
('GOOG','Equity',null,null,null,null,'Google');

insert into Security
values
('BMW','Equity',null,null,null,null,'Bmw');


insert into Security
values
('AAPL-OCT-2024-110-C','Option','Call','AAPL','2024-10-20',110,'Apple');

insert into Security
values
('AAPL-OCT-2024-110-P','Option','Put','AAPL','2024-10-20',110,'Apple');

insert into Security
values
('TSLA-NOV-2024-400-C','Option','Call','TSLA','2024-11-20',400,'Tesla');

insert into Security
values
('TSLA-DEC-2024-400-P','Option','Put','TSLA','2024-12-20',400,'Tesla');

insert into Security
values
('GOOG-NOV-2024-400-C','Option','Call','GOOG','2024-11-20',400,'Google');

insert into Security
values
('GOOG-DEC-2024-400-P','Option','Put','GOOG','2024-12-20',400,'Google');
