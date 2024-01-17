package xin.h5.service.invitation.impl;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.common.domain.User;
import xin.h5.service.invitation.InvitationService;
import xin.h5.utils.QRCodeGeneratorUtils;

import java.io.File;
import java.io.IOException;

@Service
public class invitationServiceImpl implements InvitationService {

    @Value("${realityFileDepositPath}")
    private String realityFileDepositPath;

    //返回用户专属的的二维码
    @Override
    public ResponseResult<String> myselfInvitationQRCode() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();
        System.out.println(user);
        String QRPath = realityFileDepositPath + user.getUserName() + ".png";
/*        File file = new File(QRPath);
        if (file.exists()) {
            return new ResponseResult<>(200, "获取成功！", "/api/image/" + user.getUserName() + ".png");
        }*/
        String myselfInvitationUrl = "https://www.rrthb.com/V2/register?invitatio=" + user.getMyInvitationCode();
        try {
            QRCodeGeneratorUtils.generateQRCodeImage(myselfInvitationUrl, QRPath, true);
        } catch (WriterException | IOException e) {
            System.out.println("创建邀请二维码失败！");
            e.printStackTrace();
        }
        return new ResponseResult<>(200, "获取成功！", "/api/image/" + user.getUserName() + ".png");
    }
}
