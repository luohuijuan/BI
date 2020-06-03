package com.ysyy.biserver.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.ysyy.biserver.dingding.domain.ServiceResult;
import com.ysyy.biserver.dingding.domain.UserDTO;
import com.ysyy.biserver.dingding.service.TokenService;
import com.ysyy.biserver.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.ysyy.biserver.dingding.config.UrlConstant.URL_GET_USER_INFO;
import static com.ysyy.biserver.dingding.config.UrlConstant.URL_USER_GET;

@CrossOrigin
@RestController
public class LoginControler {
    private static final Logger log = LoggerFactory.getLogger(LoginControler.class);
    public final static Map map = new HashMap();
    static {
        map.put("000001", "all");
        map.put("000002", "all");
        map.put("000097", "all");
        map.put("000104", "all");
        map.put("011542", "index2");
        map.put("011409", "yimeiduo");
        map.put("000050", "money");
        map.put("000052", "money");
        map.put("000087", "index2");
        map.put("000094", "yimeiduo");
        map.put("000092", "yimeiduo");
        map.put("000103", "yimeiduo");
        map.put("000105", "yimeiduo");

    }
    @Autowired
    private TokenService tokenService;

    @Value("${dingtalk.code_login.app_id}")
    private String codeLoginAppId;

    @Value("${dingtalk.code_login.app_secret}")
    private String codeLoginAppSecret;

    @Value("${dingtalk.code_login.access_token.app_key}")
    private String accessTokenAppKey;

    @Value("${dingtalk.code_login.access_token.app_secret}")
    private String accessTokenAppSecret;



    @PostMapping("getTempCode")
    public void getTempCode(String url){
        log.info("url:{}",url);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(url,null);
    }

    @GetMapping("/codeLogin")
    public String codeLogin(String code, String state, HttpServletRequest request){
        log.info("received temp auth code:{},state：{}",code,state);

//        String codeLoginAppId = "dingoam85eav6lbisythmy";
//        String codeLoginAppSecret = "OmkOOb3MFlUFV1zK5_sUjzH-JPj8M94XeBhQXiXBbKefxbiqRl-9ASanLMFZcEdv";

        String callback = request.getParameter("callback");
        log.info("callback string::{}",callback);

        callback = "jsonpCallback";

        Result result;

        try{
            DefaultDingTalkClient  client = new DefaultDingTalkClient("https://oapi.dingtalk.com/sns/getuserinfo_bycode");
            OapiSnsGetuserinfoBycodeRequest req = new OapiSnsGetuserinfoBycodeRequest();
            req.setTmpAuthCode(code);
            OapiSnsGetuserinfoBycodeResponse response = client.execute(req,codeLoginAppId,codeLoginAppSecret);

            if(response.getErrcode() != 0){

                return callback + "(" + JSON.toJSONString(new Result(0,"errorCode:" + response.getErrcode() + " errMsg:" +response.getErrmsg())) + ")";
            }

            //拿到用户信息的unionID
            OapiSnsGetuserinfoBycodeResponse.UserInfo user = response.getUserInfo();

            log.info("current user: {},unionId:{}",user.getNick(),user.getUnionid());


            //获取永久授权码 post   https://oapi.dingtalk.com/sns/get_persistent_code?access_token=ACCESS_TOKEN

            //通过永久授权码获取 用户授权SNS——TOKEN post https://oapi.dingtalk.com/sns/get_sns_token?access_token=ACCESS_TOKEN

            //通过unionID拿到用户ID

            String userId = getUserIdByUnionId(user.getUnionid());

            log.info("userId:{}",userId);

            String jobNumber = getJobNumberByUserId(userId);

            log.info("jobNumber:{}",jobNumber);


            if (!map.containsKey(jobNumber)) {
                return callback + "(" + JSON.toJSONString(new Result(0,"没有权限")) + ")";
            } else {
                return callback + "(" + JSON.toJSONString(new Result(1,(String)map.get(jobNumber))) + ")";
            }


        }catch (Exception e){
            e.printStackTrace();
            return callback + "(" + JSON.toJSONString(new Result(0,e.getMessage())) + ")";
        }

    }

    /**
     * 通过用户ID得到用户的工号
     * @param userId
     * @return
     */
    private String getJobNumberByUserId(String userId) throws Exception{

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
        OapiUserGetRequest request = new OapiUserGetRequest();
        request.setUserid(userId);
        request.setHttpMethod("GET");
        String accessToken = getAccessToken();

        OapiUserGetResponse response = client.execute(request, accessToken);

        if(response != null && response.getErrcode() != 0){
            throw new RuntimeException("errorCode:" + response.getErrcode() + ", errorMsg:" + response.getErrmsg());
        }
        return response.getJobnumber();
    }

