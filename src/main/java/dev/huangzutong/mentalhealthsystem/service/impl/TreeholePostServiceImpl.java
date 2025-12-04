package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.huangzutong.mentalhealthsystem.entity.TreeholePost;
import dev.huangzutong.mentalhealthsystem.entity.req.AddPostReq;
import dev.huangzutong.mentalhealthsystem.entity.req.UpdatePostReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetTreeholePostVO;
import dev.huangzutong.mentalhealthsystem.mapper.TreeholePostMapper;
import dev.huangzutong.mentalhealthsystem.service.ITreeholePostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 树洞帖子表 服务实现类
 */
@Service
public class TreeholePostServiceImpl extends ServiceImpl<TreeholePostMapper, TreeholePost> implements ITreeholePostService {

    private final TreeholePostMapper treeholePostMapper;

    public TreeholePostServiceImpl(TreeholePostMapper treeholePostMapper) {
        this.treeholePostMapper = treeholePostMapper;
    }

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

    /**
     * 获取树洞帖子详情
     *
     * @param id 帖子ID
     * @return 树洞帖子详情
     */
    @Override
    public TreeholePost getTreeholePost(Long id) {
        return getById(id);
    }

    /**
     * 获取树洞帖子列表
     *
     * @param page       页码
     * @param pageSize   每页数量
     * @param keyword    关键词
     * @param pass       审核状态
     * @return 树洞帖子列表
     */
    @Override
    public GetListVO<List<TreeholePost>> getPostList(Integer page, Integer pageSize, String keyword, Integer pass) {
        QueryWrapper<TreeholePost> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(keyword), "title", keyword);
        queryWrapper.eq(pass != null, "pass", pass);
        queryWrapper.orderByDesc("post_time");
        Page<TreeholePost> pageHelper = new Page<>(page, pageSize);
        treeholePostMapper.selectPage(pageHelper, queryWrapper);

        GetListVO<List<TreeholePost>> getListVO = new GetListVO<>();
        getListVO.setTotal(pageHelper.getTotal());
        getListVO.setPage(pageHelper.getCurrent());
        getListVO.setRecords(
                pageHelper.getRecords()
        );
        return getListVO;
    }

    /**
     * 更新树洞帖子
     *
     * @param id 树洞帖子ID
     * @param req 更新树洞帖子参数
     */
    @Override
    public void updatePost(Long id, UpdatePostReq req) {
        TreeholePost treeholePost = new TreeholePost();
        BeanUtils.copyProperties(req, treeholePost);
        treeholePost.setId(id.toString());
        updateById(treeholePost);
    }
}
