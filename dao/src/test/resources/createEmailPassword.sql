CREATE TABLE IF NOT EXISTS `Email_password` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Account_email` VARCHAR(45) NULL,
  `password` VARCHAR(64) NULL,
  PRIMARY KEY (`id`),

  CONSTRAINT `fk_Account_email`
    FOREIGN KEY (`Account_email`)
    REFERENCES `Accounts` (`email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)