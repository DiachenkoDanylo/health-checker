package com.diachenko.checker.model.payload;

/*  health-checker
    19.02.2026
    @author DiachenkoDanylo
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserPayload {

    private String username;
    private String password;
    private Set<String> authorities = new HashSet<>();

}
