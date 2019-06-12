package com.shinemo.score.core.configuration;

import com.shinemo.my.redis.domain.RedisSentinelNode;
import com.shinemo.my.redis.service.RedisService;
import com.shinemo.my.redis.service.impl.RedisServiceImpl;
import com.shinemo.score.client.comment.facade.CommentFacadeService;
import com.shinemo.jce.spring.AaceProviderBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * provider 配置类
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Configuration
public class ProviderConfiguration {


    @Bean(initMethod = "init")
    @DependsOn("commentFacadeService")
    public AaceProviderBean providerCommentFacadeService(@Qualifier("commentFacadeService") CommentFacadeService commentFacadeService) {
        AaceProviderBean aaceProviderBean = new AaceProviderBean();
        aaceProviderBean.setInterfaceName(CommentFacadeService.class.getName());
        aaceProviderBean.setTarget(commentFacadeService);
        return aaceProviderBean;
    }

    @Bean
    public RedisService redisService(@Value("${spring.redis.database}") Integer database,
                                     @Value("${spring.redis.ip}") String redisIp,
                                     @Value("${spring.redis.master.name}") String masterName,
                                     @Value("${spring.redis.pwd}") String pwd) {
        RedisSentinelNode node = new RedisSentinelNode(redisIp, masterName);
        node.setDatabase(database);
        node.setPassword(pwd);
        return new RedisServiceImpl(node);
    }
}