-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: mrp
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `inventory_usage_record`
--

DROP TABLE IF EXISTS `inventory_usage_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_usage_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `requisition_id` bigint DEFAULT NULL,
  `stock_id` bigint DEFAULT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrh6atwg8n3nrch73wjwigjsf7` (`requisition_id`),
  KEY `FK6vq9d6u8q4qwvwafa0buq2uoa` (`stock_id`),
  KEY `FKpvnpde86gd8bhiqug2cuydrx3` (`user_email`),
  CONSTRAINT `FK6vq9d6u8q4qwvwafa0buq2uoa` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`),
  CONSTRAINT `FKpvnpde86gd8bhiqug2cuydrx3` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`),
  CONSTRAINT `FKrh6atwg8n3nrch73wjwigjsf7` FOREIGN KEY (`requisition_id`) REFERENCES `requisition` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_usage_record`
--

LOCK TABLES `inventory_usage_record` WRITE;
/*!40000 ALTER TABLE `inventory_usage_record` DISABLE KEYS */;
INSERT INTO `inventory_usage_record` VALUES (3,'2024-06-03 10:49:19.733235',100,'Complete','2024-06-03 10:49:19.733235',24,4,'admin@mail.com'),(4,'2024-06-03 10:49:39.550028',200,'Complete','2024-06-03 10:49:39.550028',25,5,'admin@mail.com'),(5,'2024-06-03 10:49:43.358080',50,'Complete','2024-06-03 10:49:43.358080',26,6,'admin@mail.com'),(6,'2024-06-03 10:49:55.301277',50,'Complete','2024-06-03 10:49:55.301277',27,7,'admin@mail.com'),(7,'2024-06-03 10:50:44.758699',NULL,'Out of stock','2024-06-03 10:50:44.758699',32,6,'admin@mail.com'),(8,'2024-06-03 10:02:00.512780',1000,'Complete','2024-06-03 10:02:00.512780',28,7,'admin@mail.com');
/*!40000 ALTER TABLE `inventory_usage_record` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-03  5:33:36
