package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.MentalTestAnswer;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.service.IMentalTestAnswerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 心理测试作答相关接口
 */
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/mta")
public class MentalTestAnswerController {
    @Resource
    private IMentalTestAnswerService mentalTestAnswerService;

    /**
     * 创建心理测试问卷作答
     *
     * @param req 创建心理测试问卷作答参数
     * @return 创建结果
     */
    @RequestMapping
    public Result<Void> createAnswer(@RequestBody MentalTestAnswer req){
        log.info("创建心理测试作答：{}", req);
        mentalTestAnswerService.createAnswer(req);
        return Result.success();
    }

    /**
     * 查询心理测试问卷作答列表
     * @param page 页码
     * @param pageSize 页大小
     * @param testId 测试问卷ID
     * @param userId 作答用户ID
     * @return 心理健康测试问卷作答列表
     */
    @GetMapping("/list")
    public Result<GetListVO<List<MentalTestAnswer>>> list(
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(value = "test_id", required = false) String testId,
            @RequestParam(value = "user_id", required = false) String userId){
        log.info("查询心理测试作答列表：{} {} {} {}", page, pageSize, testId, userId);
        return Result.success(mentalTestAnswerService.list(page, pageSize, testId, userId));
    }

    /**
     * 删除心理测试作答
     *
     * @param id 心理测试作答ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id){
        log.info("删除心理测试作答：{}", id);
        mentalTestAnswerService.removeById(id);
        return Result.success();
    }

    /**
     * 根据ID查询心理测试作答
     *
     * @param id 心理测试作答ID
     * @return 心理测试作答
     */
    @GetMapping("/{id}")
    public Result<MentalTestAnswer> get(@PathVariable Long id){
        log.info("查询id为{}的心理测试作答", id);
        MentalTestAnswer mentalTestAnswer = mentalTestAnswerService.getById(id);
        return Result.success(mentalTestAnswer);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody MentalTestAnswer mentalTestAnswer){
        log.info("修改心理测试作答信息 {} {}",id, mentalTestAnswer);
        mentalTestAnswerService.update(id, mentalTestAnswer);
        return Result.success();
    }
}
