package com.immrwk.myworkspace.api;

/**
 * Created by Administrator on 2016/5/15 0015.
 */
public class FuwaiAPI {

    public static final String BaseUrl = "http://106.120.203.85:8088/";

    /**
     * 检查更新  输入 参数version_code
     * 更新内容-content 更新地址-apk 更新版本号-size   获取失败返回[{"success":"false","msg":"暂无更新内容!"}]
     * 示例 http://106.120.203.85:8088/businessManage/versions/CGetVersions.do?version_code=3.0
     */
    public static final String UpdateVersionUrl = BaseUrl + "businessManage/versions/CGetVersions.do?version_code=";

}
