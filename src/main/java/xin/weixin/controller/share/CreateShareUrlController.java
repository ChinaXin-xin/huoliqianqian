package xin.weixin.controller.share;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.common.domain.User;
import xin.weixin.domain.share.CreateShareUrl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建微信小程序创建的二维码
 */
@RestController
@RequestMapping("/pro/poster")
public class CreateShareUrlController {

    @Value("${wxpay.appId}")
    private String appId;//小程序id

    @Value("${wxpay.appSecret}")
    private String appKey;//小程序密钥

    @Value("${realityFileDepositPath}")
    private String baseImgPath;

    /**
     * 接口调用凭证 access_token
     */
    public String postToken(String appId, String appKey) throws Exception {

        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appKey;
        URL url = new URL(requestUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes("");
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 定义 BufferedReader 输入流来读取 URL 的响应
        BufferedReader in;
        if (requestUrl.contains("nlp"))
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
        else
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        StringBuilder result = new StringBuilder();
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result.append(getLine);
        }
        in.close();
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        return jsonObject.getString("access_token");
    }

    /**
     * 生成微信小程序二维码
     *
     * @param filePath    本地生成二维码路径
     * @param page        当前小程序相对页面 必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index, 根路径前不要填加 /, 不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面
     * @param scene       最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
     * @param accessToken 接口调用凭证
     */
    public void generateQrCode(String filePath, String page, String scene, String accessToken) {

        try {
            //调用微信接口生成二维码
            URL url = new URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            JSONObject paramJson = new JSONObject();
            //这就是你二维码里携带的参数 String型  名称不可变
            paramJson.put("scene", scene);
            //注意该接口传入的是page而不是path
            paramJson.put("page", page);
            //这是设置扫描二维码后跳转的页面
            paramJson.put("width", 200);
            paramJson.put("is_hyaline", true);
            paramJson.put("auto_color", true);
            printWriter.write(paramJson.toString());
            // flush输出流的缓冲
            printWriter.flush();

            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            OutputStream os = new FileOutputStream(new File(filePath));
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                os.write(arr, 0, len);
                os.flush();
            }
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("打开地址查看生成的二维码：" + filePath);
    }

    @PostMapping("/createShareImg")
    public ResponseResult createShareImg(@RequestBody CreateShareUrl createShareUrl) {

        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        String token = null;
        try {
            token = postToken(appId, appKey);
        } catch (Exception e) {
            return new ResponseResult(400, "获取失败，token错误！");
        }
        ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");

        // 文件名
        String imgName = dateFormat.format(new Date()) + curUser.getMyInvitationCode() + ".png";

        // 设置分享图片的二维码在本地的全路径
        String imgPath = baseImgPath + imgName;

        StringBuilder params = new StringBuilder();
        String cid = createShareUrl.getCid().toString();
        String invitationCode = curUser.getMyInvitationCode();

        if (!cid.isEmpty()) {
            params.append("cid=").append(cid);
        }

        if (invitationCode != null && !invitationCode.isEmpty()) {
            // 如果已经有参数被添加，添加一个 & 来分隔参数
            if (params.length() > 0) {
                params.append("&");
            }
            params.append("invitationCode=").append(invitationCode);
        }

        String param = params.toString();


        //生成二维码
        generateQrCode(imgPath, createShareUrl.getShareUrl(), param, token);

        // url在线地址
        String onlineAddress = "/api/image/" + imgName;
        return new ResponseResult(200, "创建成功！", onlineAddress);
    }
}
