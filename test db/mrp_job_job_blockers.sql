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
-- Table structure for table `job_job_blockers`
--

DROP TABLE IF EXISTS `job_job_blockers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_job_blockers` (
  `job_id` bigint NOT NULL,
  `job_blockers_id` bigint NOT NULL,
  UNIQUE KEY `UK_ea6juqfl83m5mwr94ql579wev` (`job_blockers_id`),
  KEY `FK38qbnbymqf95de8xwjk0putlt` (`job_id`),
  CONSTRAINT `FK38qbnbymqf95de8xwjk0putlt` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`),
  CONSTRAINT `FK5yw1bho6496f0d88kf1kwf7ee` FOREIGN KEY (`job_blockers_id`) REFERENCES `job` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_job_blockers`
--

LOCK TABLES `job_job_blockers` WRITE;
/*!40000 ALTER TABLE `job_job_blockers` DISABLE KEYS */;
INSERT INTO `job_job_blockers` VALUES (63,52),(63,53),(64,54),(64,55),(65,56),(66,57),(66,58),(67,59),(67,60),(67,61),(68,62),(69,63),(70,64),(70,65),(71,66),(72,67),(73,68),(74,69),(75,70),(76,71),(77,72),(78,73),(82,79),(82,80),(82,81),(83,82),(84,83),(88,85),(88,86),(88,87),(89,88),(90,89);
/*!40000 ALTER TABLE `job_job_blockers` ENABLE KEYS */;
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
