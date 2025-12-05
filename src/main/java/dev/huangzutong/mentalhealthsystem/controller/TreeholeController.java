package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.TreeholePost;
import dev.huangzutong.mentalhealthsystem.entity.TreeholeReply;
import dev.huangzutong.mentalhealthsystem.entity.req.AddPostReq;
import dev.huangzutong.mentalhealthsystem.entity.req.AddReplyReq;
import dev.huangzutong.mentalhealthsystem.entity.req.UpdatePostReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetTreeholePostVO;
import dev.huangzutong.mentalhealthsystem.service.ITreeholePostService;
import dev.huangzutong.mentalhealthsystem.service.ITreeholeReplyService;
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
    @Resource
    private ITreeholeReplyService treeholeReplyService;

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

    /**
     * 创建树洞帖子回复
     * @param req 回复参数
     * @return 创建结果
     */
    @PostMapping("/reply")
    public Result<Void> createReply(@RequestBody AddReplyReq req){
        log.info("创建树洞帖子回复：{}", req);
        treeholeReplyService.createReply(req);
        return Result.success();
    }

    /**
     * 获取树洞帖子回复列表
     * @param id 树洞帖子ID
     * @return 树洞帖子回复列表
     */
    @GetMapping("/reply/{id}")
    public Result<TreeholeReply> getReply(@PathVariable Long id){
        log.info("获取树洞帖子回复：{}", id);
        return Result.success(treeholeReplyService.getById(id));
    }

    /**
     * 删除树洞帖子回复
     * @param id 树洞帖子回复ID
     * @return 删除结果
     */
    @DeleteMapping("/reply/{id}")
    public Result<String> deleteReply(@PathVariable Long id){
        log.info("删除树洞帖子回复：{}", id);
        treeholeReplyService.removeById(id);
        return Result.success();
    }

    /**
     * 修改树洞帖子回复
     * @param id 树洞帖子回复ID
     * @param treeholeReply 树洞帖子回复
     * @return 修改结果
     */
    @PutMapping("/reply/{id}")
    public Result<String> updateReply(@PathVariable Long id, @RequestBody TreeholeReply treeholeReply){
        log.info("修改树洞帖子回复：{}", treeholeReply);
        treeholeReply.setId(id);
        treeholeReplyService.updateById(treeholeReply);
        return Result.success();
    }

    /**
     * 获取树洞帖子回复列表
     * @param id 树洞帖子ID
     * @return 树洞帖子回复列表
     */
    @GetMapping("/reply/list/{id}")
    public Result<List<TreeholeReply>> listReply(@PathVariable String id){
        log.info("查询树洞帖子回复列表：{}", id);
        return Result.success(treeholeReplyService.listPostReply(id));
    }
}
