CREATE TABLE `authors` (
  `id` int  NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `surname` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `books` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL,
  `ISBN` varchar(32)  DEFAULT NULL,
  `authorid` int(11) DEFAULT NULL,
  `categoryid` int(11) DEFAULT NULL,
  `publishedDate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `author_fk` FOREIGN KEY (`authorid`) REFERENCES `authors` (`id`),
  CONSTRAINT `category_fk` FOREIGN KEY (`categoryid`) REFERENCES `categories` (`id`)
);

CREATE TABLE `readers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `surname` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `readers_books` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `books_id` int(11) NOT NULL,
  `readers_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `readers_books_ibfk_1` FOREIGN KEY (`readers_id`) REFERENCES `readers` (`id`),
  CONSTRAINT `readers_books_ibfk_2` FOREIGN KEY (`books_id`) REFERENCES `books` (`id`)
);

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `surname` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `registerDate` datetime DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `user_preference` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `user_preference_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);
