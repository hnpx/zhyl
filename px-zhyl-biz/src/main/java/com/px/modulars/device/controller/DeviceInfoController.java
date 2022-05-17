
package com.px.modulars.device.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.constant.Constants;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.security.annotation.Inner;
import com.px.modulars.build.entity.BuildingInfo;
import com.px.modulars.build.service.BuildingInfoService;
import com.px.modulars.callLocation.entity.MealEcordTableInfo;
import com.px.modulars.callLocation.entity.SosMessageInfo;
import com.px.modulars.device.entity.HardwareStatus;
import com.px.modulars.device.entity.DeviceInfo;
import com.px.modulars.device.service.DeviceInfoService;
import com.px.basic.alone.core.base.BaseController;
import com.px.common.log.annotation.SysLog;
import com.px.basic.alone.security.util.SecurityUtils;
import com.px.modulars.device.util.GeoUtil;
import com.px.modulars.device.util.SignatureHelper;
import com.px.modulars.device.vo.AccountedEntity;
import com.px.modulars.device.vo.CurveEntity;
import com.px.modulars.meal.service.MealRecordService;
import com.px.modulars.replace.service.ReplaceBuyRecordService;
import com.px.modulars.upms.util.websocket.WebSocketController;
import com.px.modulars.upms.util.websocket.util.WebSocketServer;
import com.px.modulars.userInfo.entity.OldManInfo;
import com.px.modulars.userInfo.service.OldManInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 设备信息
 *
 * @author px code generator
 * @date 2021-12-02 13:34:52
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/device/deviceinfo")
@Api(value = "deviceinfo", tags = "设备信息管理")
public class DeviceInfoController extends BaseController<DeviceInfo, DeviceInfoService> {
    private final OldManInfoService oldManInfoService;
    private final DeviceInfoService deviceInfoService;
    private final MealRecordService mealRecordService;
    private final ReplaceBuyRecordService replaceBuyRecordService;
    private final BuildingInfoService buildingInfoService;
    static int flag = 1;//用来判断文件是否删除成功
    //路径信息
    private static String file = "D:\\测试读取TXT\\";
    static Thread thread;
    private Random random = new Random();
    private String[] lngAndlat = {"109.71624196171763,38.285350295721834", "109.71552347972988,38.284921384355684", "109.71643510222435,38.285044960093536", "109.71580118358133,38.284646816000425", "109.716188,38.284403", "109.715227,38.285164", "109.714944,38.285307", "109.71442856177691,38.285594671725384"};

    /**
     * 分页查询
     *
     * @param page       分页对象
     * @param deviceInfo 设备信息
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('device_deviceinfo_get')")
    public R getDeviceInfoPage(Page page, DeviceInfo deviceInfo) {
        Page deviceInfoPage = deviceInfoService.lambdaQuery()
                .eq(DeviceInfo::getEnable, Constants.ENABLE_TRUE)
                .like(StringUtils.isNotEmpty(deviceInfo.getBindingStatus()), DeviceInfo::getBindingStatus, deviceInfo.getBindingStatus())
                .like(StringUtils.isNotEmpty(deviceInfo.getDeviceStatus()), DeviceInfo::getDeviceStatus, deviceInfo.getDeviceStatus())
                .orderByDesc(DeviceInfo::getCreateTime)
                .page(page);
        return R.ok(deviceInfoPage);
    }

    /**
     * 查询没有绑定设备的老人
     *
     * @return R
     */
    @GetMapping("/selectOldManInfo")
    @ApiOperation("查询没有绑定设备的老人")
    public R selectOldManInfo() {
        return R.ok(deviceInfoService.selectOldManInfo());
    }

    /**
     * 解除绑定
     *
     * @param deviceId
     * @return
     */
    @PostMapping("/contactBinding")
    @ApiOperation("解除绑定")
    public R contactBinding(Integer deviceId) {
        int a = deviceInfoService.contactBinding(deviceId);
        if (a > 0) {
            return R.ok();
        } else {
            return R.failed("解绑失败");
        }
    }

