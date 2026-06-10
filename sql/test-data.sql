USE `computer_mall`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 兼容当前代码中的 goods.img1 ~ goods.img5
SET @sql = IF(
  EXISTS(
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'goods' AND COLUMN_NAME = 'img1'
  ),
  'SELECT 1',
  'ALTER TABLE `goods` ADD COLUMN `img1` VARCHAR(255)'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF(
  EXISTS(
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'goods' AND COLUMN_NAME = 'img2'
  ),
  'SELECT 1',
  'ALTER TABLE `goods` ADD COLUMN `img2` VARCHAR(255)'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF(
  EXISTS(
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'goods' AND COLUMN_NAME = 'img3'
  ),
  'SELECT 1',
  'ALTER TABLE `goods` ADD COLUMN `img3` VARCHAR(255)'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF(
  EXISTS(
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'goods' AND COLUMN_NAME = 'img4'
  ),
  'SELECT 1',
  'ALTER TABLE `goods` ADD COLUMN `img4` VARCHAR(255)'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF(
  EXISTS(
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'goods' AND COLUMN_NAME = 'img5'
  ),
  'SELECT 1',
  'ALTER TABLE `goods` ADD COLUMN `img5` VARCHAR(255)'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 清空测试数据
DELETE FROM `logistics_delivery`;
DELETE FROM `order_after_sale`;
DELETE FROM `order`;
DELETE FROM `cart`;
DELETE FROM `collect`;
DELETE FROM `comment`;
DELETE FROM `score`;
DELETE FROM `address`;
DELETE FROM `article`;
DELETE FROM `article_type`;
DELETE FROM `slides`;
DELETE FROM `goods`;
DELETE FROM `goods_type`;
DELETE FROM `notice_announcement`;
DELETE FROM `forum`;
DELETE FROM `forum_type`;
DELETE FROM `merchant_customer_service`;
DELETE FROM `merchant_users`;
DELETE FROM `schedule`;
DELETE FROM `user`;
DELETE FROM `user_group`;

ALTER TABLE `user_group` AUTO_INCREMENT = 1;
ALTER TABLE `user` AUTO_INCREMENT = 1;
ALTER TABLE `goods_type` AUTO_INCREMENT = 1;
ALTER TABLE `goods` AUTO_INCREMENT = 1;
ALTER TABLE `slides` AUTO_INCREMENT = 1;
ALTER TABLE `article_type` AUTO_INCREMENT = 1;
ALTER TABLE `article` AUTO_INCREMENT = 1;
ALTER TABLE `comment` AUTO_INCREMENT = 1;
ALTER TABLE `collect` AUTO_INCREMENT = 1;
ALTER TABLE `score` AUTO_INCREMENT = 1;
ALTER TABLE `address` AUTO_INCREMENT = 1;
ALTER TABLE `order` AUTO_INCREMENT = 1;
ALTER TABLE `order_after_sale` AUTO_INCREMENT = 1;
ALTER TABLE `logistics_delivery` AUTO_INCREMENT = 1;
ALTER TABLE `notice_announcement` AUTO_INCREMENT = 1;

-- 用户组
INSERT INTO `user_group` (`group_id`, `display`, `name`, `description`) VALUES
(1, 1, 'admin', '管理员'),
(2, 1, 'user', '普通用户');

-- 用户
INSERT INTO `user`
(`user_id`, `state`, `user_group`, `phone`, `phone_state`, `username`, `nickname`, `password`, `email`, `email_state`, `avatar`)
VALUES
(1, 1, 1, '13800000000', 2, 'admin', '系统管理员', '123456', 'admin@test.com', 2, '/uploads/admin-avatar.jpg'),
(2, 1, 2, '13900000001', 2, 'zhangsan', '张三', '123456', 'zhangsan@test.com', 2, '/uploads/user-avatar-1.jpg'),
(3, 1, 2, '13900000002', 2, 'lisi', '李四', '123456', 'lisi@test.com', 2, '/uploads/user-avatar-2.jpg');

-- 商品分类
INSERT INTO `goods_type` (`type_id`, `father_id`, `name`, `desc`) VALUES
(1, 0, 'CPU', '中央处理器'),
(2, 0, '显卡', '独立显卡'),
(3, 0, '内存', 'DDR 内存'),
(4, 0, '固态硬盘', 'SSD 存储'),
(5, 0, '主板', '电脑主板'),
(6, 0, '电源', '电源供应器'),
(7, 0, '机箱', '台式机机箱'),
(8, 0, '散热器', '风冷与水冷'),
(9, 0, '显示器', '显示设备'),
(10, 0, '外设', '键鼠耳机');

-- 轮播图
INSERT INTO `slides` (`slides_id`, `title`, `content`, `url`, `img`, `hits`) VALUES
(1, '新品上架', '高性能装机配件到货', '/goods', '/uploads/slide-1.jpg', 12),
(2, '限时促销', '热门商品限时直降', '/goods', '/uploads/slide-2.jpg', 9),
(3, '装机推荐', '精选高性价比电脑配件', '/goods', '/uploads/slide-3.jpg', 7);

-- 文章分类
INSERT INTO `article_type` (`type_id`, `display`, `name`, `description`) VALUES
(1, 1, '装机指南', '装机相关文章'),
(2, 1, '硬件评测', '硬件评测相关文章'),
(3, 1, '选购建议', '配件选购建议');

-- 文章
INSERT INTO `article`
(`article_id`, `title`, `type`, `hits`, `praise_len`, `source`, `url`, `content`, `img`, `description`)
VALUES
(1, '2026 装机配置推荐', 1, 18, 3, '平台原创', '/article/1', '<p>这是一篇用于测试的装机推荐文章。</p>', '/uploads/article-1.jpg', '适合入门和进阶用户的配置推荐'),
(2, 'RTX 4070 显卡选购建议', 2, 15, 2, '平台原创', '/article/2', '<p>这是一篇用于测试的显卡评测文章。</p>', '/uploads/article-2.jpg', '显卡性能与性价比分析');

-- 公告
INSERT INTO `notice_announcement`
(`notice_announcement_id`, `merchant_users`, `notification_title`, `notification_time`, `notification_content`, `hits`, `praise_len`)
VALUES
(1, 0, '系统维护通知', '2026-03-18', '系统将于今晚 23:00 进行例行维护。', 6, 0),
(2, 0, '新品活动公告', '2026-03-18', '本周新上架多款 CPU、显卡和 SSD。', 10, 1);

-- 商品
INSERT INTO `goods`
(`goods_id`, `title`, `img`, `description`, `price_ago`, `price`, `sales`, `inventory`, `type`, `hits`, `content`,
 `img1`, `img2`, `img3`, `img4`, `img5`, `customize_field`, `user_id`)
VALUES
(1, 'Intel Core i7-14700K', '/uploads/goods-cpu-main.jpg', '20 核高性能处理器，适合游戏与生产力场景', 3299.00, 2899.00, 56, 120, 1, 88,
 '<p>支持高频 DDR5，适合中高端装机。</p>',
 '/uploads/goods-cpu-1.jpg', '/uploads/goods-cpu-2.jpg', '/uploads/goods-cpu-3.jpg', '', '', '盒装', 1),

(2, 'NVIDIA RTX 4070 12G', '/uploads/goods-gpu-main.jpg', '热门甜品级显卡，支持光追与 DLSS', 4999.00, 4399.00, 34, 80, 2, 66,
 '<p>适合 2K 游戏与轻度 AI 创作。</p>',
 '/uploads/goods-gpu-1.jpg', '/uploads/goods-gpu-2.jpg', '', '', '', '三风扇', 1),

(3, 'DDR5 32GB 6000MHz 内存套装', '/uploads/goods-ram-main.jpg', '双通道高频内存，兼顾性能与稳定', 999.00, 799.00, 73, 200, 3, 51,
 '<p>适合搭配新平台主板使用。</p>',
 '/uploads/goods-ram-1.jpg', '', '', '', '', 'RGB 灯效', 1),

(4, 'Samsung 990 PRO 2TB', '/uploads/goods-ssd-main.jpg', 'PCIe 4.0 NVMe SSD，速度快容量大', 1399.00, 1199.00, 48, 150, 4, 59,
 '<p>适合系统盘和游戏盘。</p>',
 '/uploads/goods-ssd-1.jpg', '/uploads/goods-ssd-2.jpg', '', '', '', 'M.2', 1),

(5, 'ROG STRIX Z790-E 主板', '/uploads/goods-mb-main.jpg', '高端主板，供电和扩展能力出色', 3899.00, 3399.00, 19, 60, 5, 41,
 '<p>适合高性能 Intel 平台整机。</p>',
 '/uploads/goods-mb-1.jpg', '', '', '', '', 'ATX', 1);

-- 收货地址
INSERT INTO `address`
(`address_id`, `name`, `phone`, `postcode`, `address`, `user_id`, `default`)
VALUES
(1, '张三', '13900000001', '200000', '上海市浦东新区测试路 100 号', 2, b'1'),
(2, '李四', '13900000002', '100000', '北京市海淀区示例路 66 号', 3, b'1');

-- 购物车
INSERT INTO `cart` (`cart_id`, `user_id`, `num`, `goods_id`) VALUES
(1, 2, 1, 1),
(2, 2, 2, 4),
(3, 3, 1, 2);

-- 收藏
INSERT INTO `collect` (`collect_id`, `user_id`, `source_table`, `source_id`, `title`, `img`) VALUES
(1, 2, 'goods', 1, 'Intel Core i7-14700K', '/uploads/goods-cpu-main.jpg'),
(2, 2, 'goods', 2, 'NVIDIA RTX 4070 12G', '/uploads/goods-gpu-main.jpg');

-- 评论
INSERT INTO `comment` (`comment_id`, `user_id`, `content`, `reply`, `source_table`, `source_id`) VALUES
(1, 2, '商品性能很强，包装也不错。', '感谢支持，后续有问题可随时咨询。', 'goods', 1),
(2, 3, '显卡温度控制还可以，2K 游戏很稳。', NULL, 'goods', 2),
(3, 2, '这篇文章写得很实用。', NULL, 'article', 1);

-- 评分
INSERT INTO `score` (`score_id`, `user_id`, `nickname`, `score_num`, `source_table`, `source_id`) VALUES
(1, 2, '张三', 4.5, 'goods', 1),
(2, 3, '李四', 5.0, 'goods', 1),
(3, 2, '张三', 4.0, 'goods', 2);

-- 订单
INSERT INTO `order`
(`order_id`, `order_number`, `goods_id`, `price_ago`, `num`, `price_count`, `contact_name`, `contact_phone`, `contact_address`,
 `user_id`, `description`, `state`, `remark`, `delivery_state`, `vip_discount`)
VALUES
(1, 'ORD202603180001', 1, 3299.00, 1, 2899.00, '张三', '13900000001', '上海市浦东新区测试路 100 号', 2, '下单测试', '待发货', '请尽快发货', '未发货', 1.0),
(2, 'ORD202603180002', 2, 4999.00, 1, 4399.00, '李四', '13900000002', '北京市海淀区示例路 66 号', 3, '下单测试', '已完成', '无', '已签收', 1.0);

-- 售后
INSERT INTO `order_after_sale`
(`order_after_sale_id`, `order_id`, `after_state`, `after_state_reply`, `type`, `content_desc`, `imgs`)
VALUES
(1, 2, '已审核', '已通过，请按流程寄回商品。', '退货退款', '测试售后申请', '/uploads/after-sale-1.jpg,/uploads/after-sale-2.jpg');

-- 物流
INSERT INTO `logistics_delivery`
(`logistics_delivery_id`, `order_number`, `product_name`, `purchase_quantity`, `total_transaction_amount`, `the_date_of_issuance`,
 `delivery_number`, `ordinary_users`, `shipping_address`, `delivery_status`, `signing_status`, `recommend`, `contact_name`, `merchant_id`)
VALUES
(1, 'ORD202603180001', 'Intel Core i7-14700K', '1', 2899.00, '2026-03-18', 'SF1234567890', 2, '上海市浦东新区测试路 100 号', '运输中', '未签收', 0, '张三', 1),
(2, 'ORD202603180002', 'NVIDIA RTX 4070 12G', '1', 4399.00, '2026-03-18', 'YD0987654321', 3, '北京市海淀区示例路 66 号', '已送达', '已签收', 1, '李四', 1);

-- 普通用户商家档案（用于商品所属商家与聊天测试）
INSERT INTO `merchant_users`
(`merchant_users_id`, `merchant_name`, `merchant_phone_number`, `store_name`, `qualification_certificate`, `examine_state`, `user_id`)
VALUES
(1, '官方店铺', '13811112222', '电脑配件官方店', '/uploads/merchant-cert-1.jpg', '已通过', 1);

-- 聊天消息测试数据
INSERT INTO `merchant_customer_service`
(`merchant_customer_service_id`, `user_id`, `goods_id`, `goods_title`, `session_id`, `merchant_users`, `sender_type`, `message_content`, `read_state`)
VALUES
(1, 2, 1, 'Intel Core i7-14700K', '1_1_2', 1, 'user', '这款 CPU 支持开发票吗？', '已读'),
(2, 2, 1, 'Intel Core i7-14700K', '1_1_2', 1, 'merchant', '支持开票，下单备注即可。', '未读');

-- 回收流程测试数据
INSERT INTO `accessories_recycling`
(`accessories_recycling_id`, `user_id`, `user_name`, `user_phone_number`, `accessory_name`, `accessory_type`, `usage_time`, `recycling_address`, `purchase_price`, `accessory_content`, `examine_state`, `quote_price`, `quote_remark`, `logistics_number`, `logistics_company`, `receiver_name`, `receiver_phone`, `receiver_address`)
VALUES
(1, 2, '张三', '13900000001', '二手显卡', '显卡', '2年', '上海市浦东新区测试路 100 号', 2999.00, '成色良好，包装齐全', '待报价', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(2, 3, '李四', '13900000002', '旧主板', '主板', '3年', '北京市海淀区示例路 66 号', 1299.00, '可正常点亮', '待商家收货', 399.00, '检测后预计可回收', 'SF9988776655', '顺丰', '王师傅', '13700001111', '深圳市南山区示例仓库 8 号');

SET FOREIGN_KEY_CHECKS = 1;
