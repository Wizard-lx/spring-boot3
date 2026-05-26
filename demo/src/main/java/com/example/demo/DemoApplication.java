package com.example.demo;

import cn.dev33.satoken.SaManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/index")

public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());
    }

//   @GetMapping
//    public String index(){
//        return "GET无参请求Api方法已经实现";
//   }
   @GetMapping("/{id}")
    public String index(@PathVariable Long id){
       System.out.printf("ID=%s\n", id);
       return "GET Restful请求传值的方法实现成功";
   }
   @GetMapping
    public String INDEX2(@RequestParam Long id, @RequestParam String name){
       System.out.printf("ID=%s,name=%s\n",id,name);
       return "GET 普通请求传值方法已经实现";
   }

   //post请求
   @PostMapping
    public String save(@RequestBody Map<String,String> map){
       System.out.printf(map.toString());
       return "POST请求接收成功";
   }

   //put请求
    @PutMapping("/{id}")
    public String updata(@PathVariable Long id, @RequestBody Map<String,String> map){
        System.out.printf("ID=%s,name=%s\n",id,map);
        return "PUT请求接收成功";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        System.out.printf("ID=%s\n",id);
        return "DELETE请求接收成功";
    }
}
