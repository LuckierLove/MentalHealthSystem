package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.TreeholePost;
import dev.huangzutong.mentalhealthsystem.entity.req.AddPostReq;
import dev.huangzutong.mentalhealthsystem.entity.req.UpdatePostReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetTreeholePostVO;
import dev.huangzutong.mentalhealthsystem.service.ITreeholePostService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 树洞相关接口
 */
@RestController
@RequestMapping("/treehole")
@Slf4j
@SaCheckLogin
public class TreeholeController {
    @Resource
    private ITreeholePostService treeholePostService;

    /**
     * 创建树洞帖子
     * @param req 创建树洞帖子参数
     * @return 创建结果
     */
    @PostMapping("/post")
    public Result<Void> addPost(@RequestBody AddPostReq req){
        log.info("创建树洞帖子：{}", req);
        treeholePostService.addPost(req);
        return Result.success();
    }

    /**
     * 获取树洞帖子详情
     * @param id 帖子ID
     * @return 树洞帖子详情
     */
    @GetMapping("/post/{id}")
    public Result<TreeholePost> getPost(@PathVariable Long id){
        log.info("获取树洞帖子详情：{}", id);
        return Result.success(treeholePostService.getTreeholePost(id));
    }

    /**
     * 获取树洞帖子列表
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 标题关键字
     * @param pass 审核状态
     * @return 树洞帖子列表
     */
    @GetMapping("/post/list")
    public Result<GetListVO<List<TreeholePost>>> getPostList(@RequestParam(defaultValue = "1") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(required = false) String keyword,
                                                                  @RequestParam(required = false) Integer pass){
        log.info("获取树洞帖子列表：{}", page);
        return Result.success(treeholePostService.getPostList(page, pageSize, keyword, pass));
    }

    /**
     * 更新树洞帖子
     * @param id 帖子ID
     * @param req 更新参数
     * @return 更新结果
     */
    @PutMapping("/post/{id}")
    public Result<Void> updatePost(@PathVariable Long id, @RequestBody UpdatePostReq req){
        log.info("更新树洞帖子：{}", req);
        treeholePostService.updatePost(id, req);
        return Result.success();
    }

    /**
     * 删除树洞帖子
     * @param id 帖子ID
     * @return 删除结果
     */
    @DeleteMapping("/post/{id}")
    public Result<Void> deletePost(@PathVariable Long id){
        log.info("删除树洞帖子：{}", id);
        treeholePostService.removeById(id);
        return Result.success();
    }
}
