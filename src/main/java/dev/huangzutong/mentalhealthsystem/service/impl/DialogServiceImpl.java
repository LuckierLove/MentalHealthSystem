package dev.huangzutong.mentalhealthsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.huangzutong.mentalhealthsystem.entity.Dialog;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.mapper.DialogMapper;
import dev.huangzutong.mentalhealthsystem.service.IDialogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对话记录 服务实现类
 */
@Service
public class DialogServiceImpl extends ServiceImpl<DialogMapper, Dialog> implements IDialogService {
    @Resource
    private DialogMapper dialogMapper;

    /**
     * 获取对话列表
     *
     * @param page         页码
     * @param pageSize     页大小
     * @param counselorId  咨询师id
     * @return 对话列表
     */
    @Override
    public GetListVO<List<Dialog>> listDialog(Long page, Long pageSize, String counselorId) {
        QueryWrapper<Dialog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(counselorId), "counselor_id", counselorId);
        Page<Dialog> pageHelper = new Page<>(page, pageSize);
        dialogMapper.selectPage(pageHelper, queryWrapper);
        return new GetListVO<>(pageHelper.getTotal(), pageHelper.getCurrent(), pageHelper.getRecords());
    }
}
