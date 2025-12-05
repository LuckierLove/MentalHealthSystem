package dev.huangzutong.mentalhealthsystem.service;

import dev.huangzutong.mentalhealthsystem.common.Result;
import dev.huangzutong.mentalhealthsystem.entity.Favourite;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.huangzutong.mentalhealthsystem.entity.vo.GetListVO;

import java.util.List;

/**
 * 收藏表 服务类
 */
public interface IFavouriteService extends IService<Favourite> {

    /**
     * 创建收藏
     *
     * @param favourite 创建收藏参数
     */
    void createFavourite(Favourite favourite);

    /**
     * 获取收藏列表
     *
     * @param page       页码
     * @param pageSize   页大小
     * @param studentId  学生ID
     * @return 收藏列表
     */
    GetListVO<List<Favourite>> list(Integer page, Integer pageSize, String studentId);
}
