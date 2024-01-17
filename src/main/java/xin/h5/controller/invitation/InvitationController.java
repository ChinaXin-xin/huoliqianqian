package xin.h5.controller.invitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.h5.service.invitation.InvitationService;

@RestController
@RequestMapping("/h5/invitationController")
public class InvitationController {

    @Autowired
    InvitationService invitationService;

    @PostMapping("/getMyselfQR")
    public ResponseResult<String> myselfInvitationQRCode() {
        return invitationService.myselfInvitationQRCode();
    }
}
