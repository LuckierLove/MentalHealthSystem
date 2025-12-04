package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.huangzutong.mentalhealthsystem.entity.TreeholePost;
import dev.huangzutong.mentalhealthsystem.entity.req.AddPostReq;
import dev.huangzutong.mentalhealthsystem.mapper.TreeholePostMapper;
import dev.huangzutong.mentalhealthsystem.service.ITreeholePostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 树洞帖子表 服务实现类
 */
@Service
public class TreeholePostServiceImpl extends ServiceImpl<TreeholePostMapper, TreeholePost> implements ITreeholePostService {

    /**
     * 添加树洞帖子
     *
     * @param req 添加树洞帖子请求参数
     */
    @Override
    public void addPost(AddPostReq req) {
        TreeholePost treeholePost = new TreeholePost();
        BeanUtils.copyProperties(req, treeholePost);
        treeholePost.setId(new SnowflakeGenerator().next().toString());
        treeholePost.setUserId(StpUtil.getLoginId().toString());
        treeholePost.setPostTime(LocalDateTime.now());
        treeholePost.setPass(0);
        save(treeholePost);
    }
}
