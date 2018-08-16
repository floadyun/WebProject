package com.base.lib.http;

public class ApiHelper {
    //正式环境
//    public static final String BASE_URL = "http://www.xitouwang.com/";
//    测试域名
    public static final String BASE_URL = "http://q1.xitouwang.com/";

    public static final String API_URL =BASE_URL+"api/";
    //请求成功码
    public static final int SUCCESS_CODE = 200;
    //登录失效
    public static final int HTTP_401 = 401;
    //请求信息不完整
    public static final int HTTP_422 = 422;
    //请求信息完整，执行结果错误
    public static final int HTTP_423 = 423;
    //新手指引
    public static final String NEWR_GUIDE_URL = BASE_URL+"noviceGuidance";
    //安全保障
    public static final String SECURITY_PROTECTION = BASE_URL+"safetyGuarantee";
    //喜投网投资咨询与服务协议
    public static final String XTW_PROTOCOL_1 = BASE_URL+"protocol/consulting";
    //喜投网网站服务协议
    public static final String XTW_PROTOCOL_2 = BASE_URL+"protocol/webService";
    //喜利多服务协议书
    public static final String XLD_PROTOCOL = BASE_URL+"tenderProtocol/planPlus";
    //喜优选服务授权委托书
    public static final String XYX_PROTOCOL = BASE_URL+"tenderProtocol/planAuto";
    //签到
    public static final String SIGN_URL = BASE_URL+"user/assetOverview#page-sign";
    //发现页
    public static final String FIND_URL = BASE_URL+"find";
    //风险测评
    public static final String RISK_EVALUATION_URL = BASE_URL+"user/avatarUpload#page-safe-riskLevel";
    //帮助中心
    public static final String HELP_CENTER_URL = BASE_URL+"helpPage";
    //邀请好友
    public static final String USER_INVITE_URL = BASE_URL+"user/inviteUser";
    //提现帮助
    public static final String WITHDRAW_HELP_URL = BASE_URL+"account/withdraw#page-cash-help";
    //列表分页请求数
    public static final String PAGE_LIMIT_NUMBER = "10";
}
