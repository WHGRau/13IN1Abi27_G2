-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 09. Sep 2026 um 09:20
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
  `Datum` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `ankunft`
--

INSERT INTO `ankunft` (`aID`, `uID`, `Datum`) VALUES
(1, 2, '2026-09-01 08:15:03');

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
  `pID` int(11) NOT NULL,
  `Typ` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `bestellung`
--

INSERT INTO `bestellung` (`bID`, `Wert`, `Menge`, `Datum`, `uID`, `pID`, `Typ`) VALUES
(3, 10, 10, '2026-09-01 09:56:15', 2, 1, 'Kauf'),
(4, 10, 5, '2026-09-01 09:57:26', 2, 2, 'Kauf'),
(5, 3, 2, '2026-09-01 10:15:00', 14, 5, 'Kauf'),
(6, 5, 2, '2026-09-01 11:30:12', 15, 11, 'Kauf'),
(7, 3, 3, '2026-09-01 12:05:45', 16, 8, 'Kauf'),
(8, 6, 5, '2026-09-01 12:40:00', 18, 3, 'Kauf'),
(9, 1, 1, '2026-09-01 13:15:20', 21, 1, 'Kauf'),
(12, 123, 0, '2026-09-01 18:02:54', 2, 0, 'Aufladen'),
(13, 5, 5, '2026-09-01 18:04:35', 2, 1, 'Kauf'),
(14, 1, 1, '2026-09-01 19:51:02', 2, 3, 'Kauf'),
(15, 1, 1, '2026-09-01 19:51:03', 2, 3, 'Kauf'),
(16, 100, 0, '2026-09-09 08:56:50', 41, 0, 'Aufladen'),
(17, 2, 1, '2026-09-09 08:57:32', 41, 1, 'Kauf'),
(18, 2, 1, '2026-09-09 08:57:32', 41, 1, 'Kauf');

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
(1, 2, 0, 257.2),
(18, 38, 0, 0),
(19, 39, 0, 0),
(20, 40, 0, 0),
(22, 42, 0, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `nutzer`
--

CREATE TABLE `nutzer` (
  `uID` int(11) NOT NULL,
  `username` text DEFAULT NULL,
  `Vorname` text NOT NULL,
  `Name` text NOT NULL,
  `Email` text NOT NULL,
  `Passwort` text NOT NULL,
  `Rolle` text NOT NULL,
  `Chip` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `nutzer`
--

INSERT INTO `nutzer` (`uID`, `username`, `Vorname`, `Name`, `Email`, `Passwort`, `Rolle`, `Chip`) VALUES
(1, 'julkur1', 'Julian', 'Kurz', '', '123abc', 'Admin', ''),
(2, 'pul', 'Paula', 'Schäf', 'joshiwinner659@gmail.com', '123', 'Schüler', '0009831976'),
(3, '2tast', 'Jan', 'Stüttger', 'kurzj062@gmail.com', '123', 'Mensa', NULL),
(38, 'benmer38', 'Ben', 'Mertschuweit', 'b.mertschuweit@gmail.com', 'l3gVK', 'Schüler', '0009968524'),
(39, 'maxang39', 'Maxi', 'Angerer', 'maxiangerer321@gmail.com', 'b6iST', 'Schüler', '0009977009'),
(40, 'johstü40', 'John', 'Stüttger', 'b.mertschuweit2@gmail.com', 'VbaoX', 'Schüler', '0009966769'),
(42, 'antsch42', 'Anton', 'Schmidt', 'deez49228@gmail.com', 'Pl7SL', 'Mensa', '0001608134');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `produkte`
--

CREATE TABLE `produkte` (
  `pID` int(11) NOT NULL,
  `Name` text NOT NULL,
  `Preis` float NOT NULL,
  `Menge` int(11) NOT NULL,
  `Sollwert` int(11) DEFAULT NULL,
  `niedrig` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `produkte`
--

INSERT INTO `produkte` (`pID`, `Name`, `Preis`, `Menge`, `Sollwert`, `niedrig`) VALUES
(1, 'Snickers', 2, 278, 0, 0),
(2, 'Mars', 2, 5, 101, 1),
(3, 'Twix', 1.2, 43, 50, 1),
(4, 'Bounty', 1.2, 60, 50, 0),
(5, 'Cola 0.5l', 1.5, 120, 100, 0),
(6, 'Fanta 0.5l', 1.5, 15, 100, 1),
(7, 'Sprite 0.5l', 1.5, 80, 80, 0),
(8, 'Mineralwasser still 0.5l', 1, 200, 150, 0),
(9, 'Mineralwasser medium 0.5l', 1, 140, 150, 1),
(10, 'Apfelschorle 0.5l', 1.8, 30, 80, 1),
(11, 'Käsebrötchen', 2.5, 12, 40, 1),
(12, 'Salami-Baguette', 3.2, 5, 25, 1),
(13, 'Schokomuffin', 1.8, 22, 20, 0),
(14, 'Blaubeermuffin', 1.8, 5, 20, 1),
(15, 'Gummibärchen', 1.5, 55, 40, 0),
(16, 'Paprika Chips', 1.6, 10, 30, 1),
(17, 'Butterbrezel', 1.5, 4, 35, 1),
(18, 'Apfel', 0.6, 45, 50, 1),
(19, 'Banane', 0.8, 60, 40, 0),
(20, 'Naturjoghurt', 1.2, 12, 20, 1),
(21, 'Eistee Pfirsich 0.5l', 1.5, 75, 60, 0),
(22, 'Kinder Bueno', 1.3, 85, 50, 0);

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
  MODIFY `aID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT für Tabelle `bestellung`
--
ALTER TABLE `bestellung`
  MODIFY `bID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT für Tabelle `konto`
--
ALTER TABLE `konto`
  MODIFY `kID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT für Tabelle `nutzer`
--
ALTER TABLE `nutzer`
  MODIFY `uID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT für Tabelle `produkte`
--
ALTER TABLE `produkte`
  MODIFY `pID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

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
