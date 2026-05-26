package com.example.demo.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    //新增用户
    @PostMapping
    public Result save(@RequestBody User user){
        userService.save(user);
        return Result.success();
    }

    //修改用户
    @PutMapping
    public Result updata(@RequestBody User user){
        userService.updateById(user);
        return Result.success();
    }

    //查询单人记录
    @GetMapping("/{id}")
    public Result getOne(@PathVariable Long id){
        return Result.success(userService.getById(id));
    }

    //查询所有用户
    @GetMapping
    @SaCheckPermission("user.list")
    public Result list(){
        return Result.success(userService.list());
    }

    //删除单个用户
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        userService.removeById(id);
        return Result.success();
    }

    //用户分页
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "1")Integer pageNum,
                           @RequestParam (defaultValue = "10")Integer pageSize,
                           @RequestParam(defaultValue = "")String name){
        LambdaQueryWrapper<User>queryWrapper = new LambdaQueryWrapper<>();
        if(!"".equals(name)){
            queryWrapper.like(User::getName, name);
        }

        return Result.success(
                userService.page(new Page<>(pageNum,pageSize),queryWrapper)
        );
    }
    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    // 登录接口
    @RequestMapping("doLogin")
    public Result doLogin() {
        // 第1步，先登录上
        StpUtil.login(10001);
        // 第2步，获取 Token  相关参数
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 第3步，返回给前端
        return Result.success(tokenInfo);
    }


    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

}
