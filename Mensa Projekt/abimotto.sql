-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 21. Mai 2014 um 10:34
-- Server Version: 5.5.36
-- PHP-Version: 5.4.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `abimotto`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `motto`
--

CREATE TABLE IF NOT EXISTS `motto` (
  `MottoID` int(150) NOT NULL AUTO_INCREMENT,
  `Motto` varchar(200) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`MottoID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=12 ;

--
-- Daten für Tabelle `motto`
--

INSERT INTO `motto` (`MottoID`, `Motto`) VALUES
(1, 'Pommes mit mABI, bitte!'),
(2, 'Abiene - Abi verleiht Flügel!'),
(3, 'ABItours'),
(5, 'ABITAL - 13 Jahre Klassenkampf'),
(6, 'Abikalypse 2010 ... und nach uns die Sintflut'),
(4, 'ABI total');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `ID` int(150) NOT NULL AUTO_INCREMENT,
  `Vorname` varchar(100) CHARACTER SET utf8 NOT NULL,
  `Name` varchar(100) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=10 ;

--
-- Daten für Tabelle `user`
--

INSERT INTO `user` (`ID`, `Vorname`, `Name`) VALUES
(1, 'Axel', 'Müller'),
(2, 'Peter', 'Bond'),
(3, 'Karl', 'Kraus'),
(4, 'Anna', 'Neumann'),
(5, 'Vera', 'Heymann');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `vorschlag`
--

CREATE TABLE IF NOT EXISTS `vorschlag` (
  `ID` int(200) NOT NULL,
  `MottoID` int(200) NOT NULL,
  PRIMARY KEY (`ID`,`MottoID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `vorschlag`
--

INSERT INTO `vorschlag` (`ID`, `MottoID`) VALUES
(1, 1),
(2, 2),
(3, 3),
(3, 4),
(4, 5),
(5, 6);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
