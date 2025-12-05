package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import dev.huangzutong.mentalhealthsystem.entity.TreeholeReply;
import dev.huangzutong.mentalhealthsystem.entity.req.AddReplyReq;
import dev.huangzutong.mentalhealthsystem.mapper.TreeholeReplyMapper;
import dev.huangzutong.mentalhealthsystem.service.ITreeholeReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 树洞帖子回复表 服务实现类
 */
@Service
public class TreeholeReplyServiceImpl extends ServiceImpl<TreeholeReplyMapper, TreeholeReply> implements ITreeholeReplyService {
    /**
     * 创建树洞帖子回复
     * @param req 创建树洞帖子回复参数
     */
    @Override
    public void createReply(AddReplyReq req) {
        TreeholeReply treeholeReply = new TreeholeReply();
        BeanUtils.copyProperties(req, treeholeReply);
        treeholeReply.setUserId(StpUtil.getLoginId().toString());
        treeholeReply.setReplyTime(LocalDateTime.now());
        save(treeholeReply);
    }

    /**
     * 获取树洞帖子回复列表
     *
     * @param id 树洞帖子id
     * @return 树洞帖子回复列表
     */
    @Override
    public List<TreeholeReply> listPostReply(String id) {
        QueryWrapper<TreeholeReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", id);
        return list(queryWrapper);
    }
}
