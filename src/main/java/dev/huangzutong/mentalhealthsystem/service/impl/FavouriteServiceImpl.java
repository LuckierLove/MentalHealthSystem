package dev.huangzutong.mentalhealthsystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import dev.huangzutong.mentalhealthsystem.entity.Favourite;
import dev.huangzutong.mentalhealthsystem.mapper.FavouriteMapper;
import dev.huangzutong.mentalhealthsystem.service.IFavouriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 收藏表 服务实现类
 */
@Service
public class FavouriteServiceImpl extends ServiceImpl<FavouriteMapper, Favourite> implements IFavouriteService {

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
}
