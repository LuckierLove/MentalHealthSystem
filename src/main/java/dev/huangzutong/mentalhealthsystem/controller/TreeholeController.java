package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.req.AddPostReq;
import dev.huangzutong.mentalhealthsystem.service.ITreeholePostService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
