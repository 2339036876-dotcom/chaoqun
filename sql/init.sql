-- ============================================================
-- computer_mall 数据库初始化脚本
-- ============================================================
DROP DATABASE IF EXISTS `computer_mall`;
CREATE DATABASE IF NOT EXISTS `computer_mall` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `computer_mall`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------
-- 1. user
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `state` SMALLINT DEFAULT 1 COMMENT '账户状态:1可用|2异常|3已冻结|4已注销',
  `user_group` INT COMMENT '所在用户组',
  `login_time` TIMESTAMP NULL COMMENT '上次登录时间',
  `phone` VARCHAR(11),
  `phone_state` SMALLINT DEFAULT 0 COMMENT '手机认证:0未认证|1审核中|2已认证',
  `username` VARCHAR(16),
  `nickname` VARCHAR(16),
  `password` VARCHAR(64),
  `email` VARCHAR(64),
  `email_state` SMALLINT DEFAULT 0 COMMENT '邮箱认证:0未认证|1审核中|2已认证',
  `avatar` VARCHAR(255),
  `open_id` VARCHAR(255),
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 2. user_group
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `group_id` INT NOT NULL AUTO_INCREMENT,
  `display` SMALLINT DEFAULT 0,
  `name` VARCHAR(16),
  `description` VARCHAR(255),
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 3. auth
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `auth`;
CREATE TABLE `auth` (
  `auth_id` INT NOT NULL AUTO_INCREMENT,
  `user_group` INT,
  `mod_name` VARCHAR(64),
  `table_name` VARCHAR(64),
  `page_title` VARCHAR(255),
  `path` VARCHAR(255),
  `parent` VARCHAR(64),
  `parent_sort` INT DEFAULT 0,
  `position` VARCHAR(32),
  `mode` VARCHAR(32),
  `add` TINYINT DEFAULT 0,
  `del` TINYINT DEFAULT 0,
  `set` TINYINT DEFAULT 0,
  `get` TINYINT DEFAULT 0,
  `field_add` TEXT,
  `field_set` TEXT,
  `field_get` TEXT,
  `table_nav_name` VARCHAR(255),
  `table_nav` VARCHAR(255),
  `option` TEXT,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`auth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 4. access_token
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `access_token`;
CREATE TABLE `access_token` (
  `token_id` INT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(255),
  `info` TEXT,
  `maxage` INT DEFAULT 7200,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 5. merchant_users
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `merchant_users`;
CREATE TABLE `merchant_users` (
  `merchant_users_id` INT NOT NULL AUTO_INCREMENT,
  `merchant_name` VARCHAR(64),
  `merchant_phone_number` VARCHAR(16),
  `store_name` VARCHAR(64),
  `qualification_certificate` VARCHAR(255),
  `examine_state` VARCHAR(16) DEFAULT '未审核',
  `user_id` INT NOT NULL,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`merchant_users_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 6. merchant_customer_service
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `merchant_customer_service`;
CREATE TABLE `merchant_customer_service` (
  `merchant_customer_service_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT COMMENT '咨询用户',
  `goods_id` INT,
  `goods_title` VARCHAR(125),
  `session_id` VARCHAR(64),
  `merchant_users` INT,
  `sender_type` VARCHAR(16),
  `message_content` TEXT,
  `read_state` VARCHAR(16) DEFAULT '未读',
  `consultation_title` VARCHAR(64),
  `consultation_content` TEXT,
  `merchant_response` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`merchant_customer_service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 7. goods_type
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `goods_type`;
CREATE TABLE `goods_type` (
  `type_id` INT NOT NULL AUTO_INCREMENT,
  `father_id` SMALLINT DEFAULT 0,
  `name` VARCHAR(255),
  `desc` VARCHAR(255),
  `icon` VARCHAR(255),
  `source_table` VARCHAR(255),
  `source_field` VARCHAR(255),
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 8. goods
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `goods_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(125),
  `img` VARCHAR(255),
  `description` VARCHAR(255),
  `price_ago` DOUBLE DEFAULT 0,
  `price` DOUBLE DEFAULT 0,
  `sales` INT DEFAULT 0,
  `inventory` INT DEFAULT 0,
  `type` INT COMMENT '商品分类id, FK to goods_type',
  `hits` INT DEFAULT 0,
  `content` LONGTEXT,
  `img_1` VARCHAR(255),
  `img_2` VARCHAR(255),
  `img_3` VARCHAR(255),
  `img_4` VARCHAR(255),
  `img_5` VARCHAR(255),
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `customize_field` VARCHAR(255),
  `source_table` VARCHAR(255),
  `source_field` VARCHAR(255),
  `source_id` INT DEFAULT 0,
  `user_id` INT COMMENT '添加人',
  PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 9. cart
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `cart_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `num` INT DEFAULT 1,
  `goods_id` INT,
  PRIMARY KEY (`cart_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 10. order (reserved word, use backticks)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `order_id` INT NOT NULL AUTO_INCREMENT,
  `order_number` VARCHAR(64),
  `goods_id` INT,
  `price_ago` DOUBLE DEFAULT 0,
  `num` INT DEFAULT 1,
  `price_count` DOUBLE DEFAULT 0,
  `contact_name` VARCHAR(32),
  `contact_phone` VARCHAR(11),
  `contact_address` VARCHAR(255),
  `user_id` INT,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `description` VARCHAR(255),
  `state` VARCHAR(16) DEFAULT '待付款',
  `remark` TEXT,
  `delivery_state` VARCHAR(16) DEFAULT '未配送',
  `vip_discount` DOUBLE DEFAULT 1.0,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 11. order_after_sale
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `order_after_sale`;
CREATE TABLE `order_after_sale` (
  `order_after_sale_id` INT NOT NULL AUTO_INCREMENT,
  `order_id` INT,
  `after_state` VARCHAR(16) DEFAULT '未审核',
  `after_state_reply` VARCHAR(255),
  `type` VARCHAR(255),
  `content_desc` VARCHAR(255),
  `imgs` VARCHAR(255),
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_after_sale_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 12. article_type
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `article_type`;
CREATE TABLE `article_type` (
  `type_id` INT NOT NULL AUTO_INCREMENT,
  `display` INT DEFAULT 0,
  `name` VARCHAR(16),
  `father_id` INT DEFAULT 0,
  `description` VARCHAR(255),
  `icon` TEXT,
  `url` VARCHAR(255),
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 13. article
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `article_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(125),
  `type` INT,
  `hits` INT DEFAULT 0,
  `praise_len` INT DEFAULT 0,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source` VARCHAR(255),
  `url` VARCHAR(255),
  `content` LONGTEXT,
  `img` VARCHAR(255),
  `description` TEXT,
  PRIMARY KEY (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 14. comment
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `comment_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT,
  `reply_to_id` INT DEFAULT 0,
  `content` LONGTEXT,
  `reply` LONGTEXT,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source_table` VARCHAR(255),
  `source_field` VARCHAR(255),
  `source_id` INT,
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 15. collect
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `collect_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT,
  `source_table` VARCHAR(255),
  `source_field` VARCHAR(255),
  `source_id` INT,
  `title` VARCHAR(255),
  `img` VARCHAR(255),
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`collect_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 16. notice_announcement
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `notice_announcement`;
CREATE TABLE `notice_announcement` (
  `notice_announcement_id` INT NOT NULL AUTO_INCREMENT,
  `merchant_users` INT,
  `notification_title` VARCHAR(64),
  `notification_time` DATE,
  `notification_content` TEXT,
  `hits` INT DEFAULT 0,
  `praise_len` INT DEFAULT 0,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`notice_announcement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 17. score
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
  `score_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `nickname` VARCHAR(64),
  `score_num` DOUBLE DEFAULT 0,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source_table` VARCHAR(255),
  `source_field` VARCHAR(255),
  `source_id` INT DEFAULT 0,
  PRIMARY KEY (`score_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 18. forum
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `forum`;
CREATE TABLE `forum` (
  `forum_id` INT NOT NULL AUTO_INCREMENT,
  `display` SMALLINT DEFAULT 0,
  `user_id` INT NOT NULL,
  `nickname` VARCHAR(16),
  `praise_len` INT DEFAULT 0,
  `hits` INT DEFAULT 0,
  `title` VARCHAR(125),
  `keywords` VARCHAR(125),
  `description` VARCHAR(255),
  `url` VARCHAR(255),
  `tag` VARCHAR(255),
  `img` TEXT,
  `content` LONGTEXT,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `avatar` VARCHAR(255),
  `type` VARCHAR(64),
  `istop` INT DEFAULT 0,
  PRIMARY KEY (`forum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 19. forum_type
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `forum_type`;
CREATE TABLE `forum_type` (
  `type_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(16),
  `description` VARCHAR(255),
  `url` VARCHAR(255),
  `father_id` SMALLINT DEFAULT 0,
  `icon` VARCHAR(255),
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 20. hits
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `hits`;
CREATE TABLE `hits` (
  `hits_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source_table` VARCHAR(255),
  `source_field` VARCHAR(255),
  `source_id` INT DEFAULT 0,
  PRIMARY KEY (`hits_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 21. praise
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `praise`;
CREATE TABLE `praise` (
  `praise_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source_table` VARCHAR(255),
  `source_field` VARCHAR(255),
  `source_id` INT DEFAULT 0,
  `status` BIT(1) DEFAULT 1,
  PRIMARY KEY (`praise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 22. accessories_recycling
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `accessories_recycling`;
CREATE TABLE `accessories_recycling` (
  `accessories_recycling_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT COMMENT '提交用户',
  `user_name` VARCHAR(64),
  `user_phone_number` VARCHAR(64),
  `accessory_name` VARCHAR(64),
  `accessory_type` VARCHAR(64),
  `usage_time` VARCHAR(64),
  `recycling_address` VARCHAR(64),
  `purchase_price` DOUBLE,
  `accessory_pictures` TEXT,
  `accessory_content` TEXT,
  `accessories_details` LONGTEXT,
  `examine_state` VARCHAR(16) DEFAULT '待报价',
  `examine_reply` VARCHAR(255),
  `quote_price` DOUBLE,
  `quote_remark` VARCHAR(255),
  `logistics_number` VARCHAR(64),
  `logistics_company` VARCHAR(64),
  `receiver_name` VARCHAR(64),
  `receiver_phone` VARCHAR(32),
  `receiver_address` VARCHAR(255),
  `received_time` DATETIME,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`accessories_recycling_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 23. address
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `address_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32),
  `phone` VARCHAR(13),
  `postcode` VARCHAR(8),
  `address` VARCHAR(255) NOT NULL,
  `user_id` INT NOT NULL,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `default` BIT(1) DEFAULT 0,
  PRIMARY KEY (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 24. slides
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `slides`;
CREATE TABLE `slides` (
  `slides_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(64),
  `content` VARCHAR(255),
  `url` VARCHAR(255),
  `img` VARCHAR(255),
  `hits` INT DEFAULT 0,
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`slides_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 25. upload
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `upload`;
CREATE TABLE `upload` (
  `upload_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64),
  `path` VARCHAR(255),
  `file` VARCHAR(255),
  `display` VARCHAR(255),
  `father_id` INT DEFAULT 0,
  `dir` VARCHAR(255),
  `type` VARCHAR(32),
  PRIMARY KEY (`upload_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 26. schedule
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule` (
  `schedule_id` INT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(255),
  `scheduled_time` DATETIME,
  `user_id` INT NOT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- 27. logistics_delivery
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `logistics_delivery`;
CREATE TABLE `logistics_delivery` (
  `logistics_delivery_id` INT NOT NULL AUTO_INCREMENT,
  `order_number` VARCHAR(64),
  `product_name` VARCHAR(64),
  `purchase_quantity` VARCHAR(64),
  `total_transaction_amount` DOUBLE,
  `the_date_of_issuance` DATE,
  `delivery_number` VARCHAR(30),
  `ordinary_users` INT,
  `shipping_address` VARCHAR(64),
  `delivery_status` VARCHAR(64) DEFAULT '未配送',
  `signing_status` VARCHAR(64) DEFAULT '未签收',
  `recommend` INT DEFAULT 0,
  `contact_name` VARCHAR(255),
  `merchant_id` INT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`logistics_delivery_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 初始数据
-- ============================================================

-- 用户组
INSERT INTO `user_group` (`group_id`, `name`, `description`) VALUES
(1, 'admin', '管理员'),
(2, 'user', '普通用户');

-- 管理员用户
INSERT INTO `user` (`username`, `password`, `user_group`, `nickname`) VALUES
('admin', '123456', 1, '管理员');

-- 商品分类
INSERT INTO `goods_type` (`type_id`, `name`) VALUES
(1, 'CPU'),
(2, '显卡'),
(3, '内存'),
(4, '硬盘'),
(5, '主板'),
(6, '电源'),
(7, '机箱'),
(8, '散热器'),
(9, '显示器'),
(10, '键鼠外设');

-- 轮播图
INSERT INTO `slides` (`title`, `content`, `url`, `img`) VALUES
('新品首发', '最新电脑配件火热上架', '/goods', '/static/img/slides/slide1.jpg'),
('限时优惠', '全场满减活动进行中', '/goods', '/static/img/slides/slide2.jpg'),
('品牌专区', '各大品牌旗舰产品汇总', '/goods', '/static/img/slides/slide3.jpg');

-- 示例商品
INSERT INTO `goods` (`title`, `img`, `description`, `price_ago`, `price`, `inventory`, `type`, `content`, `user_id`) VALUES
('Intel Core i7-14700K 处理器', '/static/img/goods/cpu1.jpg', '20核28线程 睿频至高可达5.6GHz 支持DDR5', 3299.00, 2899.00, 200, 1, '<p>Intel 第14代酷睿 i7-14700K 台式机处理器，采用混合架构设计。</p>', 1),
('NVIDIA GeForce RTX 4070 显卡', '/static/img/goods/gpu1.jpg', '12GB GDDR6X 高性能光追游戏显卡', 5499.00, 4799.00, 150, 2, '<p>NVIDIA RTX 4070 显卡，支持 DLSS 3 与光线追踪。</p>', 1),
('芝奇 幻锋戟 DDR5 32GB 套装', '/static/img/goods/ram1.jpg', 'DDR5 6000MHz C30 双通道内存套装', 899.00, 749.00, 300, 3, '<p>芝奇 Trident Z5 Neo DDR5 高频内存，兼容主流平台。</p>', 1),
('三星 990 PRO 2TB 固态硬盘', '/static/img/goods/ssd1.jpg', 'NVMe M.2 PCIe 4.0 读速7450MB/s', 1299.00, 1099.00, 250, 4, '<p>三星 990 PRO 旗舰级 SSD，持续稳定高速读写。</p>', 1),
('华硕 ROG STRIX Z790-E 主板', '/static/img/goods/mb1.jpg', 'ATX主板 支持13/14代Intel处理器 DDR5', 3699.00, 3299.00, 100, 5, '<p>华硕 ROG STRIX Z790-E GAMING WIFI II 电竞主板。</p>', 1);

SET FOREIGN_KEY_CHECKS = 1;
ALTER TABLE goods ADD COLUMN img1 VARCHAR(255);
ALTER TABLE goods ADD COLUMN img2 VARCHAR(255);
ALTER TABLE goods ADD COLUMN img3 VARCHAR(255);
ALTER TABLE goods ADD COLUMN img4 VARCHAR(255);
ALTER TABLE goods ADD COLUMN img5 VARCHAR(255);