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
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `details` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `customer_order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKetvfy38ivsba5r5v5hapijhgc` (`customer_order_id`),
  CONSTRAINT `FKetvfy38ivsba5r5v5hapijhgc` FOREIGN KEY (`customer_order_id`) REFERENCES `customer_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (52,'2024-06-03 09:03:19.750595','Manufacturing parts #1 Product #1','Complete','Manufacturing','2024-06-03 09:54:51.055773',25),(53,'2024-06-03 09:03:24.803205','Manufacturing parts #2 Product #1','Complete','Manufacturing','2024-06-03 09:55:14.202782',25),(54,'2024-06-03 09:03:53.345226','Manufacturing parts #1 Product #2','Complete','Manufacturing','2024-06-03 09:55:29.210016',26),(55,'2024-06-03 09:03:56.769156','Manufacturing parts #2 Product #2','Complete','Manufacturing','2024-06-03 09:55:58.884556',26),(56,'2024-06-03 09:04:03.417855','Manufacturing parts #3 Product #2','Complete','Manufacturing','2024-06-04 18:49:07.410482',26),(57,'2024-06-03 09:04:17.213860','Manufacturing parts #1 Product #3','On hold','Manufacturing','2024-06-04 18:49:38.937393',28),(58,'2024-06-03 09:04:23.750550','Manufacturing parts #2 Product #3','In progress','Manufacturing','2024-06-03 09:15:29.446905',28),(59,'2024-06-03 09:04:29.785234','Manufacturing parts #1 Product #4','In progress','Manufacturing','2024-06-03 09:17:29.597169',27),(60,'2024-06-03 09:04:32.929489','Manufacturing parts #2 Product #4','In progress','Manufacturing','2024-06-03 09:17:29.599174',27),(61,'2024-06-03 09:04:36.743311','Manufacturing parts #3 Product #4','In progress','Manufacturing','2024-06-03 09:17:29.602167',27),(62,'2024-06-03 09:04:46.445595','Manufacturing parts #1 Product #5','In progress','Manufacturing','2024-06-03 09:18:25.648619',29),(63,'2024-06-03 09:05:41.378006','Assembly parts #1 Product #1','In progress','Assembly','2024-06-03 09:26:18.168699',25),(64,'2024-06-03 09:05:51.868333','Assembly parts #2 Product #2','In progress','Assembly','2024-06-03 09:26:45.557926',26),(65,'2024-06-03 09:06:40.763626','Assembly parts #1 Product #2','In progress','Assembly','2024-06-03 09:27:07.782924',26),(66,'2024-06-03 09:06:46.314613','Assembly parts #1 Product #3','In progress','Assembly','2024-06-03 09:27:46.148577',28),(67,'2024-06-03 09:06:50.894046','Assembly parts #1 Product #4','In progress','Assembly','2024-06-03 09:28:32.010931',27),(68,'2024-06-03 09:06:56.817495','Assembly parts #1 Product #5','In progress','Assembly','2024-06-03 09:29:23.960270',29),(69,'2024-06-03 09:07:48.962315','Painting product #1','In progress','Painting','2024-06-03 09:29:51.016368',25),(70,'2024-06-03 09:07:52.591189','Painting product #2','In progress','Painting','2024-06-03 09:30:09.372711',26),(71,'2024-06-03 09:07:56.751915','Painting product #3','In progress','Painting','2024-06-03 09:30:31.178155',28),(72,'2024-06-03 09:08:00.451200','Painting product #4','In progress','Painting','2024-06-03 09:30:49.485872',27),(73,'2024-06-03 09:08:03.477948','Painting product #5','In progress','Painting','2024-06-03 09:31:01.355331',29),(74,'2024-06-03 09:08:31.431983','Quality check product #1','In progress','Quality check','2024-06-03 09:31:22.045255',25),(75,'2024-06-03 09:08:35.170078','Quality check product #2','In progress','Quality check','2024-06-03 09:31:29.074689',26),(76,'2024-06-03 09:08:37.584442','Quality check product #3','In progress','Quality check','2024-06-03 09:31:34.543393',28),(77,'2024-06-03 09:08:40.448365','Quality check product #4','In progress','Quality check','2024-06-03 09:31:39.754191',27),(78,'2024-06-03 09:08:44.112916','Quality check product #5','In progress','Quality check','2024-06-03 09:31:47.901801',29),(79,'2024-06-03 09:14:29.826023','Manufacturing parts #1 Product #4','In progress','Manufacturing','2024-06-03 10:14:29.826023',30),(80,'2024-06-03 09:14:29.830020','Manufacturing parts #2 Product #4','In progress','Manufacturing','2024-06-03 10:14:29.830020',30),(81,'2024-06-03 09:14:29.842587','Manufacturing parts #3 Product #4','In progress','Manufacturing','2024-06-03 10:14:29.842587',30),(82,'2024-06-03 09:14:29.846581','Assembly parts #1 Product #4','In progress','Assembly','2024-06-03 10:14:29.846581',30),(83,'2024-06-03 09:14:29.848573','Painting product #4','In progress','Painting','2024-06-03 10:14:29.848573',30),(84,'2024-06-03 09:14:29.851575','Quality check product #4','In progress','Quality check','2024-06-03 10:14:29.851575',30),(85,'2024-06-03 09:17:11.938282','Manufacturing parts #1 Product #4','In progress','Manufacturing','2024-06-03 10:17:11.938282',31),(86,'2024-06-03 09:17:11.942280','Manufacturing parts #2 Product #4','In progress','Manufacturing','2024-06-03 10:17:11.942280',31),(87,'2024-06-03 09:17:11.946279','Manufacturing parts #3 Product #4','In progress','Manufacturing','2024-06-03 10:17:11.946279',31),(88,'2024-06-03 09:17:11.951291','Assembly parts #1 Product #4','In progress','Assembly','2024-06-03 10:17:11.951291',31),(89,'2024-06-03 09:17:11.953280','Painting product #4','In progress','Painting','2024-06-03 10:17:11.953280',31),(90,'2024-06-03 09:17:11.957279','Quality check product #4','In progress','Quality check','2024-06-03 10:17:11.957279',31);
/*!40000 ALTER TABLE `job` ENABLE KEYS */;
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
