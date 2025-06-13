-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th6 13, 2025 lúc 07:38 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `btl1`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `accounts`
--

CREATE TABLE `accounts` (
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `accounts`
--

INSERT INTO `accounts` (`username`, `password`, `role`) VALUES
('a', '1', 'ChuCuaHang');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customer`
--

CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `hoten` varchar(100) NOT NULL,
  `diachi` varchar(100) NOT NULL,
  `sdt` varchar(20) NOT NULL,
  `rank` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `customer`
--

INSERT INTO `customer` (`id`, `hoten`, `diachi`, `sdt`, `rank`) VALUES
(1, '2', '4', '3', 'vip');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `invoice`
--

CREATE TABLE `invoice` (
  `id` int(11) NOT NULL,
  `type` varchar(20) DEFAULT NULL,
  `partner` varchar(100) DEFAULT NULL,
  `total` decimal(15,2) DEFAULT NULL,
  `created_date` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `invoice`
--

INSERT INTO `invoice` (`id`, `type`, `partner`, `total`, `created_date`) VALUES
(1, 'Bán', 'huy dz', 120000.00, '2025-06-12 22:04:18'),
(2, 'Bán', 'huy super dz', 80000.00, '2025-06-13 08:56:32'),
(3, 'Bán', 'dskop12', 30000.00, '2025-06-13 08:58:37'),
(4, 'Bán', '1', 30000.00, '2025-06-13 08:58:59'),
(8, 'Bán', '1', 30000.00, '2025-06-13 11:40:39'),
(9, 'Nhập', 'anh kkkk', 700000.00, '2025-06-13 11:41:05');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `invoice_detail`
--

CREATE TABLE `invoice_detail` (
  `id` int(11) NOT NULL,
  `invoice_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `invoice_detail`
--

INSERT INTO `invoice_detail` (`id`, `invoice_id`, `product_id`, `quantity`) VALUES
(1, 1, 1, 1),
(2, 1, 2, 2),
(3, 1, 3, 1),
(4, 2, 1, 1),
(5, 2, 2, 1),
(6, 2, 3, 1),
(7, 3, 1, 1),
(8, 4, 1, 1),
(9, 8, 1, 1),
(10, 9, 2, 10),
(11, 9, 1, 10),
(12, 9, 3, 10);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `purchase_price` decimal(10,2) NOT NULL,
  `selling_price` decimal(10,2) NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `quantity` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `product`
--

INSERT INTO `product` (`id`, `name`, `purchase_price`, `selling_price`, `type`, `date`, `quantity`) VALUES
(1, 'Sữa tươi Vinamilk 1L', 28000.00, 30000.00, 'Food', '2025-06-01', 100),
(2, 'Bánh Oreo hộp 10 cái', 35000.00, 40000.00, 'Food', '2025-06-05', 50),
(3, 'Nước suối Lavie 500ml', 7000.00, 10000.00, 'Food', '2025-06-10', 200),
(4, 'bút chì', 5000.00, 8000.00, 'Learning', '2025-06-01', 100),
(5, 'Bóng đèn', 15000.00, 20000.00, 'Electronic', '2025-06-10', 100);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`username`);

--
-- Chỉ mục cho bảng `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `invoice`
--
ALTER TABLE `invoice`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `invoice_detail`
--
ALTER TABLE `invoice_detail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `invoice_id` (`invoice_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Chỉ mục cho bảng `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `invoice`
--
ALTER TABLE `invoice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `invoice_detail`
--
ALTER TABLE `invoice_detail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `invoice_detail`
--
ALTER TABLE `invoice_detail`
  ADD CONSTRAINT `invoice_detail_ibfk_1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `invoice_detail_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
