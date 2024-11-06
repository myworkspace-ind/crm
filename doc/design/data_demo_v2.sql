-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: crm
-- ------------------------------------------------------
-- Server version	8.0.39

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
-- Table structure for table `crm_customer`
--

DROP TABLE IF EXISTS `crm_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crm_customer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `site_id` varchar(99) DEFAULT NULL,
  `company_name` varchar(99) DEFAULT NULL,
  `contact_person` varchar(99) DEFAULT NULL,
  `email` varchar(99) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `profession` varchar(99) DEFAULT NULL,
  `main_status_id` bigint DEFAULT NULL,
  `sub_status_id` bigint DEFAULT NULL,
  `responsible_person` varchar(99) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkdeyvb8vcb3luc9rjbdq5ked0` (`main_status_id`),
  KEY `FKfomguaxykk0028fg59gwixnce` (`sub_status_id`),
  CONSTRAINT `FKfomguaxykk0028fg59gwixnce` FOREIGN KEY (`sub_status_id`) REFERENCES `crm_status` (`id`),
  CONSTRAINT `FKkdeyvb8vcb3luc9rjbdq5ked0` FOREIGN KEY (`main_status_id`) REFERENCES `crm_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_customer`
--

LOCK TABLES `crm_customer` WRITE;
/*!40000 ALTER TABLE `crm_customer` DISABLE KEYS */;
INSERT INTO `crm_customer` VALUES (1,NULL,'Cong ty A','Nguyen Van A','vananguyen@gmail.com','0123456789','1 Vo Van Ngan, Thu Duc','Thuc pham',1,2,'Nguyen The Thanh',NULL,'note'),(2,NULL,'Cong ty B','Tran Van B','tranvanb@gmail.com','0987654321','123 Nguyen Trai, District 1','Cong nghe',3,5,'Nguyen Thi Mai',NULL,'note for B'),(3,NULL,'Cong ty C','Le Van C','levanc@gmail.com','0912345678','456 Le Lai, District 3','Giao duc',2,4,'Nguyen Van Hai',NULL,'note for C'),(4,NULL,'Cong ty D','Hoang Van D','hoangvand@gmail.com','0123456780','789 Hai Ba Trung, District 1','Y te',5,7,'Nguyen Thi Lan',NULL,'note for D'),(5,NULL,'Cong ty E','Pham Van E','phamvane@gmail.com','0987123456','101 Tran Hung Dao, District 5','Du lich',1,6,'Nguyen Van An',NULL,'note for E'),(6,NULL,'Cong ty F','Nguyen Van F','nguyenvanf@gmail.com','0934567890','202 Phan Van Tri, District 6','Logistics',4,3,'Nguyen Thi Hoa',NULL,'note for F'),(7,NULL,'Cong ty G','Vu Van G','vuvang@gmail.com','0989123456','303 Le Van Sy, District 10','Nong nghiep',6,8,'Nguyen Van Quang',NULL,'note for G'),(8,NULL,'Cong ty H','Ngo Van H','ngovan@gmail.com','0976543210','404 Nguyen Hue, District 9','Kien truc',7,2,'Nguyen Thi Thao',NULL,'note for H'),(9,NULL,'Cong ty I','Vo Van I','vovan@gmail.com','0965432109','505 Tan Son Nhat, District 11','Dich vu',9,1,'Nguyen Van Cuong',NULL,'note for I'),(10,NULL,'Cong ty J','Bui Van J','buivan@gmail.com','0954321098','606 Le Van Khuyen, District 12','Tin hoc',10,9,'Nguyen Van Son',NULL,'note for J'),(21,NULL,'Cong ty thanh','Nguyen The Thanh','thethanh@gmail.com','0379513457','no','Tin hoc',9,10,'Nguyen Thanh',NULL,'no'),(22,NULL,'Cong ty K','Tran Van K','tranvank@gmail.com','0943210987','707 Pasteur, District 3','Cong nghe',2,5,'Le Thi Hong',NULL,'note for K'),(23,NULL,'Cong ty L','Le Van L','levanl@gmail.com','0932109876','808 Tran Quang Khai, District 5','Giao duc',3,6,'Nguyen Thi Huong',NULL,'note for L'),(24,NULL,'Cong ty M','Hoang Van M','hoangvanm@gmail.com','0921098765','909 Ly Chinh Thang, District 10','Y te',4,7,'Tran Van Long',NULL,'note for M'),(25,NULL,'Cong ty N','Pham Van N','phamvann@gmail.com','0910987654','1010 Cach Mang Thang 8, District 11','Du lich',5,8,'Ngo Van Binh',NULL,'note for N'),(26,NULL,'Cong ty O','Vo Van O','vovano@gmail.com','0909876543','2020 Hoang Sa, District 3','Logistics',6,9,'Bui Thi Lan',NULL,'note for O'),(27,NULL,'Cong ty P','Bui Van P','buivanp@gmail.com','0898765432','3030 Truong Sa, District 5','Nong nghiep',7,1,'Vu Van Phong',NULL,'note for P'),(28,NULL,'Cong ty Q','Nguyen Van Q','nguyenvanq@gmail.com','0887654321','4040 Dinh Tien Hoang, District 9','Kien truc',8,2,'Ngo Thi Lan',NULL,'note for Q'),(29,NULL,'Cong ty R','Tran Van R','tranvanr@gmail.com','0876543210','5050 Dien Bien Phu, District 1','Dich vu',9,3,'Nguyen Van Thanh',NULL,'note for R'),(30,NULL,'Cong ty S','Le Van S','levans@gmail.com','0865432109','6060 Vo Van Kiet, District 10','Tin hoc',10,4,'Hoang Thi Thu',NULL,'note for S'),(31,NULL,'Cong ty T','Hoang Van T','hoangvant@gmail.com','0854321098','7070 Nguyen Trai, District 7','Y te',1,5,'Pham Van Thuan',NULL,'note for T'),(32,NULL,'Cong ty U','Pham Van U','phamvanu@gmail.com','0843210987','8080 Tran Hung Dao, District 12','Giao duc',2,6,'Tran Thi Kieu',NULL,'note for U'),(33,NULL,'Cong ty V','Vo Van V','vovanv@gmail.com','0832109876','9090 To Hien Thanh, District 3','Cong nghe',3,7,'Le Van Nam',NULL,'note for V'),(34,NULL,'Cong ty W','Vu Van W','vuvan@gmail.com','0821098765','1011 Nguyen Thi Minh Khai, District 5','Nong nghiep',4,8,'Bui Van Tinh',NULL,'note for W'),(35,NULL,'Cong ty X','Ngo Van X','ngovanx@gmail.com','0810987654','2021 Hai Ba Trung, District 1','Du lich',5,9,'Tran Van Ky',NULL,'note for X'),(36,NULL,'Cong ty Y','Bui Van Y','buivany@gmail.com','0809876543','3031 Le Loi, District 7','Logistics',6,1,'Nguyen Van Hung',NULL,'note for Y'),(37,NULL,'Cong ty Z','Nguyen Van Z','nguyenvanz@gmail.com','0798765432','4041 Ba Thang Hai, District 10','Dich vu',7,2,'Hoang Van Vu',NULL,'note for Z'),(38,NULL,'Cong ty AA','Tran Van AA','tranvana@gmail.com','0787654321','5051 Nguyen Tat Thanh, District 4','Kien truc',8,3,'Ngo Van Tan',NULL,'note for AA'),(39,NULL,'Cong ty BB','Le Van BB','levanbb@gmail.com','0776543210','6061 Binh Quoi, District 6','Tin hoc',9,4,'Nguyen Thi Nga',NULL,'note for BB'),(40,NULL,'Cong ty CC','Hoang Van CC','hoangvancc@gmail.com','0765432109','7071 Bach Dang, District 2','Thuc pham',10,5,'Tran Van Lam',NULL,'note for CC'),(41,NULL,'Cong ty DD','Pham Van DD','phamvandd@gmail.com','0754321098','8081 Kha Van Can, District 1','Cong nghe',1,6,'Bui Van Thang',NULL,'note for DD');
/*!40000 ALTER TABLE `crm_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_goodscategory`
--

DROP TABLE IF EXISTS `crm_goodscategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crm_goodscategory` (
  `id` varchar(99) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_goodscategory`
--

LOCK TABLES `crm_goodscategory` WRITE;
/*!40000 ALTER TABLE `crm_goodscategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_goodscategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_order`
--

DROP TABLE IF EXISTS `crm_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crm_order` (
  `id` varchar(99) NOT NULL,
  `customer_requirement` varchar(255) DEFAULT NULL,
  `delivery_date` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  `transportation_method` varchar(255) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `goods_category_id` varchar(99) DEFAULT NULL,
  `order_category_id` varchar(99) DEFAULT NULL,
  `order_status_id` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkqn8njq5u457gy53d60t0wugw` (`customer_id`),
  KEY `FKa7v6uifcdehfy2mcvyapmx8t7` (`goods_category_id`),
  KEY `FKcxl4s7b1cwrysor1ydb4n950h` (`order_category_id`),
  KEY `FKh8jnbbs91jewbyt7geupryt7x` (`order_status_id`),
  CONSTRAINT `FKa7v6uifcdehfy2mcvyapmx8t7` FOREIGN KEY (`goods_category_id`) REFERENCES `crm_goodscategory` (`id`),
  CONSTRAINT `FKcxl4s7b1cwrysor1ydb4n950h` FOREIGN KEY (`order_category_id`) REFERENCES `crm_ordercategory` (`id`),
  CONSTRAINT `FKh8jnbbs91jewbyt7geupryt7x` FOREIGN KEY (`order_status_id`) REFERENCES `crm_orderstatus` (`id`),
  CONSTRAINT `FKkqn8njq5u457gy53d60t0wugw` FOREIGN KEY (`customer_id`) REFERENCES `crm_customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_order`
--

LOCK TABLES `crm_order` WRITE;
/*!40000 ALTER TABLE `crm_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_ordercategory`
--

DROP TABLE IF EXISTS `crm_ordercategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crm_ordercategory` (
  `id` varchar(99) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_ordercategory`
--

LOCK TABLES `crm_ordercategory` WRITE;
/*!40000 ALTER TABLE `crm_ordercategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_ordercategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_orderstatus`
--

DROP TABLE IF EXISTS `crm_orderstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crm_orderstatus` (
  `id` varchar(99) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_orderstatus`
--

LOCK TABLES `crm_orderstatus` WRITE;
/*!40000 ALTER TABLE `crm_orderstatus` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_orderstatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_status`
--

DROP TABLE IF EXISTS `crm_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crm_status` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `backgroundColor` varchar(99) DEFAULT NULL,
  `name` varchar(99) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_status`
--

LOCK TABLES `crm_status` WRITE;
/*!40000 ALTER TABLE `crm_status` DISABLE KEYS */;
INSERT INTO `crm_status` VALUES (1,'#c0392b','Mới',NULL),(2,'#e74c3c','Tư vấn',NULL),(3,'#9b59b6','KH tiềm năng',NULL),(4,'#8e44ad','Báo giá khách hàng',NULL),(5,'#2980b9','Đàm phán',NULL),(6,'#2e86c1','Chốt hợp đồng',NULL),(7,'#2e86c1','Báo giá',NULL),(8,'#27ae60','Chốt',NULL),(9,'#27ae60','Data rớt',NULL),(10,'#e67e22','Chăm lại',NULL),(11,'#e67e22','Đến nhưng không ở lại',NULL);
/*!40000 ALTER TABLE `crm_status` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-06 22:11:48
