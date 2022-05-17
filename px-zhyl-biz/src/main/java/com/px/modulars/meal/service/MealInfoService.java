package com.px.modulars.meal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.basic.alone.core.base.BaseService;
import com.px.modulars.meal.entity.MealInfo;

import java.util.List;

/**
 * 配餐计划信息
 *
 * @author liupan
 * @date 2021-11-25 15:54:29
 */
public interface MealInfoService extends IService<MealInfo>, BaseService<MealInfo> {


   public List<MealInfo> getMealInfoList();
}
