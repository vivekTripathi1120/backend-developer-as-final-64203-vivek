package com.project.resource_booking_system.security;

import com.google.gson.Gson;
import com.project.resource_booking_system.dto.CacheDTO;
import com.project.resource_booking_system.dto.UserJWTBuilderDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwtExpiration}")
    private long jwtExpiryTime;

    private SecretKey getSecretkey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    public String generateToken(UserJWTBuilderDTO user) {
        // Implement token generation logic here

        Map<String,String> claims = new HashMap<>();
        claims.put("email",user.getEmail());
        claims.put("role",new Gson().toJson(user.getRoles()));
        claims.put("userName",user.getUsername());
        claims.put("userId",user.getUserId().toString());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("users",claims)
                .issuedAt(new Timestamp(System.currentTimeMillis()))
                .expiration(new Timestamp(System.currentTimeMillis() + jwtExpiryTime))
                .signWith(getSecretkey())
                .compact();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {

        Claims claims = Jwts.parser().verifyWith(getSecretkey()).build().parseSignedClaims(token).getPayload();
        String username = claims.getSubject();
        Date expiration = claims.getExpiration();
        return username.equals(userDetails.getUsername())
                && expiration != null
                && expiration.after(new Date());
    }

    public CacheDTO extractUserDetails(HttpServletRequest request) {

        String jwtToken = request.getHeader("Authorization").split("Bearer ")[1];
        Claims claims = Jwts.parser().verifyWith(getSecretkey()).build().parseSignedClaims(jwtToken).getPayload();

             CacheDTO cacheDTO = new CacheDTO();
        if(null != claims){
            Map<String,Object> userClaims = (Map<String, Object>) claims.get("users");
            cacheDTO.setUserId(Long.valueOf(userClaims.get("userId").toString()));
            cacheDTO.setUsername(userClaims.get("userName").toString());
            cacheDTO.setEmail(userClaims.get("email").toString());
            cacheDTO.setRoles(new Gson().fromJson(userClaims.get("role").toString(), Map.class));
            return cacheDTO;
        }

        return cacheDTO;
    }

    public Long getJwtExpiryTime() {
        return new Timestamp(System.currentTimeMillis() + jwtExpiryTime).getTime();
    }
}