    /**
     * 通过id查询设备信息
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('device_deviceinfo_get')")
    public R getById(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    /**
     * 新增设备信息
     *
     * @param deviceInfo 设备信息
     * @return R
     */
    @ApiOperation(value = "新增设备信息", notes = "新增设备信息")
    @PostMapping
    @SysLog("新增设备信息")
    @PreAuthorize("@pms.hasPermission('device_deviceinfo_add')")
    public R save(@Validated @RequestBody DeviceInfo deviceInfo) {
        //默认添加设备未未绑定
        deviceInfo.setBindingStatus("0");
        //设备的状态需要拿到设备编号与卡号之后去进行校验之后进行状态追加
        //设备编号
        String deviceNumber = deviceInfo.getDeviceNumber();
        DeviceInfo a = deviceInfoService.selectDeviceInfoByDeviceNumber(deviceInfo);
        if (a != null) {
            return R.failed("设备编号已存在");
        } else {
            List<DeviceInfo> selectDeviceInfoById = deviceInfoService.selectDeviceInfoById(deviceInfo);
            if (selectDeviceInfoById.size() > 0) {
                return R.failed("老人已经绑定设备编号：" + selectDeviceInfoById.get(0).getDeviceNumber());
            }
        }
        //请求http，验签，XML
       /* String message = SignatureHelper.postString("https://www.baidu.com/sugrec", "");
        JSONObject temp_json = JSONObject.parseObject(message);
        //signdata请求返回的 值
        String code = temp_json.getString("code");
        if (!code.equals("0")) {
            return R.failed("验签错误");
        } else {
            //验签成功之后进行链接

            //如果链接成功之后进行设备的初始化设备的数据上传间隔配置、中心号码配置、SOS号码配置、GPRS开关、报警模式配置等参数。
        }*/
        //1,设备链接检测，链接成功之后初始化设备Ip

        return super.update(deviceInfo, SecurityUtils.getUser().getId());
    }

    /**
     * 修改设备信息
     *
     * @param deviceInfo 设备信息
     * @return R
     */
    @ApiOperation(value = "修改设备信息", notes = "修改设备信息")
    @PutMapping
    @SysLog("修改设备信息")
    @PreAuthorize("@pms.hasPermission('device_deviceinfo_edit')")
    public R updateById(@RequestBody DeviceInfo deviceInfo) {
        DeviceInfo a = deviceInfoService.selectDeviceInfoByDeviceNumber(deviceInfo);
        if (a != null) {
            if (a.getId() != deviceInfo.getId()) {
                return R.failed("设备编号已存在");
            }
        }
        //先用编辑进行绑定
        if (!String.valueOf(deviceInfo.getBindingId()).equals("") && !String.valueOf(deviceInfo.getBindingId()).equals(null)) {
            deviceInfo.setBindingStatus("0");
        } else {
            deviceInfo.setBindingStatus("1");
        }
        return super.update(deviceInfo, SecurityUtils.getUser().getId());
    }

    /**
     * 通过id删除设备信息
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除设备信息", notes = "通过id删除设备信息")
    @DeleteMapping("/{id}")
    @SysLog("通过id删除设备信息")
    @PreAuthorize("@pms.hasPermission('device_deviceinfo_del')")
    public R removeById(@PathVariable Integer id) {
        DeviceInfo a = deviceInfoService.deviceInfoById(id);
        if (a != null) {
            if (a.getBindingStatus().equals("0")) {
                return R.failed("设备已经绑定老人信息,请先解绑");
            }
        }
        DeviceInfo deviceInfo = super.service.queryById(id);
        if (deviceInfo == null) {
            return R.failed("ID错误");
        }
        return super.del(deviceInfo, SecurityUtils.getUser().getId());
    }

    /**
     * 楼宇信息
     *
     * @return
     */
    @ApiOperation(value = "列表", notes = "列表")
    @GetMapping("/getBuildingInList")
    public R getBuildingInList() {
        List<BuildingInfo> buildingInfoList = buildingInfoService.lambdaQuery()
                .eq(BuildingInfo::getEnable, Constants.ENABLE_TRUE)
                .orderByAsc(BuildingInfo::getCommunity).orderByAsc(BuildingInfo::getBuildingNumber).list();
        buildingInfoList.forEach(buildingInfo -> {
            buildingInfo.setName(buildingInfo.getCommunity() + "-" + buildingInfo.getBuildingNumber());
        });
        return R.ok(buildingInfoList);
    }