    /**
     * 通过unionID获取用户userID
     * @param unionid
     * @return
     */
    private String getUserIdByUnionId(String unionid) throws Exception{
        //获取accessToken

        String accessToken = getAccessToken();

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getUseridByUnionid");
        OapiUserGetUseridByUnionidRequest request = new OapiUserGetUseridByUnionidRequest();
        request.setUnionid(unionid);
        request.setHttpMethod("GET");
        OapiUserGetUseridByUnionidResponse response = client.execute(request, accessToken);

        if(response != null && response.getErrcode() != 0){
            throw new RuntimeException("errorCode: " + response.getErrcode() + " , errorMsg:"+response.getErrmsg());
        }
        return response.getUserid();
    }

    /**
     * 第三方应用获取accessToken
     * @return
     * @throws Exception
     */
    String getAccessToken() throws Exception{
        ServiceResult<String> acctokenRel = tokenService.getAccessToken(accessTokenAppKey, accessTokenAppSecret);
        return acctokenRel.getResult();

    }


    @PostMapping("/getlogin")
    public String GetLogin(@RequestBody Map params) {
        //String retunString = "";
        try {
            JSONObject json = new JSONObject(params);
            String authCode = json.get("authCode").toString();

            String accessToken;

            // 获取accessToken
            ServiceResult<String> accessTokenSr = tokenService.getAccessToken();
            if (!accessTokenSr.isSuccess()) {
                String msg = ServiceResult.failure(accessTokenSr.getCode(), accessTokenSr.getMessage()).getMessage();
                return "{\n" +
                        "    \"error_code\": 0,\n" +
                        "    \"msg\": \"" + msg + "\"\n" +
                        "}";

            }
            accessToken = accessTokenSr.getResult();

            // 获取用户userId
            ServiceResult<String> userIdSr = getUserInfo(accessToken, authCode);
            if (!userIdSr.isSuccess()) {
                String msg = ServiceResult.failure(userIdSr.getCode(), userIdSr.getMessage()).getMessage();
                return "{\n" +
                        "    \"error_code\": 0,\n" +
                        "    \"msg\": \"" + msg + "\"\n" +
                        "}";
            }

            // 获取用户详情
           // String userid = userIdSr.getResult();
            String userid = getUser(accessToken, userIdSr.getResult()).getResult().getJobnumber();
            log.error(userid);

            if (!map.containsKey(userid)) {
                return "{\n" +
                        "    \"error_code\": 0,\n" +
                        "    \"msg\": \"没有权限\"\n" +
                        "}";
            } else {

                return "{\n" +
                        "\t\"error_code\": 1,\n" +
                        "\t\"page\": \"" + map.get(userid) + "\"\n" +
                        "}";
            }
        }
        catch (Exception e){

            return e.getMessage();

        }


    }

    /**
     * 访问/user/getuserinfo接口获取用户userId
     *
     * @param accessToken access_token
     * @param authCode    临时授权码
     * @return 用户userId或错误信息
     */
    private ServiceResult<String> getUserInfo(String accessToken, String authCode) {
        DingTalkClient client = new DefaultDingTalkClient(URL_GET_USER_INFO);
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(authCode);
        request.setHttpMethod("GET");

        OapiUserGetuserinfoResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            log.error("Failed to {}", URL_GET_USER_INFO, e);
            return ServiceResult.failure(e.getErrCode(), "Failed to getUserInfo: " + e.getErrMsg());
        }
        if (!response.isSuccess()) {
            return ServiceResult.failure(response.getErrorCode(), response.getErrmsg());
        }

        return ServiceResult.success(response.getUserid());
    }

    /**
     * 访问/user/get 获取用户名称
     *
     * @param accessToken access_token
     * @param userId      用户userId
     * @return 用户名称或错误信息
     */
    private ServiceResult<UserDTO> getUser(String accessToken, String userId) {
        DingTalkClient client = new DefaultDingTalkClient(URL_USER_GET);
        OapiUserGetRequest request = new OapiUserGetRequest();
        request.setUserid(userId);
        request.setHttpMethod("GET");

        OapiUserGetResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            log.error("Failed to {}", URL_USER_GET, e);
            return ServiceResult.failure(e.getErrCode(), "Failed to getUserName: " + e.getErrMsg());
        }

        UserDTO user = new UserDTO();
        user.setName(response.getName());
        user.setUserid(response.getUserid());
        user.setAvatar(response.getAvatar());
        user.setJobnumber(response.getJobnumber());

        return ServiceResult.success(user);
    }

}
