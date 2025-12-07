package dev.huangzutong.mentalhealthsystem.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.Dialog;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.service.IDialogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 对话记录相关接口
 */
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/dialog")
public class DialogController {
    @Resource
    private IDialogService dialogService;
    /**
     * 获取对话记录
     * @param page 页码
     * @param pageSize 页大小
     * @param counselorId 咨询师ID
     * @return 对话记录列表
     */
    @GetMapping("/list")
    public Result<GetListVO<List<Dialog>>> listDialog(@RequestParam(required = false, defaultValue = "1") Long page,
                                                      @RequestParam(required = false, defaultValue = "10") Long pageSize,
                                                      @RequestParam(value = "counselor_id",required = false) String counselorId) {
        return Result.success(dialogService.listDialog(page, pageSize, counselorId));
    }

    /**
     * 获取对话记录详情
     * @param dialog_id 对话记录ID
     * @return 对话记录详情
     */
    @GetMapping("/{dialog_id}")
    public Result<Dialog> getDialog(@RequestParam Long dialog_id) {
        return Result.success(dialogService.getById(dialog_id));
    }

    /**
     * 添加对话记录
     * @param dialog 对话记录
     * @return 对话记录
     */
    @PostMapping
    public Result<Dialog> addDialog(@RequestBody Dialog dialog) {
        dialogService.save(dialog);
        return Result.success(dialog);
    }

    /**
     * 修改对话记录
     * @param dialog_id 对话记录ID
     * @param dialog 对话记录
     * @return 对话记录
     */
    @PutMapping("/{dialog_id}")
    public Result<Dialog> updateDialog(@PathVariable Long dialog_id, @RequestBody Dialog dialog) {
        dialog.setId(dialog_id);
        dialogService.updateById(dialog);
        return Result.success(dialog);
    }

    /**
     * 删除对话记录
     * @param dialog_id 对话记录ID
     * @return 删除结果
     */
    @DeleteMapping("/{dialog_id}")
    public Result<Void> deleteDialog(@PathVariable Long dialog_id) {
        dialogService.removeById(dialog_id);
        return Result.success();
    }

}
