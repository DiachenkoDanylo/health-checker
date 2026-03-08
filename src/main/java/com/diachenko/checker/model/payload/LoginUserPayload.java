package com.diachenko.checker.model.payload;

/*  health-checker
    19.02.2026
    @author DiachenkoDanylo
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginUserPayload {

    private String username;
    private String password;

    @Override
    public String toString() {
        return "LoginUserPayload{" +
                "username='" + username + '\'' +
                ", password='" + "****" + '\'' +
                '}';
    }
}
