use crm;
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
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `site_id` VARCHAR(99) DEFAULT NULL,
  `company_name` VARCHAR(99) DEFAULT NULL,
  `contact_person` VARCHAR(99) DEFAULT NULL,
  `email` VARCHAR(99) DEFAULT NULL,
  `phone` VARCHAR(10) DEFAULT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `profession_id` VARCHAR(99) DEFAULT NULL,
  `main_status_id` BIGINT DEFAULT NULL,
  `sub_status_id` BIGINT DEFAULT NULL,
  `responsible_person_id` VARCHAR(99) DEFAULT NULL,
  `created_at` DATETIME(6) DEFAULT NULL,
  `note` VARCHAR(255) DEFAULT NULL,
  `account_status` BIT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_main_status_id` (`main_status_id`),
  KEY `FK_sub_status_id` (`sub_status_id`),
  CONSTRAINT `FK_sub_status_id` FOREIGN KEY (`sub_status_id`) REFERENCES `crm_status` (`id`),
  CONSTRAINT `FK_main_status_id` FOREIGN KEY (`main_status_id`) REFERENCES `crm_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_customer`
--

INSERT INTO `crm_customer` 
(`id`, `site_id`, `company_name`, `contact_person`, `email`, `phone`, `address`, `profession_id`, `main_status_id`, `sub_status_id`, `responsible_person_id`, `created_at`, `note`, `account_status`) 
VALUES 
(1, NULL, 'Cong ty A', 'Nguyen Van A', 'vananguyen@gmail.com', '0123456789', '1 Vo Van Ngan, Thu Duc', '1', 1, 2, '1', NULL, 'note', b'1'),
(2, NULL, 'Cong ty B', 'Tran Van B', 'tranvanb@gmail.com', '0987654321', '123 Nguyen Trai, District 1', '2', 3, 5, '2', NULL, 'note for B', b'1'),
(3, NULL, 'Cong ty C', 'Le Van C', 'levanc@gmail.com', '0912345678', '456 Le Lai, District 3', '3', 2, 4, '3', NULL, 'note for C', b'1'),
(4, NULL, 'Cong ty D', 'Hoang Van D', 'hoangvand@gmail.com', '0123456780', '789 Hai Ba Trung, District 1', '4', 5, 7, '4', NULL, 'note for D', b'1'),
(5, NULL, 'Cong ty E', 'Pham Van E', 'phamvane@gmail.com', '0987123456', '101 Tran Hung Dao, District 5', '5', 1, 6, '1', NULL, 'note for E', b'1'),
(6, NULL, 'Cong ty F', 'Nguyen Van F', 'nguyenvanf@gmail.com', '0934567890', '202 Phan Van Tri, District 6', '6', 4, 3, '2', NULL, 'note for F', b'1'),
(7, NULL, 'Cong ty G', 'Vu Van G', 'vuvang@gmail.com', '0989123456', '303 Le Van Sy, District 10', '7', 6, 8, '3', NULL, 'note for G', b'1'),
(8, NULL, 'Cong ty H', 'Ngo Van H', 'ngovan@gmail.com', '0976543210', '404 Nguyen Hue, District 9', '8', 7, 2, '4', NULL, 'note for H', b'1'),
(9, NULL, 'Cong ty I', 'Vo Van I', 'vovan@gmail.com', '0965432109', '505 Tan Son Nhat, District 11', '9', 9, 1, '1', NULL, 'note for I', b'1'),
(10, NULL, 'Cong ty J', 'Bui Van J', 'buivan@gmail.com', '0954321098', '606 Le Van Khuyen, District 12', '10', 10, 9, '2', NULL, 'note for J', b'1'),
(21, NULL, 'Cong ty thanh', 'Nguyen The Thanh', 'thethanh@gmail.com', '0379513457', 'no', '9', 9, 10, '3', NULL, 'no', b'1'),
(22, NULL, 'Cong ty K', 'Tran Van K', 'tranvank@gmail.com', '0943210987', '707 Pasteur, District 3', '8', 2, 5, '4', NULL, 'note for K', b'1'),
(23, NULL, 'Cong ty L', 'Le Van L', 'levanl@gmail.com', '0932109876', '808 Tran Quang Khai, District 5', '7', 3, 6, '1', NULL, 'note for L', b'1'),
(24, NULL, 'Cong ty M', 'Hoang Van M', 'hoangvanm@gmail.com', '0921098765', '909 Ly Chinh Thang, District 10', '6', 4, 7, '2', NULL, 'note for M', b'1'),
(25, NULL, 'Cong ty N', 'Pham Van N', 'phamvann@gmail.com', '0910987654', '1010 Cach Mang Thang 8, District 11', '5', 5, 8, '3', NULL, 'note for N', b'1'),
(26, NULL, 'Cong ty O', 'Vo Van O', 'vovano@gmail.com', '0909876543', '2020 Hoang Sa, District 3', '4', 6, 9, '4', NULL, 'note for O', b'1'),
(27, NULL, 'Cong ty P', 'Bui Van P', 'buivanp@gmail.com', '0898765432', '3030 Truong Sa, District 5', '3', 7, 1, '1', NULL, 'note for P', b'1'),
(28, NULL, 'Cong ty Q', 'Nguyen Van Q', 'nguyenvanq@gmail.com', '0887654321', '4040 Dinh Tien Hoang, District 9', '2', 8, 2, '2', NULL, 'note for Q', b'1'),
(29, NULL, 'Cong ty R', 'Tran Van R', 'tranvanr@gmail.com', '0876543210', '5050 Dien Bien Phu, District 1', '1', 9, 3, '3', NULL, 'note for R', b'1'),
(30, NULL, 'Cong ty S', 'Le Van S', 'levans@gmail.com', '0865432109', '6060 Vo Van Kiet, District 10', '2', 10, 4, '4', NULL, 'note for S', b'1'),
(31, NULL, 'Cong ty T', 'Hoang Van T', 'hoangvant@gmail.com', '0854321098', '7070 Nguyen Trai, District 7', '3', 1, 5, '1', NULL, 'note for T', b'1'),
(32, NULL, 'Cong ty U', 'Pham Van U', 'phamvanu@gmail.com', '0843210987', '8080 Tran Hung Dao, District 12', '4', 2, 6, '2', NULL, 'note for U', b'1'),
(33, NULL, 'Cong ty V', 'Vo Van V', 'vovanv@gmail.com', '0832109876', '9090 To Hien Thanh, District 3', '5', 3, 7, '3', NULL, 'note for V', b'1'),
(34, NULL, 'Cong ty W', 'Vu Van W', 'vuvan@gmail.com', '0821098765', '1011 Nguyen Thi Minh Khai, District 5', '6', 4, 8, '4', NULL, 'note for W', b'1'),
(35, NULL, 'Cong ty X', 'Ngo Van X', 'ngovanx@gmail.com', '0810987654', '2021 Hai Ba Trung, District 1', '7', 5, 9, '1', NULL, 'note for X', b'1'),
(36, NULL, 'Cong ty Y', 'Bui Van Y', 'buivany@gmail.com', '0809876543', '3031 Le Loi, District 7', '8', 6, 1, '2', NULL, 'note for Y', b'1'),
(37, NULL, 'Cong ty Z', 'Nguyen Van Z', 'nguyenvanz@gmail.com', '0798765432', '4041 Ba Thang Hai, District 10', '9', 7, 2, '3', NULL, 'note for Z', b'1'),
(38, NULL, 'Cong ty AA', 'Tran Van AA', 'tranvana@gmail.com', '0787654321', '5051 Nguyen Tat Thanh, District 4', '1', 8, 3, '4', NULL, 'note for AA', b'1'),
(39, NULL, 'Cong ty BB', 'Le Van BB', 'levanbb@gmail.com', '0776543210', '6061 Binh Quoi, District 6', '2', 9, 4, '1', NULL, 'note for BB', b'1'),
(40, NULL, 'Cong ty CC', 'Hoang Van CC', 'hoangvancc@gmail.com', '0765432109', '7071 Bach Dang, District 2', '3', 10, 5, '2', NULL, 'note for CC', b'1'),
(41, NULL, 'Cong ty DD', 'Pham Van DD', 'phamvandd@gmail.com', '0754321098', '8081 Kha Van Can, District 1', '4', 1, 6, '3', NULL, 'note for DD', b'1'),
(42, NULL, 'test null', 'test', 'test@gmail.com', '079867231', '101 tran duc tho', '5', 4, 3, '4', NULL, 'test', b'1'),
(43, NULL, 'A', NULL, 'a', NULL, 'a', '6', 4, NULL, '1', NULL, NULL, b'1');

-- Thêm các dòng dữ liệu còn lại tùy vào yêu cầu của bạn

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
  `code` varchar(255) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `cus_id` bigint DEFAULT NULL,
  `order_cate_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkqn8njq5u457gy53d60t0wugw` (`customer_id`),
  KEY `FKa7v6uifcdehfy2mcvyapmx8t7` (`goods_category_id`),
  KEY `FKcxl4s7b1cwrysor1ydb4n950h` (`order_category_id`),
  KEY `FKh8jnbbs91jewbyt7geupryt7x` (`order_status_id`),
  KEY `FK5hrq5nh3sudxv50bsdv0kf1y2` (`cus_id`),
  CONSTRAINT `FK5hrq5nh3sudxv50bsdv0kf1y2` FOREIGN KEY (`cus_id`) REFERENCES `crm_customer` (`id`),
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
-- Table structure for table `crm_profession`
--

DROP TABLE IF EXISTS `crm_profession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crm_profession` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(99) NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_profession`
--

