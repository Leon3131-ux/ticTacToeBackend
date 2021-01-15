package com.hyperlinks.util;

import java.util.concurrent.ThreadLocalRandom;

public class InviteCodeGenerator {

    public String generateInviteCode(String username, int inviteCodeStrength) {
        if(inviteCodeStrength == 0){
            return username;
        }else if(inviteCodeStrength > 9){
            throw new IllegalArgumentException();
        }
        int minimum = (int)Math.pow(10, inviteCodeStrength - 1);
        int maximum  = (int)Math.pow(10, inviteCodeStrength);
        int randomInt = ThreadLocalRandom.current().nextInt(minimum, maximum);
        return username + randomInt;
    }

}
