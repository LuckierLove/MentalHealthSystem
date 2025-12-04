package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.TreeholePost;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.req.AddPostReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetTreeholePostVO;

/**
 * 树洞帖子表 服务类
 */
public interface ITreeholePostService extends IService<TreeholePost> {

    /**
     * 添加树洞帖子
     *
     * @param req 添加树洞帖子请求参数
     */
    void addPost(AddPostReq req);

    /**
     * 获取树洞帖子详情
     *
     * @param id 帖子ID
     * @return 树洞帖子详情
     */
    GetTreeholePostVO getTreeholePost(Long id);
}
