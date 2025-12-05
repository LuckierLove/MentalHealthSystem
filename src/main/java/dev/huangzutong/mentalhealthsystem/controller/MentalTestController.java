package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.MentalTest;
import dev.huangzutong.mentalhealthsystem.entity.req.AddMentalTestReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.service.IMentalTestService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 问卷相关接口
 */
@SaCheckLogin
@Slf4j
@RequestMapping("/mt")
@RestController
public class MentalTestController {
    @Resource
    private IMentalTestService mentalTestService;

    /**
     * 添加问卷
     * @param req 添加问卷请求参数
     * @return 添加结果
     */
    @SaCheckPermission(value = "mental_test.add", orRole = "咨询师")
    @PostMapping
    public Result<Void> addMentalTest(@RequestBody AddMentalTestReq req) {
        log.info("添加问卷：{}", req);
        mentalTestService.addMentalTest(req);
        return Result.success();
    }


    /**
     * 获取心理健康测试问卷列表
     *
     * @param page 页码
     * @param pageSize 页而大小
     * @param keyword 问卷标题关键词
     * @param counselorId 咨询师对应用户ID
     * @return 心理健康测试问卷列表
     */
    @GetMapping("/list")
    public Result<GetListVO<List<MentalTest>>> list(@RequestParam(defaultValue = "1") Long page,
                                                    @RequestParam(defaultValue = "10") Long pageSize,
                                                    @RequestParam(required = false) String keyword,
                                                    @RequestParam(value = "counselor_id",required = false) String counselorId) {
        GetListVO<List<MentalTest>> list = mentalTestService.list(page, pageSize, keyword, counselorId);
        return Result.success(list);
    }

    /**
     * 删除心理健康测试问卷
     * @param id 问卷ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission(value = "mental_test:delete", orRole = {"管理员", "咨询师"})
    public Result<Void> delete(@PathVariable String id) {
        mentalTestService.removeById(id);
        return Result.success();
    }

    /**
     * 修改心理健康测试问卷
     * @param id 问卷ID
     * @param test 修改信息
     * @return 修改结果
     */
    @PutMapping("/{id}")
    @SaCheckPermission(value = "mental_test:update", orRole = {"管理员", "咨询师"})
    public Result<Void> update(@PathVariable String id, @RequestBody MentalTest test) {
        test.setId(id);
        mentalTestService.updateById(test);
        return Result.success();
    }
}
