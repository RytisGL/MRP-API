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
-- Table structure for table `job_record`
--

DROP TABLE IF EXISTS `job_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `status` varchar(50) NOT NULL,
  `job_id` bigint NOT NULL,
  `user_email` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1shrcrh18fyn0s8jlhbbgpjth` (`job_id`),
  KEY `FKqoxqwlupdes62ag9hys788m6s` (`user_email`),
  CONSTRAINT `FK1shrcrh18fyn0s8jlhbbgpjth` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`),
  CONSTRAINT `FKqoxqwlupdes62ag9hys788m6s` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_record`
--

LOCK TABLES `job_record` WRITE;
/*!40000 ALTER TABLE `job_record` DISABLE KEYS */;
INSERT INTO `job_record` VALUES (1,'2024-06-03 10:54:51.025780','Complete',52,'nicole@example.com'),(2,'2024-06-03 10:55:14.194788','Complete',53,'nicole@example.com'),(3,'2024-06-03 10:55:29.202437','Complete',54,'nicole@example.com'),(4,'2024-06-03 10:55:58.880834','Complete',55,'nicole@example.com'),(5,'2024-06-04 18:48:46.290189','On hold',56,'nicole@example.com'),(6,'2024-06-04 18:48:56.513757','In progress',56,'nicole@example.com'),(7,'2024-06-04 18:49:07.408478','Complete',56,'nicole@example.com'),(8,'2024-06-04 18:49:19.418278','On hold',57,'nicole@example.com');
/*!40000 ALTER TABLE `job_record` ENABLE KEYS */;
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
