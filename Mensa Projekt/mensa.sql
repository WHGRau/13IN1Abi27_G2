-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 01. Sep 2026 um 11:19
-- Server-Version: 10.4.28-MariaDB
-- PHP-Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `mensa`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ankunft`
--

CREATE TABLE `ankunft` (
  `aID` int(11) NOT NULL,
  `uID` int(11) NOT NULL,
  `Datum` date NOT NULL,
  `Uhrzeit` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `bestellung`
--

CREATE TABLE `bestellung` (
  `bID` int(11) NOT NULL,
  `Wert` int(11) NOT NULL,
  `Menge` int(11) NOT NULL,
  `Datum` datetime NOT NULL DEFAULT current_timestamp(),
  `uID` int(11) NOT NULL,
  `pID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `bestellung`
--

INSERT INTO `bestellung` (`bID`, `Wert`, `Menge`, `Datum`, `uID`, `pID`) VALUES
(3, 10, 10, '2026-09-01 09:56:15', 2, 1),
(4, 10, 5, '2026-09-01 09:57:26', 2, 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `konto`
--

CREATE TABLE `konto` (
  `kID` int(11) NOT NULL,
  `uID` int(11) NOT NULL,
  `Pin` int(11) NOT NULL,
  `Kontostand` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `konto`
--

INSERT INTO `konto` (`kID`, `uID`, `Pin`, `Kontostand`) VALUES
(1, 2, 0, 1000);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `nutzer`
--

CREATE TABLE `nutzer` (
  `uID` int(11) NOT NULL,
  `Vorname` text NOT NULL,
  `Name` text NOT NULL,
  `Passwort` text NOT NULL,
  `Rolle` text NOT NULL,
  `Chip` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `nutzer`
--

INSERT INTO `nutzer` (`uID`, `Vorname`, `Name`, `Passwort`, `Rolle`, `Chip`) VALUES
(1, 'Julian', 'Kurz', '123abc', 'Admin', '0'),
(2, 'Paul', 'Schäfer', '123', 'Schüler', '0009831976'),
(3, 'Jan', 'Stüttger', '123', 'Mensa', '0');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `produkte`
--

CREATE TABLE `produkte` (
  `pID` int(11) NOT NULL,
  `Name` text NOT NULL,
  `Preis` float NOT NULL,
  `Menge` int(11) NOT NULL,
  `Sollwert` int(11) NOT NULL,
  `niedrig` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `produkte`
--

INSERT INTO `produkte` (`pID`, `Name`, `Preis`, `Menge`, `Sollwert`, `niedrig`) VALUES
(1, 'Snickers', 1, 75, 0, 0),
(2, 'Mars', 2, 5, 101, 1);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `ankunft`
--
ALTER TABLE `ankunft`
  ADD PRIMARY KEY (`aID`),
  ADD KEY `uID` (`uID`);

--
-- Indizes für die Tabelle `bestellung`
--
ALTER TABLE `bestellung`
  ADD PRIMARY KEY (`bID`);

--
-- Indizes für die Tabelle `konto`
--
ALTER TABLE `konto`
  ADD PRIMARY KEY (`kID`),
  ADD KEY `uID` (`uID`);

--
-- Indizes für die Tabelle `nutzer`
--
ALTER TABLE `nutzer`
  ADD PRIMARY KEY (`uID`);

--
-- Indizes für die Tabelle `produkte`
--
ALTER TABLE `produkte`
  ADD PRIMARY KEY (`pID`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `ankunft`
--
ALTER TABLE `ankunft`
  MODIFY `aID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `bestellung`
--
ALTER TABLE `bestellung`
  MODIFY `bID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT für Tabelle `konto`
--
ALTER TABLE `konto`
  MODIFY `kID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT für Tabelle `nutzer`
--
ALTER TABLE `nutzer`
  MODIFY `uID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT für Tabelle `produkte`
--
ALTER TABLE `produkte`
  MODIFY `pID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `ankunft`
--
ALTER TABLE `ankunft`
  ADD CONSTRAINT `ankunft_ibfk_1` FOREIGN KEY (`uID`) REFERENCES `nutzer` (`uID`);

--
-- Constraints der Tabelle `konto`
--
ALTER TABLE `konto`
  ADD CONSTRAINT `konto_ibfk_1` FOREIGN KEY (`uID`) REFERENCES `nutzer` (`uID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
