package xin.admin.utils;

import java.util.Random;

public class MyStringUtils {

    /**
     * 用户生成八位数的邀请码
     * @return
     */
    public static String generateInvitationCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder invitationCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            invitationCode.append(characters.charAt(index));
        }

        return invitationCode.toString();
    }
}
