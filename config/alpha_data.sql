-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.34 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.5.0.6677
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping data for table alpha_sub.barcode_type: ~1 rows (approximately)
INSERT IGNORE INTO `barcode_type` (`id`, `barcode_type`) VALUES
	(1, 'Code 128');

-- Dumping data for table alpha_sub.country: ~0 rows (approximately)
INSERT IGNORE INTO `country` (`id`, `country`) VALUES
	(1, 'Sri Lanka');

-- Dumping data for table alpha_sub.clients: ~3 rows (approximately)
INSERT IGNORE INTO `clients` (`id`, `name`, `code`, `email`, `city`, `phone`, `adresse`, `created_at`, `updated_at`, `deleted_at`, `country_id`) VALUES
	(0, 'Walk-In-Customer', NULL, 'default', NULL, 'default', NULL, NULL, NULL, NULL, NULL);

-- Dumping data for table alpha_sub.providers: ~3 rows (approximately)
INSERT IGNORE INTO `providers` (`id`, `name`, `code`, `email`, `phone`, `city`, `adresse`, `created_at`, `updated_at`, `deleted_at`, `country_id`) VALUES
	(0, 'Default-Supplier', NULL, 'default', 'default', NULL, NULL, NULL, NULL, NULL, 1);

-- Dumping data for table alpha_sub.roles: ~2 rows (approximately)
INSERT IGNORE INTO `roles` (`id`, `name`, `label`, `description`, `created_at`, `updated_at`, `deleted_at`, `status`) VALUES
	(1, 'Owner', NULL, NULL, NULL, NULL, NULL, 0),
	(2, 'User', NULL, NULL, NULL, NULL, NULL, 0);

-- Dumping data for table alpha_sub.tax_method: ~2 rows (approximately)
INSERT IGNORE INTO `tax_method` (`id`, `method`) VALUES
	(2, 'Inclusive'),
	(3, 'Exclusive');

-- Dumping data for table alpha_sub.shop: ~1 rows (approximately)
INSERT IGNORE INTO `shop` (`id`, `name`, `address`, `mobile`, `email`, `logo`, `logoPath`) VALUES
	(1, 'Alpha Engineering', '32, Annapitiya Road, Tangalle.', '0705111696', 'admin@teamalpha.lk', NULL, 'logo//logo.png');

-- Dumping data for table alpha_sub.users: ~2 rows (approximately)
INSERT IGNORE INTO `users` (`id`, `firstname`, `lastname`, `username`, `email`, `password`, `avatar`, `phone`, `role_id`, `status`, `is_all_warehouses`, `created_at`, `updated_at`, `deleted_at`, `shop_id`) VALUES
	(1, 'Admin', 'Admin', 'Admin', 'Admin@gmail.com', 'Admin123', NULL, '0768701147', 1, 1, 0, NULL, NULL, NULL, 1),
	(2, 'Sahan', 'Sahan', 'Sahan', 'Sahan@gmail.com', 'Sahan123', NULL, '0768701148', 1, 1, 0, NULL, NULL, NULL, 1);

UPDATE `shop` SET `logo2Path` = 'logo//logo2.png' WHERE `id` = '1';

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
