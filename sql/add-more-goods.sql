USE `computer_mall`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 清除现有的商品数据
DELETE FROM `goods`;
ALTER TABLE `goods` AUTO_INCREMENT = 1;

-- 商品数据
INSERT INTO `goods`
(`goods_id`, `title`, `img`, `description`, `price_ago`, `price`, `sales`, `inventory`, `type`, `hits`, `content`,
 `img1`, `img2`, `img3`, `img4`, `img5`, `customize_field`, `user_id`)
VALUES
-- CPU 分类
(1, 'Intel Core i7-14700K', '/uploads/063199cf95ec4cbc8825bb9166254b37.JPG', '20 核高性能处理器，适合游戏与生产力场景', 3299.00, 2899.00, 56, 120, 1, 88,
 '<p>支持高频 DDR5，适合中高端装机。</p>',
 '/uploads/0bb8c6d270b346c1961064e627b856ff.JPG', '/uploads/0e4af788d3104ee19a39c3742f87d202.JPG', '/uploads/111a227e48c24085b0838056356da0db.JPG', '', '', '盒装', 1),

(2, 'AMD Ryzen 7 7800X3D', '/uploads/1b63aed8d6a9460789e7b789e8689123.JPG', '8 核 16 线程，3D V-Cache 技术，游戏性能出色', 3999.00, 3499.00, 42, 80, 1, 72,
 '<p>AMD 锐龙 7 7800X3D 处理器，专为游戏优化。</p>',
 '/uploads/21c9d77149ec401a96cfcf0a52be3380.JPG', '/uploads/262bbc04b3e14f149a376decd01b071b.JPG', '', '', '', '盒装', 1),

-- 显卡分类
(3, 'NVIDIA RTX 4070 12G', '/uploads/28b105f0539c4e4b8ae33002efd0f87a.JPG', '热门甜品级显卡，支持光追与 DLSS', 4999.00, 4399.00, 34, 80, 2, 66,
 '<p>适合 2K 游戏与轻度 AI 创作。</p>',
 '/uploads/2ab99731e6934d38a75979717b2921c0.JPG', '/uploads/36bdd6bbe5a242488a4bc67042ade745.JPG', '', '', '', '三风扇', 1),

(4, 'AMD Radeon RX 7800 XT', '/uploads/3827732d546246199eb3af48ddd9936d.JPG', '16GB GDDR6 显存，支持光线追踪', 4599.00, 3999.00, 28, 60, 2, 58,
 '<p>AMD 最新一代 RDNA 3 架构显卡。</p>',
 '/uploads/38ce62bd745f45f5b5d2659eb4c5ab96.JPG', '/uploads/3da184d034f243f2b58f31058ccbc4f7.JPG', '', '', '', '双风扇', 1),

-- 内存分类
(5, 'DDR5 32GB 6000MHz 内存套装', '/uploads/41162fea17ec46478c1bdda5722b47eb.JPG', '双通道高频内存，兼顾性能与稳定', 999.00, 799.00, 73, 200, 3, 51,
 '<p>适合搭配新平台主板使用。</p>',
 '/uploads/422a1f423bc3496fb0c8421597edc2de.JPG', '', '', '', '', 'RGB 灯效', 1),

(6, 'DDR5 16GB 6400MHz 单条', '/uploads/4646998d878847f78d6016fffdf02db7.JPG', '高频单条内存，适合升级或单通道使用', 499.00, 429.00, 56, 150, 3, 45,
 '<p>支持 XMP 3.0，一键超频。</p>',
 '/uploads/48f042e919304231a449030dbb2ede95.png', '', '', '', '', '单条', 1),

-- 固态硬盘分类
(7, 'Samsung 990 PRO 2TB', '/uploads/493f46ef69254f7a9665c53347865c36.JPG', 'PCIe 4.0 NVMe SSD，速度快容量大', 1399.00, 1199.00, 48, 150, 4, 59,
 '<p>适合系统盘和游戏盘。</p>',
 '/uploads/4cc31e406ab949ceb8afa511d85d72bd.JPG', '/uploads/52ce9e2503a54babaa4f1c8c89f1f2a3.JPG', '', '', '', 'M.2', 1),

(8, 'WD Black SN850X 1TB', '/uploads/5493cb18264747ed8858408deb39c29b.jpg', 'PCIe 4.0 NVMe SSD，游戏级性能', 899.00, 749.00, 62, 180, 4, 64,
 '<p>WD Black SN850X 高性能固态硬盘。</p>',
 '/uploads/556e34d106f249488f45a6acf86b54f9.JPG', '/uploads/5640461ba21e423eb7393cacbdfd8c05.JPG', '', '', '', 'M.2', 1),

-- 主板分类
(9, 'ROG STRIX Z790-E 主板', '/uploads/5ddf65f378e6472384fdc14bc73560f7.JPG', '高端主板，供电和扩展能力出色', 3899.00, 3399.00, 19, 60, 5, 41,
 '<p>适合高性能 Intel 平台整机。</p>',
 '/uploads/6c56d3056ff243d68e48169c07207f8d.JPG', '', '', '', '', 'ATX', 1),

