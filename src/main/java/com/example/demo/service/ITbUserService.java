package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.TbUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nickle
 * @since 2020-10-08
 */
public interface ITbUserService extends IService<TbUser> {
//    String userLogin(UserDO userDO);

    void userLogout(String token);

    IPage< TbUser> listByPage(Integer currentPage, Integer pageSize,  TbUser condition,
                                    Long startCreateTime,
                                    Long endCreateTime);

    void saveOrUpdateUser( TbUser  TbUser);

    void passwordRevert(Integer userId);

    void validUser(Integer userId);

    void invalidUser(Integer userId);

    Collection<Integer> statisticsUser();

    void deleteUser(Integer userId);
}
