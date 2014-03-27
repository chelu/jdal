--
-- Initial data for swing library sample.
-- Obtained with mysqldump --no-create-info --compact --skip-extended-insert -c from mysql database.
--

INSERT INTO `authors` (`id`, `name`, `surname`) VALUES (1,'Rod','Johnson');
INSERT INTO `authors` (`id`, `name`, `surname`) VALUES (2,'Martin','Fowler');
INSERT INTO `authors` (`id`, `name`, `surname`) VALUES (3,'Eric','Evans');
INSERT INTO `authors` (`id`, `name`, `surname`) VALUES (4,'Erich','Gamma');
INSERT INTO `authors` (`id`, `name`, `surname`) VALUES (5,'Frank','Buschmann');
INSERT INTO `authors` (`id`, `name`, `surname`) VALUES (6,'Douglas ','Schmidt');
INSERT INTO `authors` (`id`, `name`, `surname`) VALUES (7,'Michael','Kircher');
INSERT INTO `authors` (`id`, `name`, `surname`) VALUES (8,'Kent','Beck');
INSERT INTO `authors` (`id`, `name`, `surname`) VALUES (9,'Craig','Walls');
INSERT INTO `authors` (`id`, `name`, `surname`) VALUES (10,'Gary','Mak');
INSERT INTO `authors` (`id`, `name`, `surname`) VALUES (11,'Chris','Richardson');
INSERT INTO `categories` (`id`, `name`) VALUES (1,'Java');
INSERT INTO `categories` (`id`, `name`) VALUES (2,'C++');
INSERT INTO `categories` (`id`, `name`) VALUES (3,'Patterns');
INSERT INTO `categories` (`id`, `name`) VALUES (4,'Sofware Design, Architecture');
INSERT INTO `categories` (`id`, `name`) VALUES (5,'C#');
INSERT INTO `readers` (`id`, `name`, `surname`) VALUES (2,'Groucho ','Marx');
INSERT INTO `readers` (`id`, `name`, `surname`) VALUES (3,'Chico','Marx');
INSERT INTO `readers` (`id`, `name`, `surname`) VALUES (4,'Harpo','Marx');
INSERT INTO `readers` (`id`, `name`, `surname`) VALUES (5,'Gummo','Marx');
INSERT INTO `readers` (`id`, `name`, `surname`) VALUES (6,'Zeppo','Marx');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (7,'Domain-driven design: tackling complexity in the heart of software  ','978-0-3211-2521-7',3,1,'2003-08-30');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (8,'Expert One-on-One J2EE Development without EJB','978-0-7645-5831-3',1,1,'2004-01-01');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (9,'Domain-Specific Languages ','978-0-3217.1294-3',2,4,'2010-10-03');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (10,'Refactoring: Improving the Design of Existing Code','978-0-2014-8567-7',2,4,'1999-07-08');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (11,'UML Distilled: A Brief Guide to the Standard Object Modeling Language','978-0-3211-9368-1',2,4,'2003-09-25');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (12,'Analysis Patterns: Reusable Object Models','978-0-2018-9542-1',2,3,'1996-10-19');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (13,'Design Patterns: Elements of Reusable Object-Oriented Software','978-0-2016-3361-0',4,3,'1994-11-10');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (14,'Contributing to Eclipse: Principles, Patterns, and Plug-Ins (The eclipse Series)','978-0-3212-4641-7',4,1,'2004-01-01');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (15,'Pattern-Oriented Software Architecture Volume 1: A System of Patterns','978-0-4719-5869-7',5,1,'1996-08-08');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (16,'Pattern-Oriented Software Architecture Volume 2: Patterns for Concurrent and Networked Objects','978-0-4716-0695-6',6,1,'2000-09-14');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (17,'Pattern-Oriented Software Architecture Volume 4: A Pattern Language for Distributed Computing ','978-0-4700-5902-9',5,3,'2007-05-15');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (18,'Pattern Oriented Software Architecture Volume 5: On Patterns and Pattern ','978-0-4714-8648-0',5,3,'2007-06-11');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (19,'Pattern-Oriented Software Architecture Volume 3: Patterns for Resource Management ','978-0-4708-4525-7',7,3,'2004-06-28');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (20,'Implementation Patterns','978-0-3214-1309-3',8,3,'2007-11-02');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (21,'Test Driven Development: By Example ','978-0-3211-4653-3',8,3,'2002-11-18');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (22,'Extreme Programming Explained: Embrace Change ','978-0-3212-7865-4',8,4,'2004-11-26');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (23,'JUnit Pocket Guide','978-0-5960-0743-0',8,1,'2004-09-23');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (25,'Spring in Action','978-1-9351-8235-1',9,1,'2010-04-28');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (26,'Spring Recipes: A Problem-Solution Approach ','978-1-5905-9979-2',10,1,'2008-06-23');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (27,'Professional Java Development with the Spring Framework','978-0-7645-7483-2',1,1,'2005-07-08');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (28,'POJOs in Action: Developing Enterprise Applications with Lightweight Frameworks','978-1-9323-9458-0',11,1,'2006-01-30');
INSERT INTO `books` (`id`, `name`, `ISBN`, `authorid`, `categoryid`, `publishedDate`) VALUES (29,'COBOL and Visual Basic on .NET: A Guide for the Reformed Mainframe Programmer ','978-1-5905-9048-5',11,5,'2003-04-10');
INSERT INTO `user` (`id`, `surname`, `username`, `registerDate`, `password`, `name`, `email`) VALUES (1,'User','admin','2012-12-02 18:17:42','ISMvKXpXpadDiUoOSoAfww==','Admin','');
INSERT INTO `user_preference` (`id`, `user_id`, `name`, `value`) VALUES (1,1,'bookPageableTable.visible_columns','name,author,category,isbn');
INSERT INTO `user_preference` (`id`, `user_id`, `name`, `value`) VALUES (2,1,'bookPageableTable.page_size','10');
