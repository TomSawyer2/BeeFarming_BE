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

 Date: 13/11/2022 22:57:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for code
-- ----------------------------
DROP TABLE IF EXISTS `code`;
CREATE TABLE `code`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` int(0) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of code
-- ----------------------------
INSERT INTO `code` VALUES (2, 'Excepteur dolore', 3);

SET FOREIGN_KEY_CHECKS = 1;
