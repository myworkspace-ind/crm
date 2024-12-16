use crm;
-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: crm
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
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
 SET character_set_client = utf8mb4 ;
CREATE TABLE `crm_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_status` bit(1) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `company_name` varchar(99) DEFAULT NULL,
  `contact_person` varchar(99) DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `email` varchar(99) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  `main_status_id` bigint(20) DEFAULT NULL,
  `profession_id` bigint(20) DEFAULT NULL,
  `responsible_person_id` bigint(20) DEFAULT NULL,
  `sub_status_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkdeyvb8vcb3luc9rjbdq5ked0` (`main_status_id`),
  KEY `FKioibte5tkd45e6165bugbd7c0` (`profession_id`),
  KEY `FK1veyaemqknyilm0th61lvqfff` (`responsible_person_id`),
  KEY `FKfomguaxykk0028fg59gwixnce` (`sub_status_id`),
  CONSTRAINT `FK1veyaemqknyilm0th61lvqfff` FOREIGN KEY (`responsible_person_id`) REFERENCES `crm_responsible_person` (`id`),
  CONSTRAINT `FKfomguaxykk0028fg59gwixnce` FOREIGN KEY (`sub_status_id`) REFERENCES `crm_status` (`id`),
  CONSTRAINT `FKioibte5tkd45e6165bugbd7c0` FOREIGN KEY (`profession_id`) REFERENCES `crm_profession` (`id`),
  CONSTRAINT `FKkdeyvb8vcb3luc9rjbdq5ked0` FOREIGN KEY (`main_status_id`) REFERENCES `crm_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_customer`
--

