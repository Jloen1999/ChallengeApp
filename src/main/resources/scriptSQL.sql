CREATE DATABASE  IF NOT EXISTS `retoapp` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `retoapp`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: retoapp
-- ------------------------------------------------------
-- Server version	8.0.38

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
-- Table structure for table `amistad`
--

DROP TABLE IF EXISTS `amistad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `amistad` (
  `usuario_id1` int NOT NULL,
  `usuario_id2` int NOT NULL,
  PRIMARY KEY (`usuario_id1`,`usuario_id2`),
  KEY `FK9ud9c4vl8j9p2ir8ahew2burp` (`usuario_id2`),
  CONSTRAINT `FK9ud9c4vl8j9p2ir8ahew2burp` FOREIGN KEY (`usuario_id2`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FKoljq12cgfs385o54aybu6531` FOREIGN KEY (`usuario_id1`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comentario`
--

DROP TABLE IF EXISTS `comentario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comentario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha` datetime(6) DEFAULT NULL,
  `texto` varchar(255) DEFAULT NULL,
  `reto_id` int DEFAULT NULL,
  `usuario_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKixspmid2pb85o8ypsd20jakxg` (`usuario_id`),
  KEY `FKjhgq68hso5ek83lnehlt6id8u` (`reto_id`),
  CONSTRAINT `FKixspmid2pb85o8ypsd20jakxg` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FKjhgq68hso5ek83lnehlt6id8u` FOREIGN KEY (`reto_id`) REFERENCES `reto` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estadistica`
--

DROP TABLE IF EXISTS `estadistica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadistica` (
  `id` int NOT NULL AUTO_INCREMENT,
  `progreso_promedio` float DEFAULT NULL,
  `retos_completados` int DEFAULT NULL,
  `retos_fallidos` int DEFAULT NULL,
  `tiempo_promedio` float DEFAULT NULL,
  `total_retos` int DEFAULT NULL,
  `usuario_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa1y2jnxscpwbcvuvt0g3cx1kt` (`usuario_id`),
  CONSTRAINT `FKa1y2jnxscpwbcvuvt0g3cx1kt` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notificacion`
--

DROP TABLE IF EXISTS `notificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha_envio` datetime(6) DEFAULT NULL,
  `leido` bit(1) DEFAULT NULL,
  `mensaje` varchar(255) DEFAULT NULL,
  `reto_id` int DEFAULT NULL,
  `usuario_id` int DEFAULT NULL,
  `tipo_notificacion` enum('SOLICITUD_AMISTAD','ACEPTACION_AMISTAD','CREACION_RETO','UNION_RETO','ELIMINACION_AMISTAD','RETO_COMPLETADO','MEDALLA_CONSEGUIDA','ELIMINACION_RETO') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtobe92k5eefvb4ujr4phniodj` (`reto_id`),
  KEY `FK5hnclv9lmmc1w4335x04warbm` (`usuario_id`),
  CONSTRAINT `FK5hnclv9lmmc1w4335x04warbm` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FKtobe92k5eefvb4ujr4phniodj` FOREIGN KEY (`reto_id`) REFERENCES `reto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `participantes_reto`
--

DROP TABLE IF EXISTS `participantes_reto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `participantes_reto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reto_id` int DEFAULT NULL,
  `usuario_id` int DEFAULT NULL,
  `fecha_union` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKif49g23dneo6hebs81s355ghm` (`usuario_id`),
  KEY `FKlh7wpr6gq2un1trviu4eywilm` (`reto_id`),
  CONSTRAINT `fk_participantes_reto` FOREIGN KEY (`reto_id`) REFERENCES `reto` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKif49g23dneo6hebs81s355ghm` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FKlh7wpr6gq2un1trviu4eywilm` FOREIGN KEY (`reto_id`) REFERENCES `reto` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `progreso_reto`
--

DROP TABLE IF EXISTS `progreso_reto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `progreso_reto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha_actualizacion` datetime(6) DEFAULT NULL,
  `progreso_actual` float DEFAULT NULL,
  `reto_id` int DEFAULT NULL,
  `usuario_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7up8420vh4sijskmmw5xniv26` (`reto_id`),
  KEY `FK2sm3o4c028qn9fe80rdxiu2th` (`usuario_id`),
  CONSTRAINT `FK2sm3o4c028qn9fe80rdxiu2th` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FK7up8420vh4sijskmmw5xniv26` FOREIGN KEY (`reto_id`) REFERENCES `reto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `punto`
--

DROP TABLE IF EXISTS `punto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `punto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cantidad` int NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `usuario_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkfhv4rwceqtsn4ut5odak4xmb` (`usuario_id`),
  CONSTRAINT `FKkfhv4rwceqtsn4ut5odak4xmb` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recompensa`
--

DROP TABLE IF EXISTS `recompensa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recompensa` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipo` enum('BRONCE','PLATA','ORO','DIAMANTE') DEFAULT NULL,
  `reto_id` int DEFAULT NULL,
  `usuario_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm1snnwkhssru7kvjahq4iwbed` (`reto_id`),
  KEY `FKr6fq1edp58vod98ljilyf79pw` (`usuario_id`),
  CONSTRAINT `FKm1snnwkhssru7kvjahq4iwbed` FOREIGN KEY (`reto_id`) REFERENCES `reto` (`id`),
  CONSTRAINT `FKr6fq1edp58vod98ljilyf79pw` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reto`
--

DROP TABLE IF EXISTS `reto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) DEFAULT NULL,
  `duracion` int NOT NULL,
  `estado` enum('COMPLETADO','EN_PROGRESO','FALLIDO','PENDIENTE') DEFAULT NULL,
  `fecha_creacion` datetime(6) DEFAULT NULL,
  `fecha_finalizacion` datetime(6) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `novedad` bit(1) DEFAULT NULL,
  `porcentaje_progreso` float DEFAULT NULL,
  `tipo` enum('COMPLEJO','SIMPLE') DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `visibilidad` bit(1) DEFAULT NULL,
  `creador_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs60j7c86jrivijfau54v92qkf` (`creador_id`),
  CONSTRAINT `FKs60j7c86jrivijfau54v92qkf` FOREIGN KEY (`creador_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reto_complejo`
--

DROP TABLE IF EXISTS `reto_complejo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reto_complejo` (
  `subtarea_actual` int NOT NULL,
  `id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKp583qwdniccsqwj1qi69xlkha` FOREIGN KEY (`id`) REFERENCES `reto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reto_simple`
--

DROP TABLE IF EXISTS `reto_simple`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reto_simple` (
  `progreso` int NOT NULL,
  `id` int NOT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKpc29glf8hylo6igeo7mnakvx3` FOREIGN KEY (`id`) REFERENCES `reto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subtarea`
--

DROP TABLE IF EXISTS `subtarea`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subtarea` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` enum('COMPLETADA','EN_PROGRESO','PENDIENTE') DEFAULT NULL,
  `fecha_creacion` datetime(6) DEFAULT NULL,
  `fecha_finalizada` datetime(6) DEFAULT NULL,
  `reto_complejo_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn5090luignd8pm1oqoguc68rj` (`reto_complejo_id`),
  CONSTRAINT `FKn5090luignd8pm1oqoguc68rj` FOREIGN KEY (`reto_complejo_id`) REFERENCES `reto_complejo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contrasena` varchar(255) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `perfil_info` varchar(255) DEFAULT NULL,
  `ubicacion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-15  4:59:20
