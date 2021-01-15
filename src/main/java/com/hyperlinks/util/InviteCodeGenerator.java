package com.hyperlinks.util;

import java.util.concurrent.ThreadLocalRandom;

public class InviteCodeGenerator {

    /**
     * Returns an invite code that consists of an input {@link String} and a randomly generated {@code int} value
     *
     * @param   username            The username {@link String} the random code gets appended to
     * @param   inviteCodeStrength  The length of the random code, must be between 0 and 9
     * @return  A {@link String} consisting of username and appended random code
     * @throws  IllegalArgumentException If {@code inviteCodeStrength} is more than 9 or less than 0
     */
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