    /**
     * 实时定位-楼宇信息-住户
     *
     * @param page        分页对象
     * @param id          老人所在楼宇ID
     * @param nameOrPhone 老人姓名或电话
     * @return
     */
    @ApiOperation(value = "实时定位-楼宇信息-住户", notes = "实时定位-楼宇信息-住户")
    @GetMapping("/geBuildingUserPage")
    public R geBuildingUserPage(Page page, int id, String nameOrPhone) {
        OldManInfo oldManInfo = new OldManInfo();
        //老人所在楼宇
        oldManInfo.setBuilding(id);
        //老人姓名
        oldManInfo.setName(nameOrPhone);
        //老人姓名
        oldManInfo.setPhone(nameOrPhone);
        Page buildingUserPage = oldManInfoService.lambdaQuery()
                .eq(OldManInfo::getEnable, Constants.ENABLE_TRUE)
                .eq(oldManInfo.getBuilding() != null, OldManInfo::getBuilding, oldManInfo.getBuilding())
                .and(StringUtils.isNotEmpty(oldManInfo.getName()), wrapper -> wrapper.like(StringUtils.isNotEmpty(oldManInfo.getName()), OldManInfo::getName, oldManInfo.getName()).or()
                        .like(StringUtils.isNotEmpty(oldManInfo.getPhone()), OldManInfo::getPhone, oldManInfo.getPhone()))
                .page(page);
        return R.ok(buildingUserPage);
    }

    /**
     * 实时定位-楼宇信息-当前所在
     *
     * @param id          楼宇ID
     * @param nameOrPhone 老人姓名或电话
     * @return
     */
    @ApiOperation(value = "实时定位-楼宇信息-当前所在", notes = "实时定位-楼宇信息-当前所在")
    @GetMapping("/geBuildingCurrentUser")
    public R geBuildingCurrentUser(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize, Integer id, String nameOrPhone) {
        //定义一个距离范围
        int gap = 100;
        Map<String, Object> map = new HashMap<>();
        DeviceInfo deviceInfo = new DeviceInfo();
        OldManInfo oldManInfo = new OldManInfo();
        BuildingInfo buildingInfo = new BuildingInfo();
        Page<OldManInfo> selectOldManList = null;
        if (id == null) {
            return R.failed("楼宇ID为空");
        } else {
            buildingInfo.setId(id);
            List<BuildingInfo> selectBuildingList = deviceInfoService.selectBuildingList(buildingInfo);
            if (selectBuildingList.size() <= 0) {
                return R.failed("没有查到此楼宇信息");
            }
            //老人所在楼宇
            oldManInfo.setBuilding(id);
            if (nameOrPhone != null && nameOrPhone != "") {
                //老人姓名
                oldManInfo.setName(nameOrPhone);
                //老人电话号
                oldManInfo.setPhone(nameOrPhone);
            }
            //拿到楼宇内所有的老人信息
            selectOldManList = deviceInfoService.selectOldManList(new Page<OldManInfo>(pageNo, pageSize), oldManInfo);
            for (OldManInfo selectOldList : selectOldManList.getRecords()) {
                double lon = Double.parseDouble("109.715227"); //当前经度
                double lat = Double.parseDouble("38.285164"); //当前纬度
                //设备返回的经纬度
                String ll = lngAndlat[random.nextInt(lngAndlat.length)];
                int index3 = ll.indexOf(",");
                double lon1 = Double.valueOf(ll.substring(0, index3).toString());
                double lat1 = Double.valueOf(ll.substring(index3 + 1).toString());
                double dist;
                dist = GeoUtil.GetDistance(lon, lat, lon1, lat1);
                if (new Double(dist).intValue() >= gap) {
                    selectOldManList.getRecords().remove(selectOldList);
                }
            }

        }
        map.put("total", (int) selectOldManList.getTotal());
        map.put("list", selectOldManList);
        return R.ok(selectOldManList);
    }

