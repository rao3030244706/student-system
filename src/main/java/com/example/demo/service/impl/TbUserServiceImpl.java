package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.TbUser;
import com.example.demo.mapper.TbUserMapper;
import com.example.demo.service.ITbUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;
//
///**
// * <p>
// *  服务实现类
// * </p>
// *
// * @author nickle
// * @since 2020-10-08
// */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ITbUserService {
    @Override
    public void userLogout(String token) {

    }

    @Override
    public IPage<TbUser> listByPage(Integer currentPage, Integer pageSize, TbUser condition, Long startCreateTime, Long endCreateTime) {
        return null;
    }

    @Override
    public void saveOrUpdateUser(TbUser TbUser) {

    }

    @Override
    public void passwordRevert(Integer userId) {

    }

    @Override
    public void validUser(Integer userId) {

    }

    @Override
    public void invalidUser(Integer userId) {

    }

    @Override
    public Collection<Integer> statisticsUser() {
        return null;
    }

    @Override
    public void deleteUser(Integer userId) {

    }
//
//    @Override
//    public String userLogin(UserDO userDO) {
//        return null;
//    }
//
//    @Override
//    public void userLogout(String token) {
//
//    }
//
//    @Override
//    public IPage<TbUser> listByPage(Integer currentPage, Integer pageSize, TbUser condition, Long startCreateTime, Long endCreateTime) {
//        return null;
//    }
//
//    @Override
//    public void saveOrUpdateUser(TbUser TbUser) {
//
//    }
//
//    @Override
//    public void passwordRevert(Integer userId) {
//
//    }
//
//    @Override
//    public void validUser(Integer userId) {
//
//    }
//
//    @Override
//    public void invalidUser(Integer userId) {
//
//    }
//
//    @Override
//    public Collection<Integer> statisticsUser() {
//        return null;
//    }
//
//    @Override
//    public void deleteUser(Integer userId) {
//
//    }
}
