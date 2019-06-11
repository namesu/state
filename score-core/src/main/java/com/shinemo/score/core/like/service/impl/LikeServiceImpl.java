package com.shinemo.score.core.like.service.impl;

import com.shinemo.client.async.InternalEventBus;
import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.exception.BizException;
import com.shinemo.score.client.comment.domain.LikeTypeEnum;
import com.shinemo.score.client.error.ScoreErrors;
import com.shinemo.score.client.like.domain.LikeDO;
import com.shinemo.score.client.like.query.LikeQuery;
import com.shinemo.score.client.like.query.LikeRequest;
import com.shinemo.score.core.async.event.AfterLikeEvent;
import com.shinemo.score.core.like.service.LikeService;
import com.shinemo.score.dal.like.wrapper.LikeWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author wenchao.li
 * @since 2019-06-11
 */
@Service
@Slf4j
public class LikeServiceImpl implements LikeService {

    @Resource
    private LikeWrapper likeWrapper;

    @Resource
    private InternalEventBus internalEventBus;

    @Override
    public LikeDO create(LikeRequest request) {

        Assert.notNull(request.getCommentId(), "commentId not be empty");
        Assert.notNull(request.getLikeAction(), "likeAction not be empty");
        Assert.notNull(request.getUid(), "uid not be empty");

        LikeDO likeDO = new LikeDO();
        likeDO.setCommentId(request.getCommentId());
        likeDO.setType(request.getLikeAction());
        likeDO.setUid(request.getUid());

        Result<LikeDO> insertRs = likeWrapper.insert(likeDO);
        if (!insertRs.hasValue()) {
            log.error("[create_like] has error,request:{},rs:{}", request, insertRs);
            throw new BizException(insertRs.getError());
        }

        // 异步更新评论
        refreshComment(request.getCommentId(), request.getLikeAction());

        return insertRs.getValue();
    }

    @Override
    public LikeDO update(LikeRequest request) {

        Assert.notNull(request.getCommentId(), "commentId not be empty");
        Assert.notNull(request.getLikeAction(), "likeAction not be empty");
        Assert.notNull(request.getUid(), "uid not be empty");

        LikeDO oldDO = getByCommentIdAndUid(request.getCommentId(), request.getUid());

        if (oldDO.getType().equals(LikeTypeEnum.ADD.getId())) {
            throw new BizException(ScoreErrors.DO_NOT_REPEAT_OPERATE.getCode(), "请勿重复点赞");
        } else if (oldDO.getType().equals(LikeTypeEnum.REMOVE.getId())) {
            throw new BizException(ScoreErrors.DO_NOT_REPEAT_OPERATE.getCode(), "您还未点赞");
        }

        LikeDO likeDO = new LikeDO();
        likeDO.setId(oldDO.getId());
        likeDO.setType(request.getLikeAction());

        Result<LikeDO> updateRs = likeWrapper.update(likeDO);
        if (!updateRs.hasValue()) {
            throw new BizException(updateRs.getError());
        }

        // 异步更新评论
        refreshComment(request.getCommentId(), request.getLikeAction());

        return updateRs.getValue();
    }

    @Override
    public LikeDO getByCommentIdAndUid(Long commentId, Long uid) {

        Assert.notNull(commentId, "commentId not be empty");
        Assert.notNull(uid, "uid not be empty");


        LikeQuery query = new LikeQuery();
        query.setCommentId(commentId);
        query.setUid(uid);
        return getByQuery(query);
    }

    @Override
    public LikeDO getByQuery(LikeQuery likeQuery) {

        Result<LikeDO> likeRs = likeWrapper.get(likeQuery, ScoreErrors.LIKE_LOG_NOT_EXIST);
        if (!likeRs.hasValue()) {
            throw new BizException(likeRs.getError());
        }
        return likeRs.getValue();
    }

    @Override
    public ListVO<LikeDO> findByQuery(LikeQuery query) {
        Result<ListVO<LikeDO>> likeRs = likeWrapper.find(query);
        if (!likeRs.hasValue()) {
            throw new BizException(likeRs.getError());
        }
        return likeRs.getValue();
    }

    // 异步更新评论
    private void refreshComment(Long commentId, Integer likeAction) {
        internalEventBus.post(
                AfterLikeEvent.builder()
                        .commentId(commentId)
                        .likeAction(likeAction)
                        .build()
        );
    }
}