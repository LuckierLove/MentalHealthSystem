package dev.huangzutong.mentalhealthsystem.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.Counselor;
import dev.huangzutong.mentalhealthsystem.entity.req.AddCounselorReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetCounselorListVO;
import dev.huangzutong.mentalhealthsystem.service.ICounselorService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 咨询师相关接口
 */
@SaCheckLogin
@Slf4j
@RestController
@RequestMapping("/counselor")
public class CounselorController {
    @Resource
    private ICounselorService counselorService;

    /**
     * 添加咨询师
     *
     * @param req 添加咨询师请求参数
     * @return 添加结果
     */
    @PostMapping
    @SaCheckPermission(value = "counselor:add", orRole = "管理员")
    public Result<Void> addCounselor(@RequestBody AddCounselorReq req) {
        log.info("添加咨询师 {}", req);
        counselorService.addCounselor(req);
        return Result.success();
    }

    /**
     * 删除咨询师
     *
     * @param id 咨询师id
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission(value = "counselor:delete", orRole = "管理员")
    public Result<Void> deleteCounselor(@PathVariable String id) {
        log.info("删除咨询师 {}", id);
        counselorService.removeCounselor(id);
        return Result.success();
    }

    /**
     * 获取咨询师详情
     *
     * @param id 咨询师id
     * @return 咨询师信息
     */
    @GetMapping("/{id}")
    public Result<Counselor> getCounselor(@PathVariable String id) {
        log.info("获取咨询师 {}", id);
        return Result.success(counselorService.getById(id));
    }

    /**
     * 更新咨询师信息
     *
     * @param id 咨询师id
     * @param req 更新咨询师请求参数
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @SaCheckPermission(value = "counselor:update", orRole = "管理员")
    public Result<Void> updateCounselor(@PathVariable String id, @RequestBody AddCounselorReq req) {
        log.info("更新咨询师 {}", req);
        counselorService.updateCounselor(id, req);
        return Result.success();
    }

    /**
     * 获取咨询师列表
     *
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 姓名关键词
     * @return 咨询师列表
     */
    @GetMapping("/list")
    public Result<GetCounselorListVO> getCounselorList(@RequestParam(defaultValue = "1", required = false) Long page,
                                                        @RequestParam(defaultValue = "10", required = false) Long pageSize,
                                                       @RequestParam(required = false) String keyword) {
        log.info("获取咨询师列表 {} {} {}", page, pageSize, keyword);
        GetCounselorListVO vo = counselorService.getCounselorList(page, pageSize, keyword);
        return Result.success(vo);
    }
}
