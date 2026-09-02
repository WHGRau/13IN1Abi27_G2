-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 02. Sep 2026 um 08:32
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
(11, 10, 0, '0000-00-00 00:00:00', 2, 0, 'Aufladen'),
(12, 123, 0, '2026-09-01 18:02:54', 2, 0, 'Aufladen'),
(13, 5, 5, '2026-09-01 18:04:35', 2, 1, 'Kauf'),
(14, 1, 1, '2026-09-01 19:51:02', 2, 3, 'Kauf'),
(15, 1, 1, '2026-09-01 19:51:03', 2, 3, 'Kauf');

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
(4, 24, 1234, 25.5),
(5, 25, 4321, 28),
(6, 26, 1111, 0),
(7, 27, 2222, 5.5),
(8, 28, 5555, 42.1),
(9, 29, 9876, 12),
(10, 30, 3333, 12.3),
(14, 34, 0, 0),
(15, 35, 0, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `nutzer`
--

CREATE TABLE `nutzer` (
  `uID` int(11) NOT NULL,
  `username` text DEFAULT NULL,
  `Vorname` text NOT NULL,
  `Name` text NOT NULL,
  `Passwort` text NOT NULL,
  `Rolle` text NOT NULL,
  `Chip` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `nutzer`
--

INSERT INTO `nutzer` (`uID`, `username`, `Vorname`, `Name`, `Passwort`, `Rolle`, `Chip`) VALUES
(1, 'julcool', 'Julian', 'Kurz', '123abc', 'Admin', NULL),
(2, 'pul', 'Paul', 'Schäfer', '123', 'Schüler', '0009831976'),
(3, '2tast', 'Jan', 'Stüttger', '123', 'Mensa', NULL),
(14, 'laumül14', 'Laura', 'Müller', 'adminPass1', 'Admin', NULL),
(15, 'marsch15', 'Markus', 'Schmidt', 'adminPass2', 'Admin', NULL),
(16, 'sarweb16', 'Sarah', 'Weber', 'adminPass3', 'Admin', NULL),
(17, 'micwag17', 'Michael', 'Wagner', 'adminPass4', 'Admin', NULL),
(18, 'elefis18', 'Elena', 'Fischer', 'adminPass5', 'Admin', NULL),
(19, 'bribec19', 'Brigitte', 'Becker', 'mensaPass1', 'Mensa', NULL),
(20, 'thohof20', 'Thomas', 'Hoffmann', 'mensaPass2', 'Mensa', NULL),
(21, 'sabsch21', 'Sabine', 'Schäfer', 'mensaPass3', 'Mensa', NULL),
(22, 'klakoc22', 'Klaus', 'Koch', 'mensaPass4', 'Mensa', NULL),
(23, 'andbau23', 'Andrea', 'Bauer', 'mensaPass5', 'Mensa', NULL),
(24, 'maxric24', 'Maximilian', 'Richter', '123', 'Schüler', '0009966769'),
(25, 'sopkle25', 'Sophie', 'Klein', 'schueler123', 'Schüler', '0009968524'),
(26, 'leowol26', 'Leon', 'Wolf', 'schueler123', 'Schüler', '0001608134'),
(27, 'emmneu27', 'Emma', 'Neumann', 'schueler123', 'Schüler', '0009977009'),
(28, 'luksch28', 'Lukas', 'Schwarz', 'schueler123', 'Schüler', NULL),
(29, 'miazim29', 'Mia', 'Zimmermann', 'schueler123', 'Schüler', NULL),
(30, 'felbra30', 'Felix', 'Braun', 'schueler123', 'Schüler', NULL),
(34, 'bennet34', 'Benjamin', 'Netanyahu', 'XGAFs', 'Schüler', NULL),
(35, 'johsch35', 'John', 'Schueler', 'fyoVY', 'Schüler', NULL);

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
(1, 'Snickers', 1, 67, 0, 0),
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
  MODIFY `aID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `bestellung`
--
ALTER TABLE `bestellung`
  MODIFY `bID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT für Tabelle `konto`
--
ALTER TABLE `konto`
  MODIFY `kID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT für Tabelle `nutzer`
--
ALTER TABLE `nutzer`
  MODIFY `uID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT für Tabelle `produkte`
--
ALTER TABLE `produkte`
  MODIFY `pID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

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
