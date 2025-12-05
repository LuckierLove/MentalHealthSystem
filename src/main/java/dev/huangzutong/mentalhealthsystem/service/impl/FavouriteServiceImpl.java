package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.Favourite;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;
import dev.huangzutong.mentalhealthsystem.mapper.FavouriteMapper;
import dev.huangzutong.mentalhealthsystem.service.IFavouriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收藏表 服务实现类
 */
@Service
public class FavouriteServiceImpl extends ServiceImpl<FavouriteMapper, Favourite> implements IFavouriteService {

    private final FavouriteMapper favouriteMapper;

    public FavouriteServiceImpl(FavouriteMapper favouriteMapper) {
        this.favouriteMapper = favouriteMapper;
    }

    /**
     * 创建收藏
     *
     * @param favourite 创建收藏参数
     */
    @Override
    public void createFavourite(Favourite favourite) {
        favourite.setStudentId(StpUtil.getLoginId().toString());
        save(favourite);
    }

    /**
     * 获取收藏列表
     *
     * @param page       页码
     * @param pageSize   页大小
     * @param studentId  学生ID
     * @return 收藏列表
     */
    @Override
    public GetListVO<List<Favourite>> list(Integer page, Integer pageSize, String studentId) {
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(studentId),"student_id", studentId);
        Page<Favourite> pageHelper = new Page<>(page, pageSize);
        favouriteMapper.selectPage(pageHelper, queryWrapper);
        return new GetListVO<>(pageHelper.getTotal(), pageHelper.getCurrent(), pageHelper.getRecords());
    }
}
