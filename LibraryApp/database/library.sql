-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 08, 2023 at 08:21 AM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 8.0.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `library`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `book_isbn` varchar(20) NOT NULL,
  `book_title` varchar(20) NOT NULL,
  `author` varchar(20) NOT NULL,
  `edition` varchar(20) NOT NULL,
  `publisher` varchar(30) NOT NULL,
  `price` float NOT NULL,
  `stock` int(11) NOT NULL,
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`book_isbn`, `book_title`, `author`, `edition`, `publisher`, `price`, `stock`, `employee_id`) VALUES
('90000001', 'Physics', 'Kiarithia', '7TH', 'Targeter', 500, 50, 1),
('90000002', 'Chemistry', 'Eliud.K', '8TH', 'Moran', 800, 38, 1),
('90000003', 'Mathematics', 'Olive.Njoroge', '4TH', 'Mzalendo', 600, 27, 1),
('90000004', 'Biology', 'Anne.Mwaura', '3RD', 'Moran', 700, 54, 1),
('90000005', 'Business', 'John.M', '10TH', 'City.Book', 400, 68, 1),
('90000006', 'Chozi.la.Heri', 'Assumpta.M', '10TH', 'Star.Shine', 700, 79, 1),
('90000007', 'Utengano', 'Said.M', '2ND', 'Tawala', 550, 86, 1),
('90000008', 'Gifted', 'Gerson', '1ST', 'City.Book', 350, 46, 1),
('90000009', 'Kigogo', 'Pauline Kea', '1ST', 'Uzindi', 400, 40, 1),
('90000010', 'Kisimani', 'Kithaka.M', '1ST', 'Marimba', 260, 59, 1),
('90000011', 'House', 'Henrik.Ibsen', '2ND', 'Moran', 550, 39, 1),
('90000012', 'The Pearl', 'John Steinbeck', '3RD', 'Targeter', 300, 31, 1),
('90000013', 'Geography', 'Dumu.Kayanda', '4TH', 'Longhorn', 650, 10, 1),
('90000014', 'Mkubwa', 'Ali.Mwalimu', '2ND', 'Moran', 300, 50, 1),
('90000015', 'History', 'Makanda.H', '7TH', 'Longhorn', 450, 54, 1),
('90000016', 'Bible', 'King James', '7TH', 'Longhorn', 900, 60, 1),
('90000017', 'Kosa', 'Mwangi.N', '3RD', 'Star.Shine', 370, 65, 1),
('90000018', 'Algorithm', 'Peterson.H', '12TH', 'Gifted.Best', 800, 67, 1),
('90000019', 'MySQL', 'Samson.Kogo', '9TH', 'Uzindi', 750, 77, 1),
('90000020', 'Tumaini', 'Ann Kogo', '1ST', 'Umoja', 600, 40, 1);

-- --------------------------------------------------------

--
-- Table structure for table `lendbook`
--