    /**
     * 接收通知,监听1（代办代买），2（订餐）,SOS报警
     */
    @PostMapping("/device_test_cellback")
    @ApiOperation(value = "接收通知,监听1（代办代买），2（订餐）", notes = "接收通知,监听1（代办代买），2（订餐）")
    @ResponseBody
    public String device_test_cellback(@RequestBody String message, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String functionCode = jsonObject.getString("functionCode");
        String res = "{  \"content\": " + message + "}";
        //首次验证链接
        if (functionCode.equals("0")) {
            return res;
        } else {
            if (functionCode.equals("1")) {
                //状态
                String hardwareStatus = jsonObject.getString("hardwareStatus");
                //转译成json
                JSONObject hardwareStatusJson = JSONObject.parseObject(hardwareStatus);
                //转化成实体类
                HardwareStatus hardwareStatusJsons = JSON.toJavaObject(hardwareStatusJson, HardwareStatus.class);
                if (Integer.parseInt(hardwareStatusJsons.getVoltameter()) < 10) {
                    //电量过低，更新设备状态--这个只有接收到通知了才知道
                    DeviceInfo deviceInfo = new DeviceInfo();
                    deviceInfo.setBindingStatus("2");
                    deviceInfo.setDeviceNumber(hardwareStatusJsons.getHardwareCode());
                    deviceInfoService.updateDeviceStatus(deviceInfo);
                }
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setDeviceNumber(hardwareStatusJsons.getIsDial());
                DeviceInfo deviceInfoResult = deviceInfoService.selectDeviceInfoByOldId(deviceInfo);
                if (deviceInfoResult != null && deviceInfoResult.getBindingId() != 0) {
                    //监听1，2-假参数
                    if ("1".equals(hardwareStatusJsons.getIsDial())) {
                        replaceBuyRecordService.EquipmentReplaceBuyRecord(deviceInfoResult.getBindingId());
                    }
                    if ("2".equals(hardwareStatusJsons.getIsDial())) {
                        mealRecordService.equipmentMeal(deviceInfoResult.getBindingId());
                    }
                }
                try {
                    //添加SOS信息
                    SosMessageInfo sosMessageInfo = new SosMessageInfo();
                    sosMessageInfo.setOldId(2);
                    sosMessageInfo.setDeviceNumber("123456123");
                    sosMessageInfo.setLatitude("124.123456");
                    sosMessageInfo.setLongitude("23.156541");
                    sosMessageInfo.setCreateBy(12);
                    sosMessageInfo.setEnable(1);
                    int a = deviceInfoService.saveSosMessageInfo(sosMessageInfo);
                } catch (Exception e) {

                }
            }
        }
        return null;
    }

