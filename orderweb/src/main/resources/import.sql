
CREATE database orderdemo default character set utf8;
use orderdemo;


CREATE TABLE orderdemo ( id int(11) NOT NULL AUTO_INCREMENT,
  userid varchar(100) NOT NULL,
  description text NOT NULL,
  step varchar(100) NOT NULL,
  startTime date NOT NULL,
  completeTime date,
  PRIMARY KEY (id) );
  
CREATE TABLE orderstep ( id int(11) NOT NULL AUTO_INCREMENT,
  step varchar(100) NOT NULL,
  startTime date NOT NULL,
  completeTime date,
  PRIMARY KEY (id, step) );
