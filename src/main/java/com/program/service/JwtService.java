package com.program.service;

import javax.servlet.http.HttpServletRequest;

public interface JwtService {

    String extractBearerToken(HttpServletRequest request);

}
