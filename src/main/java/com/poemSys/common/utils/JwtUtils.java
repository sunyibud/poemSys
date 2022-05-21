package com.poemSys.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "poemsys.jwt")//将配置注入配置文件yaml中
public class JwtUtils
{
    //方便yaml文件中设置
    private long expire;//jwt过期时间（秒）
    private String secret;//密钥
    private String header;//名称

    // 生成jwt
    public String generateToken(String userId)
    {

        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)// 7天过期
                .signWith(SignatureAlgorithm.HS512, secret)//密钥
                .compact();
    }

    // 解析jwt
    public Claims getClaimByToken(String jwt)
    {
        try
        {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e)
        {
            return null;
        }
    }

    // jwt是否过期
    public boolean isTokenExpired(Claims claims)
    {
        //过期时间是否在当前时间之前
        return claims.getExpiration().before(new Date());
    }
}