CREATE TABLE `lendbook` (
  `id` int(11) NOT NULL,
  `member_id` int(11) NOT NULL,
  `member_fname` text NOT NULL,
  `book_isbn` varchar(20) NOT NULL,
  `book_title` varchar(20) NOT NULL,
  `issuedate` date NOT NULL DEFAULT current_timestamp(),
  `returndate` date DEFAULT NULL,
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `lostbook`
--

CREATE TABLE `lostbook` (
  `id` int(11) NOT NULL,
  `member_id` int(11) NOT NULL,
  `isbn` varchar(20) NOT NULL,
  `title` varchar(20) NOT NULL,
  `price` float NOT NULL,
  `elapdays` int(11) NOT NULL,
  `fine` float NOT NULL,
  `paid` float NOT NULL,
  `balance` float NOT NULL,
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `lostbook`
--

INSERT INTO `lostbook` (`id`, `member_id`, `isbn`, `title`, `price`, `elapdays`, `fine`, `paid`, `balance`, `employee_id`) VALUES
(8, 1004, '90000003', 'Mathematics', 600, 3, 750, 1000, 250, 1),
(9, 1001, '90000019', 'MySQL', 750, 14, 1450, 1500, 50, 1),
(10, 1002, '90000009', 'Kigogo', 400, 9, 850, 900, 50, 1),
(11, 1002, '90000018', 'Algorithm', 800, 3, 950, 1000, 50, 1),
(12, 1012, '90000007', 'Utengano', 550, 2, 650, 1000, 350, 1),
(13, 1003, '90000012', 'The.Pearl', 300, 6, 600, 700, 100, 1),
(14, 1007, '90000003', 'Mathematics', 600, 8, 1000, 1100, 100, 1);

-- --------------------------------------------------------

--
-- Table structure for table `mail_server_info`
--

CREATE TABLE `mail_server_info` (
  `id` int(11) NOT NULL,
  `server_name` varchar(20) NOT NULL,
  `server_port` int(11) NOT NULL,
  `user_email` varchar(30) NOT NULL,
  `user_password` varchar(30) NOT NULL,
  `ssl_enabled` tinyint(1) NOT NULL,
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `members`
--

CREATE TABLE `members` (
  `member_id` int(11) NOT NULL,
  `fname` varchar(11) NOT NULL,
  `lname` varchar(11) NOT NULL,
  `location` varchar(20) NOT NULL,
  `mobile_number` varchar(11) NOT NULL,
  `email` varchar(40) NOT NULL,
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `members`
--

INSERT INTO `members` (`member_id`, `fname`, `lname`, `location`, `mobile_number`, `email`, `employee_id`) VALUES
(1001, 'Omondi', 'Michael', 'Busia', '0799000001', 'omondi07@gmail.com', 1),
(1002, 'Vinter', 'Ouru', 'Lamu', '0799000002', 'vinter@gmail.com', 1),
(1003, 'Florence', 'Busolo', 'Siaya', '0799000003', 'busolo@yahoo.com', 1),
(1004, 'John', 'Ntasi', 'Nyeri', '0799000004', 'john@gmail.com', 1),
(1005, 'Daniel', 'Kimutai', 'Nandi', '0799000005', 'kimutai@yahoo.com', 1),
(1006, 'Said', 'Mohamed', 'Kilifi', '0799000006', 'said100@gmail.com', 1),
(1007, 'Winnie', 'Chelagat', 'Kakamega', '0799000007', 'chelagat@yahoo.com', 1),
(1008, 'Kibet', 'Rono', 'Kericho', '0799000008', 'kibet@gmail.com', 1),
(1009, 'Abisa', 'Adhiambo', 'Siaya', '0799000009', 'abisa@gmail.com', 1),
(1010, 'Jefrida', 'Mkasi', 'Lamu', '0799000010', 'mkasi@yahoo.com', 1),
(1011, 'Zipporah', 'Chalwa', 'Kwale', '0799000011', 'chalwa@yahoo.com', 1),
(1012, 'Obiambo', 'Achieng', 'Siaya', '0799000012', 'obiambo@gmail.com', 1),
(1013, 'Kipngetich', 'Joseph', 'Narok', '0799000013', 'kipngetich07@gmail.com', 1),
(1014, 'Adelaide', 'Khayecha', 'Kakamega', '0799000014', 'adelaide@gmail.com', 1),
(1015, 'Ogola', 'Ben', 'Kisumu', '0799000015', 'ogola@yahoo.com', 1),
(1017, 'Rono', 'Peter', 'Embu', '0799000017', 'peter@gmail.com', 1),
(1018, 'Mercy', 'Kemunto', 'Kisii', '0799000018', 'mercy@gmail.com', 1),
(1020, 'Esther', 'Gakii', 'Meru', '0799000020', 'esther07@gmail.com', 1),
(1021, 'Ogola', 'Mathew', 'Nairobi', '0799000021', 'mathew@yahoo.com', 1),
(1022, 'Lionaire', 'Okoth', 'Kisumu', '0799000019', 'okoth@gmail.com', 1),
(1023, 'Mercy', 'Wanjiku', 'Nakuru', '0799000022', 'wanjiku@gmail.com', 1),
(1024, 'Gideon', 'Kimani', 'Thika', '0799000023', 'kimani@gmail.com', 1);

-- --------------------------------------------------------

--
-- Table structure for table `returnbook`
--

CREATE TABLE `returnbook` (
  `id` int(11) NOT NULL,
  `member_id` varchar(11) NOT NULL,
  `fname` varchar(18) NOT NULL,
  `title` varchar(20) NOT NULL,
  `issuedate` date DEFAULT NULL,
  `reportdate` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `elapdays` int(11) NOT NULL,
  `fine` float NOT NULL,
  `paid` float NOT NULL,
  `balance` float NOT NULL,
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `returnbook`
--

INSERT INTO `returnbook` (`id`, `member_id`, `fname`, `title`, `issuedate`, `reportdate`, `elapdays`, `fine`, `paid`, `balance`, `employee_id`) VALUES
(18, '1007', 'Winne', 'Kigogo', '2023-09-28', '2023-10-14 08:47:38', 6, 300, 1000, 700, 1),
(19, '1014', 'Adelaide', 'History', '2023-10-02', '2023-10-14 08:54:57', 2, 100, 100, 0, 1),
(20, '1002', 'Vinter', 'Biology', '2023-10-02', '2023-10-14 08:58:00', 2, 100, 500, 400, 1),
(21, '1003', 'Florence', 'Gifted.Hands', '2023-10-01', '2023-10-14 09:29:45', 3, 150, 200, 50, 1),
(22, '1011', 'Zipporah', 'Biology', '2023-09-28', '2023-10-14 16:12:59', 6, 300, 500, 200, 1),
(23, '1011', 'Zipporah', 'Utengano', '2023-09-30', '2023-10-14 17:11:23', 4, 200, 1000, 800, 1),
(24, '1011', 'Zipporah', 'Mathematics', '2023-10-04', '2023-10-14 20:04:50', 0, 0, 0, 0, 1),
(25, '1012', 'Obiambo', 'Geography', '2023-09-27', '2023-10-14 20:07:47', 7, 350, 500, 150, 1),
(26, '1004', 'John', 'Business', '2023-09-15', '2023-10-16 14:17:16', 21, 1050, 1100, 50, 1),
(27, '1001', 'Omondi', 'Physics', '2023-10-10', '2023-11-02 07:19:18', 9, 450, 500, 50, 1),
(28, '1005', 'Daniel', 'Kifo.Kisimani', '2023-10-10', '2023-11-02 07:23:16', 9, 450, 500, 50, 1);

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `supplier_id` int(11) NOT NULL,
  `supplier_name` varchar(255) NOT NULL,
  `supplier_email` varchar(255) NOT NULL,
  `supplier_mobile_number` varchar(11) NOT NULL,
  `supplier_location` varchar(255) NOT NULL,
  `employee_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `suppliers`
--

INSERT INTO `suppliers` (`supplier_id`, `supplier_name`, `supplier_email`, `supplier_mobile_number`, `supplier_location`, `employee_id`) VALUES
(15, 'Everest', 'everest2023@gmail.com', '0790000099', 'Nairobi', 1),
(16, 'Bethany', 'bethany@gmail.com', '0790000091', 'Kericho', 1),
(17, 'Prestige', 'prestiige@gmail.com', '0790000092', 'Nakuru', 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `email` varchar(30) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `email`, `user_name`, `password`) VALUES
(1, 'Kipngetich', 'Josphat', 'softwarecoding8@gmail.com', 'Admin', '12345');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`book_isbn`);

--
-- Indexes for table `lendbook`
--
ALTER TABLE `lendbook`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `lostbook`
--
ALTER TABLE `lostbook`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `mail_server_info`
--
ALTER TABLE `mail_server_info`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `members`
--
ALTER TABLE `members`
  ADD PRIMARY KEY (`member_id`);

--
-- Indexes for table `returnbook`
--
ALTER TABLE `returnbook`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`supplier_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `lendbook`
--
ALTER TABLE `lendbook`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT for table `lostbook`
--
ALTER TABLE `lostbook`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `mail_server_info`
--
ALTER TABLE `mail_server_info`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `returnbook`
--
ALTER TABLE `returnbook`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `supplier_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
