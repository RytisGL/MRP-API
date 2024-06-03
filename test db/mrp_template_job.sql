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
-- Table structure for table `template_job`
--

DROP TABLE IF EXISTS `template_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `template_job` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `details` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `customer_order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4ohve9hlxvydfmbh52vx7bei2` (`customer_order_id`),
  CONSTRAINT `FK4ohve9hlxvydfmbh52vx7bei2` FOREIGN KEY (`customer_order_id`) REFERENCES `template_customer_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `template_job`
--

LOCK TABLES `template_job` WRITE;
/*!40000 ALTER TABLE `template_job` DISABLE KEYS */;
INSERT INTO `template_job` VALUES (31,'2024-06-03 10:10:50.363359','Manufacturing parts #1 Product #4','In progress','Manufacturing','2024-06-03 10:10:50.363359',15),(32,'2024-06-03 10:10:50.368380','Manufacturing parts #2 Product #4','In progress','Manufacturing','2024-06-03 10:10:50.368380',15),(33,'2024-06-03 10:10:50.373381','Manufacturing parts #3 Product #4','In progress','Manufacturing','2024-06-03 10:10:50.373381',15),(34,'2024-06-03 10:10:50.376377','Assembly parts #1 Product #4','In progress','Assembly','2024-06-03 10:10:50.377370',15),(35,'2024-06-03 10:10:50.378371','Painting product #4','In progress','Painting','2024-06-03 10:10:50.378371',15),(36,'2024-06-03 10:10:50.382376','Quality check product #4','In progress','Quality check','2024-06-03 10:10:50.382376',15);
/*!40000 ALTER TABLE `template_job` ENABLE KEYS */;
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
