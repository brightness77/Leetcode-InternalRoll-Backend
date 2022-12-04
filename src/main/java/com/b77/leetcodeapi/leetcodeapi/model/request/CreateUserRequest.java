package com.b77.leetcodeapi.leetcodeapi.model.request;

import lombok.Data;

@Data
public class CreateUserRequest {

    String username;

    String password;

    String email;

    String clientHost;

}
