CREATE TABLE IF NOT EXISTS `community_request` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `request_type` VARCHAR(16) NOT NULL,
    `name` VARCHAR(40) NOT NULL,
    `contact` VARCHAR(120) DEFAULT NULL,
    `category` VARCHAR(60) DEFAULT NULL,
    `message` VARCHAR(1000) NOT NULL,
    `status` TINYINT NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `reviewed_at` TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_community_request_status` (`status`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
