package com.example.demo;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.util.RedisUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;

    @Test
    void contextLoads() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.isTrue(5 == userList.size(), "");
        userList.forEach(System.out::println);
    }
    @Test
    void contextService(){
        List<User> userList = userService.list();
        userList.forEach(System.out::println);
    }
    @Test
    void contextRedis(){
        List<User> userList = userService.list();
        redisUtil.set("userList",userList);
    }
}
