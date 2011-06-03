-- MySQL dump 10.11
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	5.0.83-1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `empresa`
--

DROP TABLE IF EXISTS `empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `empresa` (
  `id` int(11) NOT NULL auto_increment,
  `nombre` varchar(200) default '',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empresa`
--

LOCK TABLES `empresa` WRITE;
/*!40000 ALTER TABLE `empresa` DISABLE KEYS */;
INSERT INTO `empresa` VALUES (1,'Nombre Empresa 1'),(2,'Nombre Empresa 1'),(3,'Nombre Empresa 1'),(4,'Nombre Empresa 1'),(5,'Nombre Empresa 1'),(6,'Nombre Empresa 1'),(7,'Nombre Empresa 1'),(8,'Nombre Empresa 1'),(9,'Nombre Empresa 1'),(10,'Nombre Empresa 1'),(11,'Nombre Empresa 1'),(12,'Nombre Empresa 1'),(13,'Nombre Empresa 1'),(14,'Nombre Empresa 1'),(15,'Nombre Empresa 1'),(16,'Nombre Empresa 1'),(17,'Nombre Empresa 1'),(18,'Nombre Empresa 1'),(19,'Nombre Empresa 1'),(20,'Nombre Empresa 1'),(21,'Nombre Empresa 1'),(22,'Nombre Empresa 1'),(23,'Nombre Empresa 1'),(24,'Nombre Empresa 1');
/*!40000 ALTER TABLE `empresa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL auto_increment,
  `nombre` varchar(200) character set latin1 default NULL,
  `password` varchar(200) character set latin1 default NULL,
  `usuario` varchar(200) character set latin1 default NULL,
  `edad` int(11) default NULL,
  `admin` tinyint(1) default NULL,
  `apellido` varchar(200) character set latin1 default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=215 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (100,'Antonio',NULL,'antonio',30,1,NULL),(101,'Antonio',NULL,'antonio',30,1,NULL),(102,'Antonio',NULL,'antonio',30,1,NULL),(103,'Antonio',NULL,'antonio',30,1,NULL),(104,'Antonio',NULL,'antonio',30,1,NULL),(105,'Antonio',NULL,'antonio',30,1,NULL),(106,'Antonio',NULL,'antonio',30,1,NULL),(107,'Antonio',NULL,'antonio',30,1,NULL),(108,'Antonio',NULL,'antonio',30,1,NULL),(109,'Antonio',NULL,'antonio',30,1,NULL),(110,'Antonio',NULL,'antonio',30,1,NULL),(111,'Antonio',NULL,'antonio',30,1,NULL),(112,'Antonio',NULL,'antonio',30,1,NULL),(113,'Antonio',NULL,'antonio',30,1,NULL),(114,'Antonio',NULL,'antonio',30,1,NULL),(115,'Antonio',NULL,'antonio',30,1,NULL),(116,'Antonio',NULL,'antonio',30,1,NULL),(117,'Antonio',NULL,'antonio',30,1,NULL),(118,'Antonio',NULL,'antonio',30,1,NULL),(119,'Antonio',NULL,'antonio',30,1,NULL),(120,'Antonio',NULL,'antonio',30,1,NULL),(121,'Antonio',NULL,'antonio',30,1,NULL),(122,'Antonio',NULL,'antonio',30,1,NULL),(123,'Antonio',NULL,'antonio',30,1,NULL),(124,'Antonio',NULL,'antonio',30,1,NULL),(125,'Antonio',NULL,'antonio',30,1,NULL),(126,'Antonio',NULL,'antonio',30,1,NULL),(127,'Antonio',NULL,'antonio',30,1,NULL),(128,'Antonio',NULL,'antonio',30,1,NULL),(129,'Antonio',NULL,'antonio',30,1,NULL),(130,'Antonio',NULL,'antonio',30,1,NULL),(131,'Antonio',NULL,'antonio',30,1,NULL),(132,'Antonio',NULL,'antonio',30,1,NULL),(133,'Antonio',NULL,'antonio',30,1,NULL),(134,'Antonio',NULL,'antonio',30,1,NULL),(135,'Antonio',NULL,'antonio',30,1,NULL),(136,'Antonio',NULL,'antonio',30,1,NULL),(137,'Antonio',NULL,'antonio',30,1,NULL),(138,'Antonio',NULL,'antonio',30,1,NULL),(139,'Antonio',NULL,'antonio',30,1,NULL),(140,'Antonio',NULL,'antonio',30,1,NULL),(141,'Antonio',NULL,'antonio',30,1,NULL),(142,'Antonio',NULL,'antonio',30,1,NULL),(143,'Antonio',NULL,'antonio',30,1,NULL),(144,'Antonio',NULL,'antonio',30,1,NULL),(145,'Antonio',NULL,'antonio',30,1,NULL),(146,'Antonio',NULL,'antonio',30,1,NULL),(147,'Antonio',NULL,'antonio',30,1,NULL),(148,'Juan',NULL,'juan',24,1,NULL),(149,'Juan',NULL,'juan',24,1,NULL),(150,'Juan',NULL,'juan',24,1,NULL),(151,'Juan',NULL,'juan',24,1,NULL),(152,'Juan',NULL,'juan',24,1,NULL),(153,'Juan',NULL,'juan',24,1,NULL),(154,'Juan',NULL,'juan',24,1,NULL),(155,'Juan',NULL,'juan',24,1,NULL),(156,'Juan',NULL,'juan',24,1,NULL),(157,'Juan',NULL,'juan',24,1,NULL),(158,'Juan',NULL,'juan',24,1,NULL),(159,'Juan',NULL,'juan',24,1,NULL),(160,'Juan',NULL,'juan',24,1,NULL),(161,'Juan',NULL,'juan',24,1,NULL),(162,'Juan',NULL,'juan',24,1,NULL),(163,'Juan',NULL,'juan',24,1,NULL),(164,'Juan',NULL,'juan',24,1,NULL),(165,'Juan',NULL,'juan',24,1,NULL),(166,'Juan',NULL,'juan',24,1,NULL),(167,'Juan',NULL,'juan',24,1,NULL),(168,'Juan',NULL,'juan',24,1,NULL),(169,'Juan',NULL,'juan',24,1,NULL),(170,'Juan',NULL,'juan',24,1,NULL),(171,'Juan',NULL,'juan',24,1,NULL),(172,'Juan',NULL,'juan',24,1,NULL),(173,'Juan',NULL,'juan',24,1,NULL),(174,'Juan',NULL,'juan',24,1,NULL),(175,'Juan',NULL,'juan',24,1,NULL),(176,'Juan',NULL,'juan',24,1,NULL),(177,'Juan',NULL,'juan',24,1,NULL),(178,'Juan',NULL,'juan',24,1,NULL),(179,'Juan',NULL,'juan',24,1,NULL),(180,'Juan',NULL,'juan',24,1,NULL),(181,'Juan',NULL,'juan',24,1,NULL),(182,'Antonio',NULL,'antonio',30,1,NULL),(183,'Pedro',NULL,'pedro',48,1,NULL),(184,'Pedro',NULL,'pedro',48,1,NULL),(185,'Pedro',NULL,'pedro',48,1,NULL),(186,'Pedro',NULL,'pedro',48,1,NULL),(187,'Pedro',NULL,'pedro',48,1,NULL),(188,'Pedro',NULL,'pedro',48,1,NULL),(189,'Pedro',NULL,'pedro',48,1,NULL),(190,'Pedro',NULL,'pedro',48,1,NULL),(191,'Pedro',NULL,'pedro',48,1,NULL),(192,'Pedro',NULL,'pedro',48,1,NULL),(193,'Pedro',NULL,'pedro',48,1,NULL),(194,'Pedro',NULL,'pedro',48,1,NULL),(195,'Pedro',NULL,'pedro',48,1,NULL),(196,'Pedro',NULL,'pedro',48,1,NULL),(197,'Pedro',NULL,'pedro',48,1,NULL),(198,'Pedro',NULL,'pedro',48,1,NULL),(199,'Pedro',NULL,'pedro',48,1,NULL),(200,'Pedro',NULL,'pedro',48,1,NULL),(201,'Pedro',NULL,'pedro',48,1,NULL),(202,'Pedro',NULL,'pedro',48,1,NULL),(203,'Pedro',NULL,'pedro',48,1,NULL),(204,'Pedro',NULL,'pedro',48,1,NULL),(205,'Pedro',NULL,'pedro',48,1,NULL),(206,'Pedro',NULL,'pedro',48,1,NULL),(207,'Pedro',NULL,'pedro',48,1,NULL),(208,'Pedro',NULL,'pedro',48,1,NULL),(209,'Pedro',NULL,'pedro',48,1,NULL),(210,'Pedro',NULL,'pedro',48,1,NULL),(211,'Pedro',NULL,'pedro',48,1,NULL),(212,'Pedro',NULL,'pedro',48,1,NULL),(213,'Pedro',NULL,'pedro',48,1,NULL),(214,'Pedro',NULL,'pedro',48,1,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-06-02 21:53:18
