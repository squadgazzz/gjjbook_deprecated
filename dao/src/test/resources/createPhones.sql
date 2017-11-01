CREATE TABLE IF NOT EXISTS `Phones` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Account_id` INT NOT NULL,
  `type` CHAR(6) NOT NULL,
  `number` CHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Phones_Accounts`
    FOREIGN KEY (`Account_id`)
    REFERENCES `Accounts` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)