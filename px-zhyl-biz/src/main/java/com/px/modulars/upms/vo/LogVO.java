

package com.px.modulars.upms.vo;

import com.px.modulars.upms.entity.SysLog;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pinxun
 * @date 2019/2/1
 */
@Data
public class LogVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private SysLog sysLog;

	private String username;

}
