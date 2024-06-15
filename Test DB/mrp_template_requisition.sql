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
-- Table structure for table `template_requisition`
--

DROP TABLE IF EXISTS `template_requisition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `template_requisition` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `quantity` float NOT NULL,
  `status` varchar(50) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `job_id` bigint NOT NULL,
  `stock_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnuegtt172oe3ac8rnth1lixj0` (`job_id`),
  KEY `FKofdn8a56g3htssqb7i5y3p30q` (`stock_id`),
  CONSTRAINT `FKnuegtt172oe3ac8rnth1lixj0` FOREIGN KEY (`job_id`) REFERENCES `template_job` (`id`),
  CONSTRAINT `FKofdn8a56g3htssqb7i5y3p30q` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `template_requisition`
--

LOCK TABLES `template_requisition` WRITE;
/*!40000 ALTER TABLE `template_requisition` DISABLE KEYS */;
INSERT INTO `template_requisition` VALUES (14,'2024-06-03 10:10:50.365373',200,'In progress','2024-06-03 10:10:50.365373',31,6),(15,'2024-06-03 10:10:50.371372',150,'In progress','2024-06-03 10:10:50.371372',32,6),(16,'2024-06-03 10:10:50.375378',100,'In progress','2024-06-03 10:10:50.375378',33,7),(17,'2024-06-03 10:10:50.380376',100,'In progress','2024-06-03 10:10:50.380376',35,10);
/*!40000 ALTER TABLE `template_requisition` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-15  2:41:40
