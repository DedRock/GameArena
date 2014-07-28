CREATE TABLE `accounts` (
  `nickName` varchar(20) NOT NULL,
  `Name` varchar(45) NOT NULL,
  `LastName` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `lastEnterTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`nickName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `chat` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sender` varchar(20) NOT NULL,
  `text` varchar(45) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `sender` (`sender`),
  CONSTRAINT `sender` FOREIGN KEY (`sender`) REFERENCES `accounts` (`nickName`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
CREATE TABLE `games` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `player_1` varchar(20) NOT NULL,
  `player_2` varchar(20) DEFAULT NULL,
  `gameEnd` tinyint(1) NOT NULL DEFAULT '0',
  `winPlayer` varchar(20) DEFAULT NULL,
  `gameType` varchar(20) DEFAULT 'XO',
  `gameTerminated` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8;
CREATE TABLE `gamestate` (
  `id` int(10) unsigned NOT NULL DEFAULT '0',
  `curPlayer` varchar(20) DEFAULT NULL,
  `State_00` varchar(20) NOT NULL DEFAULT 'Empty',
  `State_01` varchar(20) NOT NULL DEFAULT 'Empty',
  `State_10` varchar(20) NOT NULL DEFAULT 'Empty',
  `State_11` varchar(20) NOT NULL DEFAULT 'Empty',
  `State_12` varchar(20) NOT NULL DEFAULT 'Empty',
  `State_20` varchar(20) NOT NULL DEFAULT 'Empty',
  `State_21` varchar(20) NOT NULL DEFAULT 'Empty',
  `State_22` varchar(20) NOT NULL DEFAULT 'Empty',
  `State_02` varchar(20) NOT NULL DEFAULT 'Empty',
  PRIMARY KEY (`id`),
  CONSTRAINT `id` FOREIGN KEY (`id`) REFERENCES `games` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;