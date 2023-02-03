CREATE TABLE IF NOT EXISTS `school`.`student_info` (
  `row_id` INT NOT NULL AUTO_INCREMENT,
  `identification_card` BIGINT(10) NOT NULL,
  `first_name` VARCHAR(45) NULL,
  `last_name` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `gender` VARCHAR(45) NULL,
  `ip_address` VARCHAR(45) NULL,
  `cm_height` INT NULL,
  `age` INT NULL,
  `grade` INT NULL,
  `grade_avg` FLOAT NULL,
  PRIMARY KEY (`row_id`, `identification_card`));

CREATE TABLE IF NOT EXISTS `school`.`car_details` (
  `identification_card` BIGINT(10) NOT NULL,
  `car_color` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`identification_card`));
  
  CREATE TABLE IF NOT EXISTS `school`.`friends` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `row_id_student` INT NOT NULL,
  `row_id_friend` INT NOT NULL,
  PRIMARY KEY (`id`));
  
 CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `root`@`localhost` 
    SQL SECURITY DEFINER
VIEW `stud_view` AS
    SELECT 
        `student_info`.`identification_card` AS `identification_card`,
        `student_info`.`first_name` AS `first_name`,
        `student_info`.`last_name` AS `last_name`,
        `student_info`.`grade_avg` AS `grade_avg`
    FROM
        `student_info`;
		
		


