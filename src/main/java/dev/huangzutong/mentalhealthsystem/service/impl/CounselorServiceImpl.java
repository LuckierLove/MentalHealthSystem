package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dev.huangzutong.mentalhealthsystem.entity.Counselor;
import dev.huangzutong.mentalhealthsystem.entity.Role;
import dev.huangzutong.mentalhealthsystem.entity.UserRole;
import dev.huangzutong.mentalhealthsystem.entity.req.AddCounselorReq;
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
        userRoleMapper.insert(new UserRole().setUserId(counselor.getUserId()).setRoleId(role_id));
    }
}
