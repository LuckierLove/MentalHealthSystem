package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.huangzutong.mentalhealthsystem.entity.Counselor;
import dev.huangzutong.mentalhealthsystem.entity.Role;
import dev.huangzutong.mentalhealthsystem.entity.UserRole;
import dev.huangzutong.mentalhealthsystem.entity.req.AddCounselorReq;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.mapper.CounselorMapper;
import dev.huangzutong.mentalhealthsystem.mapper.RoleMapper;
import dev.huangzutong.mentalhealthsystem.mapper.UserRoleMapper;
import dev.huangzutong.mentalhealthsystem.service.ICounselorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 咨询师表 服务实现类
 */
@Service
public class CounselorServiceImpl extends ServiceImpl<CounselorMapper, Counselor> implements ICounselorService {
    @Resource
    private CounselorMapper counselorMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;
    /**
     * 添加咨询师
     *
     * @param req 添加咨询师请求参数
     */
    @Transactional
    @Override
    public void addCounselor(AddCounselorReq req) {
        Counselor counselor = new Counselor();
        BeanUtils.copyProperties(req, counselor);
        counselor.setId(new SnowflakeGenerator().next().toString())
                .setRating(new BigDecimal(5));
        counselorMapper.insert(counselor);
        String role_id = roleMapper.selectOne(new QueryWrapper<Role>().eq("name", "咨询师")).getId();
        userRoleMapper.delete(new QueryWrapper<UserRole>().eq("user_id", counselor.getUserId()));
        userRoleMapper.insert(new UserRole().setUserId(counselor.getUserId()).setRoleId(role_id));
    }

    /**
     * 删除咨询师
     *
     * @param id 咨询师id
     */
    @Transactional
    @Override
    public void removeCounselor(String id) {
        Counselor counselor = getById(id);
        removeById(counselor.getId());
        userRoleMapper.delete(new QueryWrapper<UserRole>().eq("user_id", counselor.getUserId())
                .eq("role_id", getCounselorId())
        );
    }

    /**
     * 更新咨询师信息
     *
     * @param id 咨询师id
     * @param req 更新咨询师请求参数
     */
    @Override
    public void updateCounselor(String id, AddCounselorReq req) {
        Counselor counselor = new Counselor();
        BeanUtils.copyProperties(req, counselor);
        counselor.setId(id);
        updateById(counselor);
    }

    /**
     * 获取咨询师列表
     *
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 姓名关键字
     * @return 咨询师列表
     */
    @Override
    public GetListVO<List<Counselor>> getCounselorList(Long page, Long pageSize, String keyword) {
        QueryWrapper<Counselor> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(keyword), "name", keyword);
        Page<Counselor> pageHelper = new Page<>(page, pageSize);
        counselorMapper.selectPage(pageHelper, queryWrapper);
        GetListVO<List<Counselor>> vo = new GetListVO<List<Counselor>>();
        vo.setPage(pageHelper.getCurrent())
                .setTotal(pageHelper.getTotal())
                .setRecords(pageHelper.getRecords());
        return vo;
    }

    /**
     * 获取咨询师id
     *
     * @return 咨询师id
     */
    private String getCounselorId() {
        for (Role role : roleMapper.selectList(null)) {
            if(role.getName().equals("咨询师")) return role.getId();
        }
        return null;
    }
}
