package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.Dialog;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;

import java.util.List;

/**
 *  对话记录 服务类
 */
public interface IDialogService extends IService<Dialog> {

    /**
     * 获取对话记录列表
     *
     * @param page 页码
     * @param pageSize 页大小
     * @param counselorId 咨询师ID
     * @return 对话记录列表
     */
    GetListVO<List<Dialog>> listDialog(Long page, Long pageSize, String counselorId);
}
