package com.shinemo.score.core.comment.service;

import com.shinemo.client.common.ListVO;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.client.comment.query.CommentQuery;
import com.shinemo.score.client.comment.query.CommentRequest;

import java.util.List;

/**
 * 评论服务
 *
 * @author wenchao.li
 * @since 2019-06-06
 */
public interface CommentService {


    /**
     * 创建评论
     * 会更新缓存
     *
     * @param request
     */
    CommentDO create(CommentRequest request);

    /**
     * 更新
     * 会更新缓存
     */
    void update(CommentRequest request);

    /**
     * @param commentId 评论id
     * @return 根据评论id查找评论, 会先走缓存
     */
    CommentDO getById(Long commentId);

    /**
     * @return 查询评论
     */
    CommentDO getByQuery(CommentQuery query);


    /**
     * @return 查询评论列表
     */
    ListVO<CommentDO> findByQuery(CommentQuery query);

    /**
     * @return 查询出所有匹配的评论id
     */
    ListVO<Long> findIdsByQuery(CommentQuery query);

    /**
     * @return 根据commentId直接走db查询
     */
    CommentDO getByIdFromDB(Long commentId);

    /**
     * throw 评论关闭，则code 100007
     */
    void checkCommentOpen();

    void delete(CommentRequest delReq);

    /**
     *  返回结果 处理敏感词
     */
    void transferSensitiveWord(CommentDO commentDO);
}
