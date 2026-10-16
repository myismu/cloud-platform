/*
 Navicat Premium Dump SQL

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50737 (5.7.37)
 Source Host           : localhost:3306
 Source Schema         : user_hub

 Target Server Type    : MySQL
 Target Server Version : 50737 (5.7.37)
 File Encoding         : 65001

 Date: 28/03/2026 07:15:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for global_log
-- ----------------------------
DROP TABLE IF EXISTS `global_log`;
CREATE TABLE `global_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `system_id` bigint(20) NULL DEFAULT NULL COMMENT '所属系统ID',
  `request_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求的 URL',
  `request_method` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `ip_address` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP 地址',
  `user_agent` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'User-Agent 信息',
  `request_headers` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求头信息',
  `request_params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求参数',
  `response_status_code` int(11) NULL DEFAULT NULL COMMENT '响应状态码',
  `response_time` bigint(20) NULL DEFAULT NULL COMMENT '响应时间（毫秒）',
  `response_headers` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '响应头信息',
  `response_body` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '响应体内容',
  `exception_message` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常信息',
  `exception_stack_trace` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常堆栈信息',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户 ID',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `create_time` datetime NULL DEFAULT NULL COMMENT '日志创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '日志修改i时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2037662758085890050 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '全局请求日志记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for login_history
-- ----------------------------
DROP TABLE IF EXISTS `login_history`;
CREATE TABLE `login_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `system_id` bigint(20) NULL DEFAULT NULL COMMENT '所属系统ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `login_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `ip_address` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP 地址',
  `user_agent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '用户代理信息（浏览器或设备信息）',
  `client_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录设备类型，如：Android、iOS、PC',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2037662681409818627 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登录历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of login_history
-- ----------------------------

-- ----------------------------
-- Table structure for password_history
-- ----------------------------
DROP TABLE IF EXISTS `password_history`;
CREATE TABLE `password_history`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID，雪花ID生成',
  `system_id` bigint(20) NOT NULL COMMENT '系统ID，用于区分不同系统来源',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID，关联到users表',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '历史密码，存储加密后的密码',
  `change_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间，记录密码修改的具体时间点',
  `change_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改原因，记录密码修改的原因（如：定期更换、忘记密码等）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，记录数据创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间，记录数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_system_id`(`system_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '密码历史记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of password_history
-- ----------------------------

-- ----------------------------
-- Table structure for role_routes
-- ----------------------------
DROP TABLE IF EXISTS `role_routes`;
CREATE TABLE `role_routes`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `system_id` bigint(20) NULL DEFAULT NULL COMMENT '所属系统ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `route_id` bigint(20) NOT NULL COMMENT '路由ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色与路由关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_routes
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID，雪花ID生成',
  `system_id` bigint(20) NOT NULL COMMENT '所属系统ID，外键关联系统表',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称，如：admin、user等',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码（用于权限控制）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述，说明角色的用途和权限范围',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '角色状态，0-禁用，1-启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，记录数据创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间，记录数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_role_code`(`role_code`) USING BTREE,
  INDEX `idx_system_id`(`system_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (1928500986482003970, 1, '31', '3131', '1313', 1, '2025-05-31 01:17:19', '2025-05-31 01:33:10');
INSERT INTO `roles` VALUES (1931004312649990145, 11313131243124121, '13', '31', '331', 1, '2025-06-06 23:04:39', '2025-06-06 23:04:39');

-- ----------------------------
-- Table structure for routes
-- ----------------------------
DROP TABLE IF EXISTS `routes`;
CREATE TABLE `routes`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID，雪花ID生成',
  `system_id` bigint(20) NOT NULL COMMENT '所属系统ID，外键关联系统表',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路由路径，如：/api/users',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路由名称，用于前端路由匹配',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径，前端组件的位置',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父级路由ID，用于构建路由树',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由图标，用于前端显示',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '路由排序，用于前端显示顺序',
  `is_hidden` tinyint(1) NULL DEFAULT 0 COMMENT '是否隐藏（0-否，1-是）',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '路由状态，0-禁用，1-启用',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，记录数据创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间，记录数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_system_id`(`system_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '路由表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of routes
-- ----------------------------
INSERT INTO `routes` VALUES (1928499274643935234, 1, '1331', '313', '313', NULL, '131', 0, 0, 1, '3131', '2025-05-31 01:10:31', '2025-05-31 01:10:31');

-- ----------------------------
-- Table structure for system_info
-- ----------------------------
DROP TABLE IF EXISTS `system_info`;
CREATE TABLE `system_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统ID',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统编码，唯一标识如 exam-system',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统名称，如 考试系统',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '系统描述',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1930095349874077699 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统信息表，用于标识接入用户中心的系统' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_info
-- ----------------------------
INSERT INTO `system_info` VALUES (313131, 'examBaby-admin', '在线题录-管理界面', '在线题录的管理员界面', 1, '2025-12-22 21:40:44', '2025-12-22 21:40:44');
INSERT INTO `system_info` VALUES (11313131243124121, 'userHub', '用户中心', '用户中心系统', 1, '2025-06-01 02:44:40', '2025-06-01 03:24:27');
INSERT INTO `system_info` VALUES (11313131243124122, 'userHubTest', '用户中心-测试', '用户中心-测试系统', 1, '2025-06-04 08:37:05', '2025-06-04 08:37:05');
INSERT INTO `system_info` VALUES (1930095349874077698, 'examBaby-web', '在线题库-用户界面', '在线题库系统的用户界面', 1, '2025-06-04 10:52:45', '2025-12-22 21:39:57');

-- ----------------------------
-- Table structure for user_roles
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `system_id` bigint(20) NULL DEFAULT NULL COMMENT '所属系统ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_roles
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加密后的密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `disk_quota_mb` int(11) NULL DEFAULT 100 COMMENT '磁盘配额，单位MB，默认100MB',
  `is_locked` tinyint(1) NULL DEFAULT 0 COMMENT '是否锁定（0-否，1-是）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1934286740208533506 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (113, 'admin', '$2a$10$vnrTevZUh9RCUbRteeCR7u107sGkW.cdJDDfKLT7q5hnOmj4kXrA2', '3301882618@qq.com', '15347798010', '/img/cb86ab54-d3c5-4db1-ad1f-5f13d5ab74f0.jpg', 0, '2025-05-30 22:19:11', '2026-03-28 06:28:35');
INSERT INTO `users` VALUES (13131314144123451, 'mufeng', '$2a$10$vnrTevZUh9RCUbRteeCR7u107sGkW.cdJDDfKLT7q5hnOmj4kXrA2', '3301882618@qq.com', '15347798010', '', 0, '2025-06-01 22:03:43', '2025-06-22 14:50:18');
INSERT INTO `users` VALUES (1934285995824386050, 'mufeng2', '$2a$10$c8MX4QIXzKwJDULCbaCmKefIb4PcxshvLTE9yHm.N1Q8BF2y.SCGq', NULL, NULL, '', 1, NULL, '2025-06-22 14:50:16');
INSERT INTO `users` VALUES (1934286740208533505, 'mufeng3', '$2a$10$vnrTevZUh9RCUbRteeCR7u107sGkW.cdJDDfKLT7q5hnOmj4kXrA2', NULL, NULL, '', 1, NULL, '2025-06-22 14:50:21');

SET FOREIGN_KEY_CHECKS = 1;
