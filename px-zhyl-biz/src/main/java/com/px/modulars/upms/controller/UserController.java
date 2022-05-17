

package com.px.modulars.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.basic.alone.security.annotation.Inner;
import com.px.basic.alone.security.util.SecurityUtils;
import com.px.modulars.upms.dto.UserDTO;
import com.px.modulars.upms.entity.SysUser;
import com.px.modulars.upms.service.SysUserService;
import com.pig4cloud.pig.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author pinxun
 * @date 2019/2/1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Api(value = "user", tags = "用户管理模块")
public class UserController {

    private final SysUserService userService;
    @Autowired
    private PasswordEncoder encoding;

    /**
     * 获取当前用户全部信息
     *
     * @return 用户信息
     */
    @GetMapping(value = {"/info"})
    @ApiOperation(value = "获取当前用户全部信息",notes = "获取当前用户全部信息")
    public R info() {
        String username = SecurityUtils.getUser().getUsername();
        SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (user == null) {
            return R.failed("获取当前用户信息失败");
        }
        return R.ok(userService.getUserInfo(user));
    }

    /**
     * 获取指定用户全部信息
     *
     * @return 用户信息
     */
    @Inner
    @GetMapping("/info/{username}")
    @ApiOperation(value = "获取指定用户全部信息",notes = "获取指定用户全部信息")
    public R info(@PathVariable String username) {
        SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (user == null) {
            return R.failed(String.format("用户信息为空 %s", username));
        }
        return R.ok(userService.getUserInfo(user));
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "通过ID查询用户信息",notes = "通过ID查询用户信息")
    public R user(@PathVariable Integer id) {
        return R.ok(userService.getUserVoById(id));
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return
     */
    @GetMapping("/details/{username}")
    @ApiOperation(value = "根据用户名查询用户信息",notes = "根据用户名查询用户信息")
    public R user(@PathVariable String username) {
        SysUser condition = new SysUser();
        condition.setUsername(username);
        return R.ok(userService.getOne(new QueryWrapper<>(condition)));
    }

    /**
     * 删除用户信息
     *
     * @param id ID
     * @return R
     */
//	@SysLog("删除用户信息")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户信息",notes = "删除用户信息")
    @PreAuthorize("@pms.hasPermission('sys_user_del')")
    public R userDel(@PathVariable Integer id) {
        SysUser sysUser = userService.getById(id);
        return R.ok(userService.removeUserById(sysUser));
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
//	@SysLog("添加用户")
    @PostMapping
    @ApiOperation(value = "添加用户",notes = "添加用户")
    @PreAuthorize("@pms.hasPermission('sys_user_add')")
    public R user(@RequestBody UserDTO userDto) {
        return R.ok(userService.saveUser(userDto));
    }


    @GetMapping("/getPassWordEncoding")
    @Inner(value = false)
    public String getPassWordEncoding(String password) {
        return encoding.encode(password);
    }

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return R
     */
//	@SysLog("更新用户信息")
    @PutMapping
    @ApiOperation(value = "更新用户信息",notes = "更新用户信息")
    @PreAuthorize("@pms.hasPermission('sys_user_edit')")
    public R updateUser(@Valid @RequestBody UserDTO userDto) {
        return R.ok(userService.updateUser(userDto));
    }

    /**
     * 分页查询用户
     *
     * @param page    参数集
     * @param userDTO 查询参数列表
     * @return 用户集合
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询用户",notes = "分页查询用户")
    public R getUserPage(Page page, UserDTO userDTO) {
        return R.ok(userService.getUserWithRolePage(page, userDTO));
    }

    /**
     * 修改个人信息
     *
     * @param userDto userDto
     * @return success/false
     */
//	@SysLog("修改个人信息")
    @PutMapping("/edit")
    @ApiOperation(value = "修改个人信息",notes = "修改个人信息")
    public R updateUserInfo(@Valid @RequestBody UserDTO userDto) {
        return userService.updateUserInfo(userDto);
    }

    /**
     * @param username 用户名称
     * @return 上级部门用户列表
     */
    @GetMapping("/ancestor/{username}")
    @ApiOperation(value = "上级部门用户列表",notes = "上级部门用户列表")
    public R listAncestorUsers(@PathVariable String username) {
        return R.ok(userService.listAncestorUsersByUsername(username));
    }

}