LOCK TABLES `crm_customer` WRITE;
/*!40000 ALTER TABLE `crm_customer` DISABLE KEYS */;
INSERT INTO `crm_customer` VALUES (1,_binary '','B56 Lê Văn Sỹ, Quận 3 TPHCM','Công ty Thiết kế Nội thất TPHCM','Nguyễn Quốc Anhhh','2024-12-09','quocanh.nguyen@gmail.com','nothing','0325678156',NULL,1,5,2,13),(2,_binary '','A123 duong Vo Nguyen Gap','UTE','Nguyen Hoang Phuong Ngan','2024-12-09','phuong.123@gmail.com','asv','0987777777',NULL,6,2,3,13),(3,_binary '','C89 Lê Văn Thọ, Quận 3 TPHCM','Công ty An toàn thực phẩm New Food','Lê Thị Tuyết Ngân','2024-12-09','tngan.nguyen890@gmail.com','New notettt','0345672134',NULL,5,21,2,13),(5,_binary '','Duong Tua Lua, xa Binh Thuong1213','Công ty MeKong Solutionsss','Lê Văn C','2024-12-10','cvanle@gmail.com','Ghi chú mới','0988765432',NULL,5,4,1,6),(6,_binary '','ABC đường DHF, quận Bình Thạnh','Công ty Thiết kế thời trang','Trần Ánh Nguyệt','2024-12-12','anhnguyet@gmail.com',NULL,'9876111111',NULL,7,5,2,13),(10,_binary '','Số 12, Đường Lê Lợi, Quận 1, TP. Hồ Chí Minh','Công ty TNHH Phần Mềm Việt Hưng','Nguyễn Văn Hùng','2024-12-12','hungnv@vietsoftware.vn','Khách hàng mới cần xây dựng quan hệ','0912345678',NULL,10,3,2,13),(11,_binary '','45 Đường Nguyễn Văn Cừ, Quận Long Biên, TP. Hà Nội','Công ty Cổ phần Dịch Vụ FPT','Lê Thanh Tùng','2024-12-12','tunglt@fptservices.vn','Thường xuyên tham gia dự án lớn','0987654321',NULL,8,3,2,2),(12,_binary '','34 Đường Trường Sơn, Quận Tân Bình, TP. Hồ Chí Minh','Công ty TNHH Xuất Nhập Khẩu Đại Dương Xanh','Phạm Văn Hải','2024-12-12','haipv@daiduongxanh.vn','Đang có nhu cầu mở rộng dịch vụ','0934567890',NULL,7,6,3,10);
/*!40000 ALTER TABLE `crm_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_customer_interaction`
--

DROP TABLE IF EXISTS `crm_customer_interaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `crm_customer_interaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contact_person` varchar(99) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `interaction_date` datetime(6) DEFAULT NULL,
  `next_plan` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKeg3sojlsbai9710ojw37rste6` (`customer_id`),
  CONSTRAINT `FKeg3sojlsbai9710ojw37rste6` FOREIGN KEY (`customer_id`) REFERENCES `crm_customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_customer_interaction`
--

LOCK TABLES `crm_customer_interaction` WRITE;
/*!40000 ALTER TABLE `crm_customer_interaction` DISABLE KEYS */;
INSERT INTO `crm_customer_interaction` VALUES (1,'Phương Ngân','Content 1','2024-12-05 00:00:00.000000','Plan 1',1),(2,'Thế Thành','Content 2','2024-12-03 00:00:00.000000','Plan 2',1),(3,'Trần Anh','Content 1','2024-12-03 00:00:00.000000','Plan 1',2),(4,'Ngọc Thạch','Content 3','2024-11-21 00:00:00.000000','Plan 3',1),(5,'Ngọc Thạch','Content 3','2024-11-21 00:00:00.000000','Plan 3',5);
/*!40000 ALTER TABLE `crm_customer_interaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_goodscategory`
--

DROP TABLE IF EXISTS `crm_goodscategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `crm_goodscategory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_goodscategory`
--

LOCK TABLES `crm_goodscategory` WRITE;
/*!40000 ALTER TABLE `crm_goodscategory` DISABLE KEYS */;
INSERT INTO `crm_goodscategory` VALUES (1,'Mỹ phẩm',NULL),(2,'Trái cây',NULL),(3,'Thực phẩm',NULL),(4,'Thiết bị gia dụng',NULL),(5,'Thiết bị điện tử',NULL),(6,'Đồ dùng văn phòng/học tập',NULL);
/*!40000 ALTER TABLE `crm_goodscategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_order`
--

DROP TABLE IF EXISTS `crm_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `crm_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `customer_requirement` varchar(255) DEFAULT NULL,
  `delivery_date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  `transportation_method` varchar(255) DEFAULT NULL,
  `goods_category_id` bigint(20) DEFAULT NULL,
  `order_cate_id` bigint(20) DEFAULT NULL,
  `order_status_id` bigint(20) DEFAULT NULL,
  `receiver_id` bigint(20) DEFAULT NULL,
  `sender_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa7v6uifcdehfy2mcvyapmx8t7` (`goods_category_id`),
  KEY `FKsvogcsppdtl21v1467rwciuqo` (`order_cate_id`),
  KEY `FKh8jnbbs91jewbyt7geupryt7x` (`order_status_id`),
  KEY `FKsf5kc5yjyrhlf5ycrr4faqci0` (`receiver_id`),
  KEY `FKh3iutx15xrfqfocoaha9r0v87` (`sender_id`),
  CONSTRAINT `FKa7v6uifcdehfy2mcvyapmx8t7` FOREIGN KEY (`goods_category_id`) REFERENCES `crm_goodscategory` (`id`),
  CONSTRAINT `FKh3iutx15xrfqfocoaha9r0v87` FOREIGN KEY (`sender_id`) REFERENCES `crm_customer` (`id`),
  CONSTRAINT `FKh8jnbbs91jewbyt7geupryt7x` FOREIGN KEY (`order_status_id`) REFERENCES `crm_orderstatus` (`id`),
  CONSTRAINT `FKsf5kc5yjyrhlf5ycrr4faqci0` FOREIGN KEY (`receiver_id`) REFERENCES `crm_customer` (`id`),
  CONSTRAINT `FKsvogcsppdtl21v1467rwciuqo` FOREIGN KEY (`order_cate_id`) REFERENCES `crm_ordercategory` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_order`
--

LOCK TABLES `crm_order` WRITE;
/*!40000 ALTER TABLE `crm_order` DISABLE KEYS */;
INSERT INTO `crm_order` VALUES (1,'Đầu hẻm B78 đường Nguyễn Đình Chiểuuu','IP6678','2024-12-09 00:00:00.000000','Nothingggg','2024-12-18 00:00:00.000000',NULL,NULL,'Xe hơiiii',1,1,2,2,1),(2,'Trước nhà','B67890','2024-12-10 00:00:00.000000','Đem đến đúng yêu cầu nhé','2024-12-14 00:00:00.000000',NULL,NULL,'Xe chuyên chở',4,2,2,3,2);
/*!40000 ALTER TABLE `crm_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_ordercategory`
--

DROP TABLE IF EXISTS `crm_ordercategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `crm_ordercategory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_ordercategory`
--

LOCK TABLES `crm_ordercategory` WRITE;
/*!40000 ALTER TABLE `crm_ordercategory` DISABLE KEYS */;
INSERT INTO `crm_ordercategory` VALUES (1,'Mặc định','Loại đơn hàng mặc định ban đầu.',NULL),(2,'Máy móc','Đơn hàng liên quan đến các thiết bị máy móc.',NULL),(3,'Mỹ phẩm','Sản phẩm chăm sóc sắc đẹp và làm đẹp.',NULL),(4,'Điện tử','Đơn hàng liên quan đến các thiết bị điện tử.',NULL),(5,'Hàng gia dụng','Đơn hàng về đồ dùng gia đình như bếp, tủ lạnh.',NULL);
/*!40000 ALTER TABLE `crm_ordercategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_orderstatus`
--

DROP TABLE IF EXISTS `crm_orderstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `crm_orderstatus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_orderstatus`
--

LOCK TABLES `crm_orderstatus` WRITE;
/*!40000 ALTER TABLE `crm_orderstatus` DISABLE KEYS */;
INSERT INTO `crm_orderstatus` VALUES (1,'Nhận đơn',NULL),(2,'Bóc hàng',NULL),(3,'Trữ lạnh',NULL),(4,'Đóng gói',NULL),(5,'Vận chuyển',NULL),(6,'Giao hàng',NULL);
/*!40000 ALTER TABLE `crm_orderstatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_profession`
--

DROP TABLE IF EXISTS `crm_profession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `crm_profession` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(99) NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_profession`
--

LOCK TABLES `crm_profession` WRITE;
/*!40000 ALTER TABLE `crm_profession` DISABLE KEYS */;
INSERT INTO `crm_profession` VALUES (1,'Y tế',''),(2,'Giáo dục',''),(3,'Công nghệ',''),(4,'Kinh doanh - Tài chính',''),(5,'Sáng tạo - Nghệ thuật',''),(6,'Công nghiệp - Sản xuất',''),(7,'Luật pháp',''),(8,'Thương mại điện tử',''),(9,'Nông nghiệp',''),(10,'Kỹ thuật - Cơ khí',''),(11,'Hàng không - Vũ trụ',''),(12,'Môi trường',''),(13,'Du lịch - Nhà hàng - Khách sạn',''),(14,'Truyền thông - Marketing',''),(15,'Logistics - Chuỗi cung ứng',''),(16,'Năng lượng',''),(17,'Dịch vụ công',''),(18,'Thể thao - Giải trí',''),(19,'Khoa học - Nghiên cứu',''),(20,'Chăm sóc sắc đẹp',''),(21,'Thực phẩm - Đồ uống','');
/*!40000 ALTER TABLE `crm_profession` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_responsible_person`
--

DROP TABLE IF EXISTS `crm_responsible_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `crm_responsible_person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(99) NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_responsible_person`
--

LOCK TABLES `crm_responsible_person` WRITE;
/*!40000 ALTER TABLE `crm_responsible_person` DISABLE KEYS */;
INSERT INTO `crm_responsible_person` VALUES (1,'Lê Ngọc Thạch',''),(2,'Nguyễn Hoàng Phương Ngân',''),(3,'Nguyễn Thế Thành','');
/*!40000 ALTER TABLE `crm_responsible_person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_status`
--

DROP TABLE IF EXISTS `crm_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `crm_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `backgroundColor` varchar(99) DEFAULT NULL,
  `name` varchar(99) DEFAULT NULL,
  `site_id` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_status`
--

LOCK TABLES `crm_status` WRITE;
/*!40000 ALTER TABLE `crm_status` DISABLE KEYS */;
INSERT INTO `crm_status` VALUES (1,'#c0392b','Mới',NULL),(2,'#e74c3c','Tư vấn',NULL),(3,'#9b59b6','KH tiềm năng',NULL),(4,'#8e44ad','Báo giá khách hàng',NULL),(5,'#2980b9','Đàm phán',NULL),(6,'#2e86c1','Chốt hợp đồng',NULL),(7,'#2e86c1','Báo giá',NULL),(8,'#27ae60','Chốt',NULL),(9,'#27ae60','Data rớt',NULL),(10,'#e67e22','Chăm lại',NULL),(11,'#e67e22','Đến nhưng không ở lại',NULL),(12,'#48484a','Mới cập nhật',NULL),(13,'#48484a','Đừng quên',NULL);
/*!40000 ALTER TABLE `crm_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_category_status`
--

DROP TABLE IF EXISTS `order_category_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `order_category_status` (
  `order_category_id` bigint(20) NOT NULL,
  `order_status_id` bigint(20) NOT NULL,
  PRIMARY KEY (`order_category_id`,`order_status_id`),
  KEY `FKd3wk4in9sf7su3hdsvufhqs56` (`order_status_id`),
  CONSTRAINT `FKd3wk4in9sf7su3hdsvufhqs56` FOREIGN KEY (`order_status_id`) REFERENCES `crm_orderstatus` (`id`),
  CONSTRAINT `FKm2y48411pwtgasyfchuf79jq5` FOREIGN KEY (`order_category_id`) REFERENCES `crm_ordercategory` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_category_status`
--

LOCK TABLES `order_category_status` WRITE;
/*!40000 ALTER TABLE `order_category_status` DISABLE KEYS */;
INSERT INTO `order_category_status` VALUES (1,1),(2,1),(3,1),(1,2),(2,2),(3,2),(2,3),(3,3),(1,4),(2,4),(3,4),(1,5),(2,5),(3,5),(1,6),(2,6),(3,6);
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

-- Dump completed on 2024-12-12 21:59:11
