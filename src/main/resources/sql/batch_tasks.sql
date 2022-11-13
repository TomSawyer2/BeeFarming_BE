/*
 Navicat Premium Data Transfer

 Source Server         : local-3308
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3308
 Source Schema         : bee_farming

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 13/11/2022 22:57:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for batch_tasks
-- ----------------------------
DROP TABLE IF EXISTS `batch_tasks`;
CREATE TABLE `batch_tasks`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `user_id` int(0) NOT NULL,
  `status` int(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  `code_id_a` int(0) NULL DEFAULT NULL,
  `code_id_b` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of batch_tasks
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
