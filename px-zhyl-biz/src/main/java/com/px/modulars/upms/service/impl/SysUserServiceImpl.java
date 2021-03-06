

package com.px.modulars.upms.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.util.WebUtils;
import com.px.modulars.upms.dto.UserDTO;
import com.px.modulars.upms.dto.UserInfo;
import com.px.modulars.upms.entity.*;
import com.px.modulars.upms.vo.MenuVO;
import com.px.modulars.upms.vo.UserVO;
import com.px.modulars.upms.mapper.SysUserMapper;
import com.px.modulars.upms.service.*;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author pinxun
 * @date 2019/2/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    //	private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    @Autowired
    private PasswordEncoder encoding;
    private final SysMenuService sysMenuService;

    private final SysRoleService sysRoleService;

    private final SysDeptService sysDeptService;

    private final SysUserRoleService sysUserRoleService;

    private final SysPolicySecurityService policySecurityService;

    /**
     * ??????????????????
     *
     * @param userDto DTO ??????
     * @return success/fail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
        sysUser.setPassword(encoding.encode(userDto.getPassword()));
        this.initSecurityInfo(sysUser, true);
//        WebUtils
        baseMapper.insert(sysUser);
        List<SysUserRole> userRoleList = userDto.getRole().stream().map(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            return userRole;
        }).collect(Collectors.toList());
        return sysUserRoleService.saveBatch(userRoleList);
    }

    /**
     * ????????????????????????????????????
     *
     * @param user
     */
    private void initSecurityInfo(SysUser user, boolean saveFlag) {

        SysPolicySecurity policy = this.policySecurityService.readDef();
        if (policy != null) {
            //?????????????????????????????????????????????????????????????????????
            if (policy.getFirstModify() == 1 && saveFlag) {
                user.setPassExpiredTime(LocalDateTime.now());
            } else {
                //??????????????????????????????
                if (policy.getTimingModify() == 1) {
                    user.setPassExpiredTime(LocalDateTime.now().plus(policy.getTimingNum(), ChronoUnit.DAYS));
                }
            }
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param sysUser ??????
     * @return
     */
    @Override
    public UserInfo getUserInfo(SysUser sysUser) {
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        // ?????????????????? ???ID???
        List<Integer> roleIds = sysRoleService.findRolesByUserId(sysUser.getUserId()).stream().map(SysRole::getRoleId)
                .collect(Collectors.toList());
        userInfo.setRoles(ArrayUtil.toArray(roleIds, Integer.class));

        // ?????????????????????menu.permission???
        Set<String> permissions = new HashSet<>();
        roleIds.forEach(roleId -> {
            List<MenuVO> menus = sysMenuService.findMenuByRoleId(roleId);
            List<String> permissionList = menus.stream()
                    .filter(menuVo -> StringUtils.isNotEmpty(menuVo.getPermission())).map(MenuVO::getPermission)
                    .collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        return userInfo;
    }

    @Override
    public SysUser getUserByUsername(String username) {
        SysUser param = new SysUser();
        param.setUsername(username);
        QueryWrapper query = new QueryWrapper(param);
        SysUser user = this.baseMapper.selectOne(query);
        return user;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param page    ????????????
     * @param userDTO ????????????
     * @return
     */
    @Override
    public IPage getUserWithRolePage(Page page, UserDTO userDTO) {
        return baseMapper.getUserVosPage(page, userDTO);
    }

    /**
     * ??????ID??????????????????
     *
     * @param id ??????ID
     * @return ????????????
     */
    @Override
    public UserVO getUserVoById(Integer id) {
        return baseMapper.getUserVoById(id);
    }

    /**
     * ????????????
     *
     * @param sysUser ??????
     * @return Boolean
     */
    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#sysUser.username")
    public Boolean removeUserById(SysUser sysUser) {
        sysUserRoleService.removeRoleByUserId(sysUser.getUserId());
        this.removeById(sysUser.getUserId());
        return Boolean.TRUE;
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public R updateUserInfo(UserDTO userDto) {
        UserVO userVO = baseMapper.getUserVoByUsername(userDto.getUsername());
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userVO.getUserId());
        if (Integer.valueOf(1).equals(userDto.getEditIdea())) {
            if (!encoding.matches(userDto.getPassword(), userVO.getPassword())) {
                return R.failed("??????????????????????????????");
            }
            sysUser.setPassword(encoding.encode(userDto.getNewpassword1()));
            this.initSecurityInfo(sysUser, false);
        } else {
            sysUser.setPhone(userDto.getPhone());
            sysUser.setAvatar(userDto.getAvatar());
        }
        return R.ok(this.updateById(sysUser));
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public Boolean updateUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setUpdateTime(LocalDateTime.now());
        //????????????????????????????????????
        if (StrUtil.isNotBlank(userDto.getPassword())) {
            sysUser.setPassword(encoding.encode(userDto.getPassword()));
        }
        this.updateById(sysUser);

        sysUserRoleService
                .remove(Wrappers.<SysUserRole>update().lambda().eq(SysUserRole::getUserId, userDto.getUserId()));
        userDto.getRole().forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            userRole.insert();
        });
        return Boolean.TRUE;
    }

    /**
     * ?????????????????????????????????
     *
     * @param username ?????????
     * @return R
     */
    @Override
    public List<SysUser> listAncestorUsersByUsername(String username) {
        SysUser sysUser = this.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));

        SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
        if (sysDept == null) {
            return null;
        }

        Integer parentId = sysDept.getParentId();
        return this.list(Wrappers.<SysUser>query().lambda().eq(SysUser::getDeptId, parentId));
    }

    @Override
    public UserInfo readInfo(String username) {
        SysUser param = new SysUser();
        param.setUsername(username);
        QueryWrapper query = new QueryWrapper(param);
        SysUser user = this.baseMapper.selectOne(query);
        UserInfo userInfo = this.getUserInfo(user);
        //??????????????????
        if (user != null && user.getLockTime() != null && user.getLockTime().isAfter(LocalDateTime.now())) {
            user.setLockFlag(CommonConstants.STATUS_LOCK);
        }
        return userInfo;
    }
}
