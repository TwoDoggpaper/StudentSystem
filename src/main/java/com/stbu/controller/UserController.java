package com.stbu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stbu.entities.UserEntity;
import com.stbu.service.UserService;
import com.stbu.utils.RestResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/getById/{id}")
    public RestResponse<UserEntity> getUserById(@PathVariable int id){
        UserEntity user = userService.getById(id);
        if(user == null){
            return RestResponse.fail("该用户不存在");
        } else {
            return RestResponse.ok(user);
        }
    }

    @RequestMapping("/getByName")
    public RestResponse getUserByNameAndPassword(@RequestParam("username")String username,
                                                 @RequestParam("password")String password){
        if (username == null || password == null){
            return RestResponse.fail("用户名或密码为空");
        }else {
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",username);
            queryWrapper.eq("password",password);
            UserEntity user= userService.getOne(queryWrapper);
            if (user==null){
                return RestResponse.fail("该用户不存在");
            }else {
                return RestResponse.ok(user);
            }
        }
    }

    // 查询所有用户接口
    @GetMapping("/getAll")
    public RestResponse<List<UserEntity>> getAllUsers() {
        List<UserEntity> userList = userService.list();
        if (userList.isEmpty()) {
            return RestResponse.fail("暂无用户数据");
        }
        return RestResponse.ok(userList);
    }

    // 新增用户接口
    @PostMapping("/add")
    public RestResponse<UserEntity> addUser(@RequestBody UserEntity userEntity) {
        // 参数校验
        if (userEntity == null) {
            return RestResponse.fail("用户信息不能为空");
        }
        if (userEntity.getUsername() == null || userEntity.getUsername().trim().isEmpty()) {
            return RestResponse.fail("用户名不能为空");
        }
        if (userEntity.getPassword() == null || userEntity.getPassword().trim().isEmpty()) {
            return RestResponse.fail("密码不能为空");
        }

        // 校验用户名是否已存在
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userEntity.getUsername().trim());
        if (userService.count(queryWrapper) > 0) {
            return RestResponse.fail("用户名已存在，无法新增");
        }

        // 新增用户
        boolean saveSuccess = userService.save(userEntity);
        if (saveSuccess) {
            return RestResponse.ok(userEntity);
        } else {
            return RestResponse.fail("新增用户失败");
        }
    }
    //修改用户
    @PutMapping("/update")
    public RestResponse updateUser(@RequestBody UserEntity userEntity) {
        if (userEntity.getUid() == null) {
            return RestResponse.fail("用户ID不能为空");
        }
        boolean updateSuccess = userService.updateById(userEntity);
        if (updateSuccess) {
            return RestResponse.ok("修改成功");
        } else {
            return RestResponse.fail("修改失败");
        }
    }
    //删除用户
    @DeleteMapping("/delete/{id}")
    public RestResponse deleteUser(@PathVariable Integer id) {
        if (id == null) {
            return RestResponse.fail("用户ID不能为空");
        }
        boolean deleteSuccess = userService.removeById(id);
        if (deleteSuccess) {
            return RestResponse.ok("删除成功");
        } else {
            return RestResponse.fail("删除失败");
        }
    }
}
//新增 POST /user/add
//删除 DELETE /user/delete/1
//修改 PUT /user/update
//查询单个 GET /user/getById/1
//查询所有 GET /user/getAll
//登录查询 GET /user/getByName