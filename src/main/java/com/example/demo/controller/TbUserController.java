package com.example.demo.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.entity.TbUser;
import com.example.demo.pojo.CommonResponseVO;
import com.example.demo.pojo.PageVO;
import com.example.demo.service.ITbUserService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author nickle
 * @since 2020-10-08
 */
@RestController
@RequestMapping("/tb-user")
public class TbUserController {
    @Autowired
    private ITbUserService userService;

   /* @RequestMapping("/login")
    public CommonResponseVO login(@Validated @RequestBody UserDO userDO) {
        String token = userService.userLogin(userDO);
//        roles: ['admin'],
//        introduction: 'I am a super administrator',
//        avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
//        name: 'Super Admin'
        HashMap<String, Object> hashMap = Maps.newHashMap();
        hashMap.put("roles", Arrays.asList("admin"));
        hashMap.put("introduction", "I am a super administrator");
        hashMap.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        hashMap.put("name", "Super Admin");
        hashMap.put("token", token);
        return CommonResponseVO.success(CommonResponseVO.SUCCESS_STATUS, hashMap);
    }

    @RequestMapping("/logout")
    public CommonResponseVO logout(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("X-Token");
        userService.userLogout(token);
        return CommonResponseVO.SUCCESS;
    }

    @RequestMapping("/userList")
    public CommonResponseVO userList(@NotNull @Min(1) Integer currentPage
            , @NotNull @Min(1) @Max(50) Integer pageSize,  TbUser condition,
                                     Long startCreateTime,
                                     Long endCreateTime) {
        IPage<TbUser> userIPage = userService.listByPage(currentPage, pageSize
                , condition, startCreateTime, endCreateTime);
        UserVO userVO = new UserVO();
        PageVO pageVO = new PageVO();
        pageVO.setCurrentPage(userIPage.getCurrent());
        pageVO.setPageSize(userIPage.getSize());
        pageVO.setTotal(userIPage.getTotal());
        userVO.setPageInfo(pageVO);
//        userVO.setUserList(userIPage.getRecords());
        return CommonResponseVO.success(userVO);
    }

    @RequestMapping("/addUser")
    public CommonResponseVO addUser(@Validated @RequestBody  TbUser  TbUser) throws Exception {
        userService.saveOrUpdateUser( TbUser);
        return CommonResponseVO.SUCCESS;
    }

    @RequestMapping("/passwordRevert")
    public CommonResponseVO passwordRevert(@NotNull Integer userId) throws Exception {
        userService.passwordRevert(userId);
        return CommonResponseVO.SUCCESS;
    }

    @RequestMapping("/validUser")
    public CommonResponseVO validUser(@NotNull Integer userId) throws Exception {
        userService.validUser(userId);
        return CommonResponseVO.SUCCESS;
    }

    @RequestMapping("/invalidUser")
    public CommonResponseVO invalidUser(@NotNull Integer userId) throws Exception {
        userService.invalidUser(userId);
        return CommonResponseVO.SUCCESS;
    }

    @RequestMapping("/deleteUser")
    public CommonResponseVO deleteUser(@NotNull Integer userId) throws Exception {
        userService.deleteUser(userId);
        return CommonResponseVO.SUCCESS;
    }

    @RequestMapping("/getUserCount")
    public CommonResponseVO getUserCount() {
        return CommonResponseVO.success(userService.count());
    }

    *//**
     * 统计最近七天的数据
     *
     * @return
     *//*
    @RequestMapping("/statisticsUser")
    public CommonResponseVO statisticsUser() {
        return CommonResponseVO.success(userService.statisticsUser());
        }*/
}

