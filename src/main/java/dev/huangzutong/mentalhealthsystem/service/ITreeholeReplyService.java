package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.TreeholeReply;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.req.AddReplyReq;

/**
 * 树洞帖子回复表 服务类
 */
public interface ITreeholeReplyService extends IService<TreeholeReply> {

    /**
     * 创建树洞帖子回复
     *
     * @param req 创建树洞帖子回复请求参数
     */
    void createReply(AddReplyReq req);
}
