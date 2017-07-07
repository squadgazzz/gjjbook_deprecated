CREATE TABLE IF NOT EXISTS `Phones` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Accounts_id` INT NOT NULL,
  `type` VARCHAR(6) NOT NULL,
  `countrycode` INT(5) NOT NULL,
  `number` BIGINT(25) NOT NULL,
  `Phonescol` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Phones_Accounts`
    FOREIGN KEY (`Accounts_id`)
    REFERENCES `Accounts` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
