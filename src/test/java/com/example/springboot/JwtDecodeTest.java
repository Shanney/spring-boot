package com.example.springboot;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.Charset;


public class JwtDecodeTest {

    @Test
    void test(){
        String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJjcmVkZW50aWFsVHlwZSI6IlVzZXJuYW1lUGFzc3dvcmRDcmVkZW50aWFsIiwiYXVkIjoiaHR0cDpcL1wvMTcyLjE2OC4xNTkuMjMzOjgwODBcL2FwaVwvdGVzdFJlc3QiLCJzdWIiOiJ4aWFuZ25hbiIsImlzRnJvbU5ld0xvZ2luIjoiZmFsc2UiLCJhdXRoZW50aWNhdGlvbkRhdGUiOiIyMDIyLTA0LTExVDEwOjM1OjIzLjMwNCswODowMFtBc2lhXC9TaGFuZ2hhaV0iLCJhdXRoZW50aWNhdGlvbk1ldGhvZCI6IlF1ZXJ5RGF0YWJhc2VBdXRoZW50aWNhdGlvbkhhbmRsZXIiLCJzdWNjZXNzZnVsQXV0aGVudGljYXRpb25IYW5kbGVycyI6IlF1ZXJ5RGF0YWJhc2VBdXRoZW50aWNhdGlvbkhhbmRsZXIiLCJpc3MiOiJodHRwczpcL1wvd3d3LnRyeWNhcy5jb206ODQ0M1wvY2FzIiwibG9uZ1Rlcm1BdXRoZW50aWNhdGlvblJlcXVlc3RUb2tlblVzZWQiOiJmYWxzZSIsImV4cCI6MTY0OTY3NDE3NywiaWF0IjoxNjQ5NjQ1Mzc3LCJqdGkiOiJTVC03LU1BeVA3eWoxVElzZnFmb1E3U1F5aWlLWWh4Y1dJTi1PSDdONFU5REIwNCJ9.bgqvcaKdlFN905_KZK2dThskif2qOhUOlHtB9850vcB4_I5x6J5XaeLXR2WKBYc-LWkeU4tmrhjRUfp216Mwpw";
        Jws<Claims> jws = Jwts.parser().setSigningKey("aUBHMUwwghi_8LaAdS98LmVHBk99lJZP29JhQMzdRYa1thh4f1JTF5q3NDW5HQQMTw32RLvka0hNTgslmyUTCQ".getBytes(Charset.forName("UTF-8"))).parseClaimsJws(jwtToken);
        Claims claims = jws.getBody();
        System.out.println(claims);
    }
}
