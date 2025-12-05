package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import dev.huangzutong.mentalhealthsystem.entity.TreeholeReply;
import dev.huangzutong.mentalhealthsystem.entity.req.AddReplyReq;
import dev.huangzutong.mentalhealthsystem.mapper.TreeholeReplyMapper;
import dev.huangzutong.mentalhealthsystem.service.ITreeholeReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
