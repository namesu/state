DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmt_create`   datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `status` int  NOT NULL COMMENT '删除状态 1-正常 0-删除',
  `movie_id` varchar(128) not null comment '电影id',
  `score` int(4) not null comment '评分',
  `uid`   bigint not null comment '用户id',
  `extend` text DEFAULT null comment '扩展字段',
  `version` bigint not null DEFAULT 0 comment '版本',
  PRIMARY KEY (`id`),
  UNIQUE key `uk_m_id`(`movie_id`,`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='电影评分表';


#初始评分7分 俩百个人同时评分 怎么更新? 先插入数据
#定时任务扫描 半天之前更新的评分  然后根据电影分组 更新电影分数
#每天凌晨扫描昨天更新的评分   然后根据电影分组 更新分数 并把之前的分数更新为昨日分数