package com.bf.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtUtils {
    private final int EXPIRATION = 7 * 60 * 60 * 24 * 1000;
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    /**
     * 生成token字符串
     *
     * @param user_id 用户id
     * @return token字符串
     */
    public String generateJwtToken(int user_id) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("beefarming-be-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .claim("id", user_id)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * 检验token是否有效
     *
     * @param token 登录用户携带的token
     * @return boolean
     */
    public boolean checkToken(String token) {
        if (StringUtils.isEmpty(token)) return false;
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * 检验token是否存在与有效
     */
    public boolean checkToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            return checkToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从token中取出用户id
     *
     * @param request 前端请求
     * @return int
     */
    public int getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        if (!checkToken(token)) return -1;
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (int) claims.get("id");
        } catch (Error err) {
            return -1;
        }
    }

    /**
     * 从token中取出用户id
     *
     * @param token token
     * @return int
     */
    public int getUserIdFromToken(String token) {
        if (!checkToken(token)) return -1;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (int) claims.get("id");
    }

}
