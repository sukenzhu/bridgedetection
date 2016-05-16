package com.suken.bridgedetection;

public enum RequestType {

    login("登陆", "/m/login.ht"), exit("退出", "/m/logout.ht"), update("更新", "/app/app_upgrade.txt"), gxlxInfo("管辖路线信息",
            "/m/base/getLuXianByUID.ht"), qlBaseData("桥梁基础数据", "/m/base/getBridgeByUID.ht"), hdBaseData("涵洞基础数据",
            "/m/base/getCulvertByUID.ht"), sdBaseData("隧道基础数据", "/m/base/getTunnelByUID.ht"), qhyhzrInfo("桥涵养护责任信息",
            "/m/base/getEngineerBrgByUID.ht"), sdyhzrInfo("隧道养护责任信息", "/m/base/getEngineerTunByUID.ht"), ywzddmInfo("业务字典代码信息",
            "/m/base/getDicInfo.ht"), lastqhjcInfo("上次桥涵检查信息", "/m/check/ofencheck/getPreBrgByUID.ht"), lastsdjcInfo(
            "上次隧道检查信息", "/m/check/ofencheck/getPreTunByUID.ht"), lastsdxcinfo("上次隧道巡查信息", "/m/check/inspect/getPreTunByUID.ht"), updateqhjcInfo("上传桥涵检查信息",
            "/m/check/ofencheck/uploadBrg.ht"), updateqhxcInfo("上传桥涵巡查日志信息",
            "/m/check/inspect/uploadBrg.ht"), updatesdjcInfo("上传隧道检查信息",
            "/m/check/ofencheck/uploadTun.ht"), updatesdxcInfo("上传隧道巡查信息",
            "/m/check/inspect/uploadTun.ht"), updateGpsgjInfo("上传GPS轨迹信息",
            "/m/gps/subGPSDatas.ht"), uploadFile("上传附件",
            "/m/file/upload.ht"), updateGps("更新gps",
            "/m/base/updateGPS.ht"), syncData("同步基础数据", "/m/base/synBaseData.ht");

    private String desc;
    private String url;

    private RequestType(String desc, String url) {
        this.desc = desc;
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

}
