package com.px.modulars.meal.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.px.modulars.meal.entity.MealInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class MealRecordVo {

    @Excel(name = "配餐编号",width =12)
    private String mealNumber;
    @Excel(name = "楼宇信息",width =25)
    private String buildingInfoName;
    @Excel(name = "老人名字",width =12)
    private String name;
    @Excel(name = "联系方式",width =12)
    private String phone;
    @Excel(name = "完成状态",width =12)
    private String stateName;
    @Excel(name = "创建时间",width =12)
    private String time;
}
