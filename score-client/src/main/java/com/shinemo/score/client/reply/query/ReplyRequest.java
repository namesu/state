package com.shinemo.score.client.reply.query;

import com.shinemo.client.common.BaseDO;
import com.shinemo.client.common.FlagHelper;
import com.shinemo.score.client.comment.domain.CommentFlag;
import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-06-10
 */
@Data
public class ReplyRequest extends BaseDO {

    /**
     * 主评论id
     */
    private Long commentId;
    /**
     * 删除状态 1-正常 0-删除
     */
    private Integer status;
    /**
     * 回复内容
     */
    private String content;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 头像
     */
    private String avatarUrl;
    /**
     * 设备
     */
    private String device;
    /**
     * 网络类型
     */
    private String netType;
    /**
     * 排序权重
     */
    private Integer sort;
    /**
     * 扩展字段
     */
    private String extend;

    private String ip;

    /**
     * 完整机型
     */
    private String fullDevice;
    /**
     * 打标
     * @see CommentFlag
     */
    private Long flag;

    private FlagHelper replyFlag = FlagHelper.build();
}
