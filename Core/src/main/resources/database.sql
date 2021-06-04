CREATE DATABASE IF NOT EXISTS middle DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

CREATE USER 'middleWareAdmin'@'localhost' IDENTIFIED BY '123456';
CREATE USER 'middleWareAdmin'@'%' IDENTIFIED BY '123456';

GRANT ALL ON middle.* TO 'middleWareAdmin'@'localhost';
GRANT ALL ON middle.* TO 'middleWareAdmin'@'%';

FLUSH PRIVILEGES;