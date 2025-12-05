package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.Favourite;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.service.IFavouriteService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 获取收藏列表
     * @param page 页码
     * @param pageSize 页大小
     * @param studentId 学生对应用户ID
     * @return 收藏列表
     */
    @GetMapping("/list")
    public Result<GetListVO<List<Favourite>>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(value = "student_id", required = false) String studentId
    ) {
        return Result.success(favouriteService.list(page, pageSize, studentId));
    }

    /**
     * 删除收藏
     * @param id 收藏ID
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("删除收藏：{}", id);
        favouriteService.removeById(id);
        return Result.success();
    }
}