LOCK TABLES `crm_profession` WRITE;
/*!40000 ALTER TABLE `crm_profession` DISABLE KEYS */;
INSERT INTO `crm_profession` VALUES (1,'Giao duc',NULL),(2,'Y te',NULL),(3,'Thuc pham',NULL),(4,'Van phong pham',NULL),(5,'Dien',NULL),(6,'May moc',NULL),(7,'Tin hoc',NULL),(8,'Du lich',NULL),(9,'Kien truc',NULL),(10,'Cong nghe',NULL);
/*!40000 ALTER TABLE `crm_profession` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_responsible_person`
--

DROP TABLE IF EXISTS `crm_responsible_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crm_responsible_person` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(99) NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_responsible_person`
--

LOCK TABLES `crm_responsible_person` WRITE;
/*!40000 ALTER TABLE `crm_responsible_person` DISABLE KEYS */;
INSERT INTO `crm_responsible_person` VALUES (1,'Nguyen The Thanh',NULL),(2,'Nguyen Hoang Phuong Ngan',NULL),(3,'Nguyen Ngoc Cam Hanh',NULL),(4,'Le Ngoc Thach',NULL);
/*!40000 ALTER TABLE `crm_responsible_person` ENABLE KEYS */;
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

--
-- Table structure for table `customer_status`
--

