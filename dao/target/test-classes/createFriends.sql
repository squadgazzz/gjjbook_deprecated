CREATE TABLE IF NOT EXISTS `Friends` (
  `Account_id` INT NOT NULL,
  `Friend_id` INT NOT NULL,
  PRIMARY KEY (`Account_id`, `Friend_id`),
    FOREIGN KEY (`Account_id`)
    REFERENCES `Accounts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (`Friend_id`)
    REFERENCES `Accounts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)