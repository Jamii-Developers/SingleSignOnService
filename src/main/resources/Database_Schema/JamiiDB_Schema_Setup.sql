CREATE SCHEMA `jamiidb` ;

CREATE TABLE IF NOT EXISTS `jamiidb`.`user_login_information` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `email_address` VARCHAR(100) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `password_salt` LONGTEXT NOT NULL,
  `active` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `email_address_1_UNIQUE` (`email_address` ASC) VISIBLE );