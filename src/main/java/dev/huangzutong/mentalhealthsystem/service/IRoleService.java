package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;

import java.util.List;

/**
 * 角色表 服务类
 */
public interface IRoleService extends IService<Role> {

    /**
     * 获取角色列表
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 角色名关键字
     * @return 角色列表
     */
    GetListVO<List<Role>> list(Long page, Long pageSize, String keyword);
}
