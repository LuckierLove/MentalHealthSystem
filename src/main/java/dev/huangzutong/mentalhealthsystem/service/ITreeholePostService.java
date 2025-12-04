package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.TreeholePost;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.req.AddPostReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetTreeholePostVO;

import java.util.List;

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

    /**
     * 获取树洞帖子列表
     *
     * @param page       页码
     * @param pageSize   页大小
     * @param keyword    关键词
     * @param pass       审核状态（0：不通过； 1：通过）
     * @return 树洞帖子列表
     */
    GetListVO<List<GetTreeholePostVO>> getPostList(Integer page, Integer pageSize, String keyword, Integer pass);
}
