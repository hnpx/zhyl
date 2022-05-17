package com.px.modulars.upms.dto;

import com.px.basic.alone.security.dto.AbsSysUser;
import com.px.basic.alone.security.dto.AbsUserInfo;
import com.px.modulars.upms.entity.SysUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pinxun
 * @date 2019/2/1
 * <p>
 * commit('SET_ROLES', data) commit('SET_NAME', data) commit('SET_AVATAR', data)
 * commit('SET_INTRODUCTION', data) commit('SET_PERMISSIONS', data)
 */
@Data
public class UserInfo extends AbsUserInfo implements Serializable {

    /**
     * 用户基本信息
     */
    private SysUser sysUser;

    /**
     * 权限标识集合
     */
    private String[] permissions;

    /**
     * 角色集合
     */
    private Integer[] roles;

    @Override
    public AbsSysUser getAbsSysUser() {
        return this.getSysUser();
    }
}