    /**
     * 心跳
     * 定位获取请求，10秒执行一次
     */
    @ApiOperation(value = "定位获取请求，10秒执行一次", notes = "定位获取请求，10秒执行一次")
    public void heartbeat() {
        //经度
        String j = null;
        //纬度
        String w = null;
        //获取一个日期
        String SumBtDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        ExecutorService executors = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(500), new ThreadPoolExecutor.CallerRunsPolicy());
        thread = new Thread(() -> new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //拿到所有设备信息
                List<DeviceInfo> selectDeviceInfo = deviceInfoService.selectDeviceInfo();
                for (DeviceInfo a : selectDeviceInfo) {
                    executors.submit(() -> {
                        //设备编号
                        String deviceNumber = a.getDeviceNumber();
                        //去请求拿到经纬度
                        //请求失败修改设备状态
                        if (false) {
                            //请求失败
                            DeviceInfo deviceInfo = new DeviceInfo();
                            deviceInfo.setDeviceNumber(deviceNumber);
                            DeviceInfo deviceInfoResult = deviceInfoService.selectDeviceInfoByOldId(deviceInfo);
                            if (deviceInfoResult.getDeviceStatus() != "1") {
                                deviceInfo.setBindingStatus("1");
                                deviceInfoService.updateDeviceStatus(deviceInfo);
                            }
                        } else {
                            //请求成功
                            DeviceInfo deviceInfo = new DeviceInfo();
                            deviceInfo.setDeviceNumber(deviceNumber);
                            DeviceInfo deviceInfoResult = deviceInfoService.selectDeviceInfoByOldId(deviceInfo);
                            //状态不异常,不正常,即为异常，直接等于异常的话，与电量有冲突
                            if (deviceInfoResult.getDeviceStatus() == "1" && deviceInfoResult.getDeviceStatus() != "0") {
                                deviceInfo.setBindingStatus("0");
                                deviceInfoService.updateDeviceStatus(deviceInfo);
                            }
                            //写入TXT文件内容
                            outTxtFile(file + SumBtDate, deviceNumber, formatterDate() + "|" + j + "," + w);
                        }

                    });
                }
            }
        }, 0, 10 * 1000));//默认10秒
        thread.start();
    }

    /**
     * 写入txt文件内容
     */
    public static void outTxtFile(String filePath, String filePath2, String shuju) {
        FileWriter fw = null;
        try {
            File f = new File(filePath);// 相对路径，如果没有则要建立一个新的output.txt文件
            if (!f.exists()) {
                f.mkdirs();
            }
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            File writename = new File(filePath + "\\" + filePath2 + ".txt");
            fw = new FileWriter(writename, true);//true,进行追加写。
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(shuju);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹
     */
    //3.添加定时任务,每隔72小时执行一次，也就是三天
    //@Scheduled(cron = "0 0 */72 * * ?")
    public static void delFolder() {
        String folderPath = "D:\\测试读取TXT";
        String folderPatht1 = null;
        try {
            int b = 3;
            for (int i = 1; i <= b; i++) {
                Calendar calendar1 = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                calendar1.add(Calendar.DATE, -i);
                String three_days_ago = sdf1.format(calendar1.getTime());
                folderPatht1 = folderPath + "\\" + three_days_ago;
                delAllFile(folderPatht1); //删除完里面所有内容
                String filePath = folderPatht1;
                filePath = filePath.toString();
                java.io.File myFilePath = new java.io.File(filePath);
                myFilePath.delete(); //删除空文件夹
                folderPatht1 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径 ,"D:\测试读取TXT"
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                flag = true;
            }
        }
        return flag;
    }

    //获取当前时间
    public static String formatterDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }


    /**
     * 首页-数据总览
     *
     * @return
     */
    @GetMapping("/getOverviewMap")
    @ApiOperation("首页数据总览")
    public R getOverviewMap() {
        Map<String, Object> map = deviceInfoService.getOverviewMap();
        return R.ok(map);
    }

    /**
     * 曲线图-订餐数量
     */
    @GetMapping("/getTogetherfor")
    @ApiOperation("曲线图-订餐数量")
    public R getTogetherfor() {
        List<CurveEntity> getTogetherfor = deviceInfoService.getTogetherfor();
        return R.ok(getTogetherfor);
    }

    /**
     * 曲线图-代办代买
     */
    @GetMapping("/getAgents")
    @ApiOperation("曲线图-代办代买")
    public R getAgents() {
        List<CurveEntity> getTogetherfor = deviceInfoService.getAgents();
        return R.ok(getTogetherfor);
    }

    /**
     * 饼状图-楼宇分布
     */
    @GetMapping("/getDistribution")
    @ApiOperation("饼状图-楼宇分布")
    public R getDistribution(Integer timeType) {
        List<AccountedEntity> getDistribution = deviceInfoService.getDistribution(timeType);
        return R.ok(getDistribution);
    }

    /**
     * 饼状图-年龄占比
     */
    @GetMapping("/getAgeInfo")
    @ApiOperation("饼状图-年龄占比")
    public R getAgeInfo(Integer timeType) {
        List<AccountedEntity> getAgeInfo = deviceInfoService.getAgeInfo(timeType);
        return R.ok(getAgeInfo);
    }

    /**
     * 柱状统计-年龄段分析
     */
    @GetMapping("/getAgeInfoz")
    @ApiOperation("柱状统计-年龄段分析")
    public R getAgeInfoz(Integer timeType) {
        List<AccountedEntity> getAgeInfoz = deviceInfoService.getAgeInfoz(timeType);
        return R.ok(getAgeInfoz);
    }

    @GetMapping("/ceshi")
    @Inner(value = false)
    public static void ceshi(String timeType) throws IOException {
        System.out.println(timeType);
        WebSocketServer.BroadCastInfo(timeType);
    }

}
