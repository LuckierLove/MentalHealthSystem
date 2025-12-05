package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.Favourite;
import dev.huangzutong.mentalhealthsystem.service.IFavouriteService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 收藏相关接口
 */
@Slf4j
@RestController
@SaCheckLogin
@RequestMapping("/favourite")
public class FavouriteController {
    @Resource
    private IFavouriteService favouriteService;
    /**
     * 创建收藏
     * @param favourite 创建收藏参数
     */
    @PostMapping
    public Result<Void> createFavourite(@RequestBody Favourite favourite) {
        log.info("创建收藏：{}", favourite);
        favouriteService.createFavourite(favourite);
        return Result.success();
    }
}
