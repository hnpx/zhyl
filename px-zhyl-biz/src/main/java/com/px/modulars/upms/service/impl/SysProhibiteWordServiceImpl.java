package com.px.modulars.upms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.constant.Constants;
import com.px.basic.alone.core.base.BaseModel;
import com.px.modulars.upms.entity.SysProhibiteWord;
import com.px.modulars.upms.mapper.SysProhibiteWordMapper;
import com.px.modulars.upms.service.SysProhibiteWordService;
import com.px.basic.alone.core.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 关键词过滤
 *
 * @author 吕郭飞
 * @date 2021-06-18 15:34:16
 */
@Service
public class SysProhibiteWordServiceImpl extends BaseServiceImpl<SysProhibiteWord, SysProhibiteWordMapper> implements SysProhibiteWordService {

    @Override
    public List<String> queryByKey(String s) {
        List<SysProhibiteWord> words = super.lambdaQuery().eq(SysProhibiteWord::getSkey, s).eq(BaseModel::getEnable, Constants.ENABLE_TRUE).list();
        List<String> wordStrs = new ArrayList<>();
        for (SysProhibiteWord word : words) {
            wordStrs.add(word.getWord());
        }
        return wordStrs;
    }
}