DROP TABLE IF EXISTS `customer_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_status` (
  `customer_id` bigint NOT NULL,
  `status_id` bigint NOT NULL,
  PRIMARY KEY (`customer_id`,`status_id`),
  KEY `FK9mvprs6d1917jaqwckyna9i6d` (`status_id`),
  CONSTRAINT `FK9mvprs6d1917jaqwckyna9i6d` FOREIGN KEY (`status_id`) REFERENCES `crm_status` (`id`),
  CONSTRAINT `FKc7o25oslt7rn0qjtvdmr1aq2p` FOREIGN KEY (`customer_id`) REFERENCES `crm_customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_status`
--

LOCK TABLES `customer_status` WRITE;
/*!40000 ALTER TABLE `customer_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_category_status`
--

DROP TABLE IF EXISTS `order_category_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_category_status` (
  `order_category_id` bigint NOT NULL,
  `order_status_id` bigint NOT NULL,
  PRIMARY KEY (`order_category_id`,`order_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_category_status`
--

LOCK TABLES `order_category_status` WRITE;
/*!40000 ALTER TABLE `order_category_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_category_status` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-14  1:24:00

DROP TABLE IF EXISTS `customer_interaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `crm_customer_interaction` (
  `id` BIGINT AUTO_INCREMENT NOT NULL,
  `customer_id` bigint NOT NULL,
  `interaction_date` DATETIME NOT NULL,
  `content` TEXT NOT NULL,
  `next_plan` TEXT NOT NULL,
  `contact_person` TEXT NOT NULL,
   PRIMARY KEY (`id`),
   CONSTRAINT `FK_customer_interaction_customer` FOREIGN KEY (`customer_id`) REFERENCES `crm_customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO `crm_customer_interaction` (`customer_id`, `interaction_date`, `content`, `next_plan`, `contact_person`) 
VALUES
(1, '2024-12-01 10:30:00', 'Trao đổi về sản phẩm mới', 'Liên hệ lại vào tuần sau để cung cấp thêm thông tin', 'Thiện'),
(2, '2024-12-02 14:00:00', 'Giải quyết khiếu nại về đơn hàng', 'Kiểm tra và hoàn tất xử lý trước ngày 2024-12-05', 'Thiện'),
(3, '2024-12-03 09:15:00', 'Hỗ trợ cài đặt phần mềm', 'Theo dõi kết quả và gọi lại khách hàng vào ngày 2024-12-04', 'Thiện');
