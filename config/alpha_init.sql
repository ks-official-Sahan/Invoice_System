-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema alpha_sub
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema alpha_sub
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `alpha_sub` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `alpha_sub` ;

-- -----------------------------------------------------
-- Table `alpha_sub`.`brands`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`brands` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(192) NOT NULL,
  `description` VARCHAR(192) NULL DEFAULT NULL,
  `image` VARCHAR(192) NULL DEFAULT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`categories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(192) NULL,
  `name` VARCHAR(192) NOT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`country`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`country` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `country` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `alpha_sub`.`clients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`clients` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(191) NOT NULL,
  `code` INT NULL,
  `email` VARCHAR(192) NOT NULL,
  `city` VARCHAR(191) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NULL DEFAULT NULL,
  `phone` VARCHAR(191) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NULL DEFAULT NULL,
  `adresse` VARCHAR(191) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NULL DEFAULT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  `country_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_clients_country1_idx` (`country_id` ASC) VISIBLE,
  CONSTRAINT `fk_clients_country1`
    FOREIGN KEY (`country_id`)
    REFERENCES `alpha_sub`.`country` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`currencies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`currencies` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(192) NOT NULL,
  `name` VARCHAR(192) NOT NULL,
  `symbol` VARCHAR(192) NOT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`password_resets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`password_resets` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(191) NOT NULL,
  `token` VARCHAR(191) NOT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `email` (`email` ASC) VISIBLE,
  INDEX `token` (`token` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`providers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`providers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(191) NOT NULL,
  `code` INT NULL,
  `email` VARCHAR(192) NOT NULL,
  `phone` VARCHAR(191) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NULL DEFAULT NULL,
  `city` VARCHAR(191) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NULL DEFAULT NULL,
  `adresse` VARCHAR(191) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NULL DEFAULT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  `country_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_providers_country1_idx` (`country_id` ASC) VISIBLE,
  CONSTRAINT `fk_providers_country1`
    FOREIGN KEY (`country_id`)
    REFERENCES `alpha_sub`.`country` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`shop`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`shop` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `address` VARCHAR(150) NULL,
  `mobile` VARCHAR(12) NULL,
  `email` VARCHAR(100) NULL,
  `logo` BLOB NULL,
  `logoPath` VARCHAR(150) NULL,
  `logo2` BLOB NULL,
  `logo2Path` VARCHAR(150) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `alpha_sub`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(191) NOT NULL,
  `lastname` VARCHAR(191) NOT NULL,
  `username` VARCHAR(192) NOT NULL,
  `email` VARCHAR(192) NOT NULL,
  `password` VARCHAR(191) NOT NULL,
  `avatar` VARCHAR(191) NULL DEFAULT NULL,
  `phone` VARCHAR(192) NOT NULL,
  `role_id` INT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT '1',
  `is_all_warehouses` TINYINT(1) NOT NULL DEFAULT '0',
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  `shop_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_users_shop1_idx` (`shop_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_shop1`
    FOREIGN KEY (`shop_id`)
    REFERENCES `alpha_sub`.`shop` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`purchases`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`purchases` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `Ref` VARCHAR(192) NOT NULL,
  `date` DATE NOT NULL,
  `provider_id` INT NULL DEFAULT 0,
  `warehouse_id` INT NULL,
  `tax_rate` DOUBLE NULL DEFAULT '0',
  `TaxNet` DOUBLE NULL DEFAULT '0',
  `discount` DOUBLE NULL DEFAULT '0',
  `shipping` DOUBLE NULL DEFAULT '0',
  `GrandTotal` DOUBLE NOT NULL,
  `paid_amount` DOUBLE NOT NULL DEFAULT '0',
  `statut` VARCHAR(191) NOT NULL,
  `payment_statut` VARCHAR(192) NOT NULL,
  `notes` TEXT NULL DEFAULT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_id_purchases` (`user_id` ASC) VISIBLE,
  INDEX `provider_id` (`provider_id` ASC) VISIBLE,
  CONSTRAINT `provider_id`
    FOREIGN KEY (`provider_id`)
    REFERENCES `alpha_sub`.`providers` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `user_id_purchases`
    FOREIGN KEY (`user_id`)
    REFERENCES `alpha_sub`.`users` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`payment_purchases`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`payment_purchases` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `Ref` VARCHAR(192) NOT NULL,
  `purchase_id` INT NOT NULL,
  `montant` DOUBLE NOT NULL,
  `change` DOUBLE NOT NULL DEFAULT '0',
  `Reglement` VARCHAR(192) NOT NULL,
  `notes` TEXT NULL DEFAULT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_id_payment_purchases` (`user_id` ASC) VISIBLE,
  INDEX `payments_purchase_id` (`purchase_id` ASC) VISIBLE,
  CONSTRAINT `factures_purchase_id`
    FOREIGN KEY (`purchase_id`)
    REFERENCES `alpha_sub`.`purchases` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `user_id_factures_achat`
    FOREIGN KEY (`user_id`)
    REFERENCES `alpha_sub`.`users` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`sales`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`sales` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `Ref` VARCHAR(192) NOT NULL,
  `is_pos` TINYINT(1) NULL DEFAULT '0',
  `client_id` INT NULL DEFAULT 0,
  `warehouse_id` INT NULL,
  `tax_rate` DOUBLE NULL DEFAULT '0',
  `TaxNet` DOUBLE NULL DEFAULT '0',
  `discount` DOUBLE NULL DEFAULT '0',
  `shipping` DOUBLE NULL DEFAULT '0',
  `GrandTotal` DOUBLE NOT NULL DEFAULT '0',
  `paid_amount` DOUBLE NOT NULL DEFAULT '0',
  `payment_statut` VARCHAR(192) NOT NULL,
  `statut` VARCHAR(191) NOT NULL,
  `notes` TEXT NULL DEFAULT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  `shipping_status` VARCHAR(191) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_id_sales` (`user_id` ASC) VISIBLE,
  INDEX `sale_client_id` (`client_id` ASC) VISIBLE,
  CONSTRAINT `sale_client_id`
    FOREIGN KEY (`client_id`)
    REFERENCES `alpha_sub`.`clients` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `user_id_sales`
    FOREIGN KEY (`user_id`)
    REFERENCES `alpha_sub`.`users` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`payment_sales`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`payment_sales` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `Ref` VARCHAR(192) NOT NULL,
  `sale_id` INT NOT NULL,
  `montant` DOUBLE NOT NULL,
  `change` DOUBLE NOT NULL DEFAULT '0',
  `Reglement` VARCHAR(192) NOT NULL,
  `notes` TEXT NULL DEFAULT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_id_payments_sale` (`user_id` ASC) VISIBLE,
  INDEX `payment_sale_id` (`sale_id` ASC) VISIBLE,
  CONSTRAINT `facture_sale_id`
    FOREIGN KEY (`sale_id`)
    REFERENCES `alpha_sub`.`sales` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `user_id_factures_ventes`
    FOREIGN KEY (`user_id`)
    REFERENCES `alpha_sub`.`users` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`payment_with_credit_card`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`payment_with_credit_card` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `payment_id` INT NOT NULL,
  `customer_id` INT NOT NULL,
  `customer_stripe_id` VARCHAR(192) NOT NULL,
  `charge_id` VARCHAR(192) NOT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`permissions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`permissions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(192) NOT NULL,
  `label` VARCHAR(192) NULL DEFAULT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 106
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(192) NOT NULL,
  `label` VARCHAR(192) NULL DEFAULT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  `status` INT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`permission_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`permission_role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `permission_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `permission_role_permission_id` (`permission_id` ASC) VISIBLE,
  INDEX `permission_role_role_id` (`role_id` ASC) VISIBLE,
  CONSTRAINT `permission_role_permission_id`
    FOREIGN KEY (`permission_id`)
    REFERENCES `alpha_sub`.`permissions` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `permission_role_role_id`
    FOREIGN KEY (`role_id`)
    REFERENCES `alpha_sub`.`roles` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
AUTO_INCREMENT = 106
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`units`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`units` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(192) NOT NULL,
  `ShortName` VARCHAR(192) NOT NULL,
  `base_unit` INT NULL DEFAULT NULL,
  `operator` VARCHAR(192) NULL DEFAULT '*',
  `operator_value` DOUBLE NULL DEFAULT '1',
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `base_unit` (`base_unit` ASC) VISIBLE,
  CONSTRAINT `base_unit`
    FOREIGN KEY (`base_unit`)
    REFERENCES `alpha_sub`.`units` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`barcode_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`barcode_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `barcode_type` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `alpha_sub`.`tax_method`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`tax_method` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `method` ENUM('Inclusive', 'Exclusive') NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `alpha_sub`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`products` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(192) NOT NULL,
  `name` VARCHAR(192) NOT NULL,
  `cost` DOUBLE NULL,
  `price` DOUBLE NULL,
  `category_id` INT NULL,
  `brand_id` INT NULL DEFAULT NULL,
  `unit_id` INT NULL DEFAULT NULL,
  `unit_sale_id` INT NULL DEFAULT NULL,
  `unit_purchase_id` INT NULL DEFAULT NULL,
  `TaxNet` DOUBLE NULL DEFAULT '0',
  `image` TEXT NULL DEFAULT NULL,
  `note` TEXT NULL DEFAULT NULL,
  `stock_alert` DOUBLE NULL DEFAULT '0',
  `is_variant` TINYINT(1) NOT NULL DEFAULT '0',
  `is_imei` TINYINT(1) NOT NULL DEFAULT '0',
  `is_active` TINYINT(1) NULL DEFAULT '1',
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  `barcode_type_id` INT NOT NULL,
  `tax_method_id` INT NULL,
  `product_type` ENUM('product', 'service') NULL DEFAULT 'product',
  `sale_price` DOUBLE NULL,
  PRIMARY KEY (`id`),
  INDEX `category_id` (`category_id` ASC) VISIBLE,
  INDEX `brand_id_products` (`brand_id` ASC) VISIBLE,
  INDEX `unit_id_products` (`unit_id` ASC) VISIBLE,
  INDEX `unit_id_sales` (`unit_sale_id` ASC) VISIBLE,
  INDEX `unit_purchase_products` (`unit_purchase_id` ASC) VISIBLE,
  INDEX `fk_products_barcode_type1_idx` (`barcode_type_id` ASC) VISIBLE,
  INDEX `fk_products_tax_method1_idx` (`tax_method_id` ASC) VISIBLE,
  CONSTRAINT `brand_id_products`
    FOREIGN KEY (`brand_id`)
    REFERENCES `alpha_sub`.`brands` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `category_id`
    FOREIGN KEY (`category_id`)
    REFERENCES `alpha_sub`.`categories` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `unit_id_products`
    FOREIGN KEY (`unit_id`)
    REFERENCES `alpha_sub`.`units` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `unit_id_sales`
    FOREIGN KEY (`unit_sale_id`)
    REFERENCES `alpha_sub`.`units` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `unit_purchase_products`
    FOREIGN KEY (`unit_purchase_id`)
    REFERENCES `alpha_sub`.`units` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_products_barcode_type1`
    FOREIGN KEY (`barcode_type_id`)
    REFERENCES `alpha_sub`.`barcode_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_tax_method1`
    FOREIGN KEY (`tax_method_id`)
    REFERENCES `alpha_sub`.`tax_method` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`product_variants`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`product_variants` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NULL DEFAULT NULL,
  `name` VARCHAR(192) NULL DEFAULT NULL,
  `qty` DECIMAL(8,2) NULL DEFAULT '0.00',
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `product_id_variant` (`product_id` ASC) VISIBLE,
  CONSTRAINT `product_id_variant`
    FOREIGN KEY (`product_id`)
    REFERENCES `alpha_sub`.`products` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`stocks`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`stocks` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `code` VARCHAR(45) NULL,
  `cost` DOUBLE NOT NULL,
  `price` DOUBLE NOT NULL,
  `is_expire` TINYINT(1) NOT NULL DEFAULT '0',
  `exp_date` DATE NULL,
  `mfd_date` DATE NULL,
  `quantity` DOUBLE NOT NULL,
  `products_id` INT NOT NULL,
  `sale_price` DOUBLE NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_stocks_products1_idx` (`products_id` ASC) VISIBLE,
  CONSTRAINT `fk_stocks_products1`
    FOREIGN KEY (`products_id`)
    REFERENCES `alpha_sub`.`products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `alpha_sub`.`purchase_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`purchase_details` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `cost` DOUBLE NOT NULL,
  `purchase_unit_id` INT NULL DEFAULT NULL,
  `TaxNet` DOUBLE NULL DEFAULT '0',
  `discount` DOUBLE NULL DEFAULT '0',
  `discount_method` VARCHAR(192) NULL DEFAULT '1',
  `purchase_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  `product_variant_id` INT NULL DEFAULT NULL,
  `imei_number` TEXT NULL DEFAULT NULL,
  `total` DOUBLE NOT NULL,
  `quantity` DOUBLE NOT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `tax_method_id` INT NULL,
  `stocks_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `purchase_id` (`purchase_id` ASC) VISIBLE,
  INDEX `product_id` (`product_id` ASC) VISIBLE,
  INDEX `purchase_product_variant_id` (`product_variant_id` ASC) VISIBLE,
  INDEX `purchase_unit_id_purchase` (`purchase_unit_id` ASC) VISIBLE,
  INDEX `fk_purchase_details_tax_method1_idx` (`tax_method_id` ASC) VISIBLE,
  INDEX `fk_purchase_details_stocks1_idx` (`stocks_id` ASC) VISIBLE,
  CONSTRAINT `product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `alpha_sub`.`products` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `purchase_id`
    FOREIGN KEY (`purchase_id`)
    REFERENCES `alpha_sub`.`purchases` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `purchase_product_variant_id`
    FOREIGN KEY (`product_variant_id`)
    REFERENCES `alpha_sub`.`product_variants` (`id`),
  CONSTRAINT `purchase_unit_id_purchase`
    FOREIGN KEY (`purchase_unit_id`)
    REFERENCES `alpha_sub`.`units` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_purchase_details_tax_method1`
    FOREIGN KEY (`tax_method_id`)
    REFERENCES `alpha_sub`.`tax_method` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_purchase_details_stocks1`
    FOREIGN KEY (`stocks_id`)
    REFERENCES `alpha_sub`.`stocks` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`quotations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`quotations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `Ref` VARCHAR(192) NOT NULL,
  `client_id` INT NOT NULL,
  `warehouse_id` INT NULL,
  `tax_rate` DOUBLE NULL DEFAULT '0',
  `TaxNet` DOUBLE NULL DEFAULT '0',
  `discount` DOUBLE NULL DEFAULT '0',
  `shipping` DOUBLE NULL DEFAULT '0',
  `GrandTotal` DOUBLE NOT NULL,
  `statut` VARCHAR(192) NOT NULL,
  `notes` TEXT NULL DEFAULT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_id_quotation` (`user_id` ASC) VISIBLE,
  INDEX `client_id_quotation` (`client_id` ASC) VISIBLE,
  CONSTRAINT `client_id _quotation`
    FOREIGN KEY (`client_id`)
    REFERENCES `alpha_sub`.`clients` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `user_id_quotation`
    FOREIGN KEY (`user_id`)
    REFERENCES `alpha_sub`.`users` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`quotation_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`quotation_details` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `price` DOUBLE NOT NULL,
  `sale_unit_id` INT NULL DEFAULT NULL,
  `TaxNet` DOUBLE NULL DEFAULT '0',
  `discount` DOUBLE NULL DEFAULT '0',
  `discount_method` VARCHAR(192) NULL DEFAULT '1',
  `total` DOUBLE NOT NULL,
  `quantity` DOUBLE NOT NULL,
  `product_id` INT NOT NULL,
  `product_variant_id` INT NULL DEFAULT NULL,
  `imei_number` TEXT NULL DEFAULT NULL,
  `quotation_id` INT NOT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `tax_method_id` INT NULL,
  `stocks_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `product_id_quotation_details` (`product_id` ASC) VISIBLE,
  INDEX `quote_product_variant_id` (`product_variant_id` ASC) VISIBLE,
  INDEX `quotation_id` (`quotation_id` ASC) VISIBLE,
  INDEX `sale_unit_id_quotation` (`sale_unit_id` ASC) VISIBLE,
  INDEX `fk_quotation_details_tax_method1_idx` (`tax_method_id` ASC) VISIBLE,
  INDEX `fk_quotation_details_stocks1_idx` (`stocks_id` ASC) VISIBLE,
  CONSTRAINT `product_id_quotation_details`
    FOREIGN KEY (`product_id`)
    REFERENCES `alpha_sub`.`products` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `quotation_id`
    FOREIGN KEY (`quotation_id`)
    REFERENCES `alpha_sub`.`quotations` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `quote_product_variant_id`
    FOREIGN KEY (`product_variant_id`)
    REFERENCES `alpha_sub`.`product_variants` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `sale_unit_id_quotation`
    FOREIGN KEY (`sale_unit_id`)
    REFERENCES `alpha_sub`.`units` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_quotation_details_tax_method1`
    FOREIGN KEY (`tax_method_id`)
    REFERENCES `alpha_sub`.`tax_method` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quotation_details_stocks1`
    FOREIGN KEY (`stocks_id`)
    REFERENCES `alpha_sub`.`stocks` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`role_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`role_user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `role_user_user_id` (`user_id` ASC) VISIBLE,
  INDEX `role_user_role_id` (`role_id` ASC) VISIBLE,
  CONSTRAINT `role_user_role_id`
    FOREIGN KEY (`role_id`)
    REFERENCES `alpha_sub`.`roles` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `role_user_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `alpha_sub`.`users` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`sale_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`sale_details` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATE NOT NULL,
  `sale_id` INT NOT NULL,
  `product_id` INT NULL,
  `product_variant_id` INT NULL DEFAULT NULL,
  `imei_number` TEXT NULL DEFAULT NULL,
  `price` DOUBLE NOT NULL,
  `sale_unit_id` INT NULL DEFAULT NULL,
  `TaxNet` DOUBLE NULL DEFAULT NULL,
  `discount` DOUBLE NULL DEFAULT NULL,
  `discount_method` VARCHAR(192) NULL DEFAULT '1',
  `total` DOUBLE NOT NULL,
  `quantity` DOUBLE NOT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `tax_method_id` INT NULL,
  `stocks_id` INT NULL,
  `is_sale_price` TINYINT(1) NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `Details_Sale_id` (`sale_id` ASC) VISIBLE,
  INDEX `sale_product_id` (`product_id` ASC) VISIBLE,
  INDEX `sale_product_variant_id` (`product_variant_id` ASC) VISIBLE,
  INDEX `sales_sale_unit_id` (`sale_unit_id` ASC) VISIBLE,
  INDEX `fk_sale_details_tax_method1_idx` (`tax_method_id` ASC) VISIBLE,
  INDEX `fk_sale_details_stocks1_idx` (`stocks_id` ASC) VISIBLE,
  CONSTRAINT `Details_Sale_id`
    FOREIGN KEY (`sale_id`)
    REFERENCES `alpha_sub`.`sales` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `sale_product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `alpha_sub`.`products` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `sale_product_variant_id`
    FOREIGN KEY (`product_variant_id`)
    REFERENCES `alpha_sub`.`product_variants` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `sales_sale_unit_id`
    FOREIGN KEY (`sale_unit_id`)
    REFERENCES `alpha_sub`.`units` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_sale_details_tax_method1`
    FOREIGN KEY (`tax_method_id`)
    REFERENCES `alpha_sub`.`tax_method` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sale_details_stocks1`
    FOREIGN KEY (`stocks_id`)
    REFERENCES `alpha_sub`.`stocks` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `alpha_sub`.`settings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alpha_sub`.`settings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(191) NOT NULL,
  `currency_id` INT NULL DEFAULT NULL,
  `CompanyName` VARCHAR(191) NOT NULL,
  `CompanyPhone` VARCHAR(191) NOT NULL,
  `CompanyAdress` VARCHAR(191) NOT NULL,
  `logo` VARCHAR(191) NULL DEFAULT NULL,
  `created_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `updated_at` TIMESTAMP(6) NULL DEFAULT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  `footer` VARCHAR(192) NOT NULL DEFAULT 'Stocky - Ultimate Inventory With POS',
  `developed_by` VARCHAR(192) NOT NULL DEFAULT 'Stocky',
  `client_id` INT NULL DEFAULT NULL,
  `warehouse_id` INT NULL DEFAULT NULL,
  `default_language` VARCHAR(192) NOT NULL DEFAULT 'en',
  PRIMARY KEY (`id`),
  INDEX `currency_id` (`currency_id` ASC) VISIBLE,
  INDEX `client_id` (`client_id` ASC) VISIBLE,
  CONSTRAINT `currency_id`
    FOREIGN KEY (`currency_id`)
    REFERENCES `alpha_sub`.`currencies` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `settings_client_id`
    FOREIGN KEY (`client_id`)
    REFERENCES `alpha_sub`.`clients` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