(10, 'MSI B650M迫击炮', '/uploads/6c64d158c3b64bc094ae635803b11ae7.JPG', '中端主板，性价比出色，支持 AMD 锐龙 7000 系列', 1299.00, 1099.00, 45, 120, 5, 52,
 '<p>MSI B650M 迫击炮主板，适合 AMD 平台。</p>',
 '/uploads/6fcb227a9d524c4e8dba3b6d033f868a.JPG', '', '', '', '', 'M-ATX', 1),

-- 电源分类
(11, '振华 LEADEX G 850W', '/uploads/7200baa60f8e41e8ba167cda0b566409.png', '金牌全模组电源，稳定可靠', 899.00, 799.00, 32, 80, 6, 38,
 '<p>振华 LEADEX G 系列金牌全模组电源。</p>',
 '/uploads/7e505f71e3d9474997f5fd35d395cd54.JPG', '', '', '', '', '850W', 1),

(12, '海盗船 RM750x', '/uploads/815502e491f04756837d1567f3f05f56.JPG', '金牌全模组电源，静音设计', 799.00, 699.00, 28, 70, 6, 35,
 '<p>海盗船 RM750x 金牌全模组电源。</p>',
 '/uploads/83600d688a3d446e97dd0538f81d76bc.JPG', '', '', '', '', '750W', 1),

-- 机箱分类
(13, 'NZXT H5 Flow', '/uploads/83677567c6634738909022b1fe5ff2c5.JPG', '中塔机箱，良好的散热性能', 499.00, 429.00, 25, 60, 7, 32,
 '<p>NZXT H5 Flow 中塔机箱，支持 360mm 水冷。</p>',
 '/uploads/85af691388254c9db503df6bbd9bc23b.JPG', '', '', '', '', '中塔', 1),

(14, 'Lian Li O11 Dynamic', '/uploads/881b946f278f43f28eaf4740d1735c1d.JPG', '海景房机箱，炫酷外观', 899.00, 799.00, 18, 40, 7, 45,
 '<p>Lian Li O11 Dynamic 海景房机箱。</p>',
 '/uploads/8dd3c206cef341949b496bd0f5f53bee.JPG', '', '', '', '', '中塔', 1),

-- 散热器分类
(15, 'NZXT Kraken X73', '/uploads/99d75855f0c54e448cd96aaf333397f9.JPG', '360mm 水冷散热器，高效降温', 899.00, 799.00, 22, 50, 8, 36,
 '<p>NZXT Kraken X73 360mm 水冷散热器。</p>',
 '/uploads/9a591b5875f64bffa636f1d6fcbf285d.JPG', '', '', '', '', '360mm 水冷', 1),

(16, 'Noctua NH-D15', '/uploads/a0293741fa2243d5a149c6e5c9bfea72.JPG', '双塔风冷散热器，静音效果出色', 499.00, 429.00, 35, 80, 8, 42,
 '<p>Noctua NH-D15 双塔风冷散热器。</p>',
 '/uploads/a683f9c6c064422e9ad0659877b31df4.JPG', '', '', '', '', '风冷', 1),

-- 显示器分类
(17, 'LG 27GP850', '/uploads/a694c71e68a44a0eadb22a6561b2946b.JPG', '27英寸 2K 165Hz IPS 显示器', 2499.00, 1999.00, 38, 70, 9, 48,
 '<p>LG 27GP850 电竞显示器，支持 G-Sync Compatible。</p>',
 '/uploads/a883657241414ad79e4188b1eb43c140.JPG', '', '', '', '', '27英寸 2K', 1),

(18, 'ASUS TUF VG289Q', '/uploads/a8911ac1e1a048b1b51663f8be97b732.JPG', '28英寸 4K 60Hz IPS 显示器', 2999.00, 2499.00, 26, 50, 9, 41,
 '<p>ASUS TUF VG289Q 4K 显示器，适合设计和办公。</p>',
 '/uploads/aa9e27f2a8b6459893012fcaef41fcc6.JPG', '', '', '', '', '28英寸 4K', 1),

-- 外设分类
(19, '罗技 G Pro X Superlight 2', '/uploads/ad190807a8244374a8e90da0028b0254.JPG', '轻量化无线游戏鼠标', 899.00, 799.00, 45, 100, 10, 56,
 '<p>罗技 G Pro X Superlight 2 轻量化无线游戏鼠标。</p>',
 '/uploads/ade1a4008eab4fedbb1b900344ea3d27.JPG', '', '', '', '', '无线', 1),

(20, 'Cherry MX 3.0S', '/uploads/aea2e1d236bb4bd2b7dcaaa4982a3c8e.jpg', '机械键盘，Cherry MX 轴体', 599.00, 499.00, 32, 80, 10, 42,
 '<p>Cherry MX 3.0S 机械键盘，德国原厂轴体。</p>',
 '/uploads/c06b59170f59483997402f173b964d7b.jpg', '', '', '', '', '机械键盘', 1);

SET FOREIGN_KEY_CHECKS = 1;