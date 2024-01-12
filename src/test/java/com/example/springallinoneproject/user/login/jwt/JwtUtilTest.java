package com.example.springallinoneproject.user.login.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springallinoneproject.user.dto.LoggedInUser;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

   @DisplayName("Authorization 에 Bearer prefix가 포함된 경우 성공")
   @Test
   void validAuthorizationPrefixTest(){
      LoggedInUser loggedInUser = new LoggedInUser("ex@example.com", "username");
      Instant current = Instant.now();
      JwtUtil jwtUtil = new JwtUtil();
      jwtUtil.setSecret("secret");

      String token = jwtUtil.createAuthorization(loggedInUser, current);

      assertThat(jwtUtil.isIncludeTokenPrefix(token))
              .isTrue();
   }

   @DisplayName("Authorization에 Bearer prefix가 포함되지 않은 경우 실패")
   @Test
   void validAuthorizationWithoutPrefixTest(){
      LoggedInUser loggedInUser = new LoggedInUser("ex@example.com", "username");
      Instant current = Instant.now();
      JwtUtil jwtUtil = new JwtUtil();
      jwtUtil.setSecret("secret");
      String token = jwtUtil.createAuthorization(loggedInUser, current);

      String result = jwtUtil.extractTokenFromAuthorization(token);

      assertThat(jwtUtil.isIncludeTokenPrefix(result))
              .isFalse();
   }

   @DisplayName("Authorization에 Bearer 외 다른 prefix가 포함된 경우 실패")
   @Test
   void validAuthorizationWithInvalidPrefixTest(){
      LoggedInUser loggedInUser = new LoggedInUser("ex@example.com", "username");
      Instant current = Instant.now();
      JwtUtil jwtUtil = new JwtUtil();
      jwtUtil.setSecret("secret");
      String token = jwtUtil.createAuthorization(loggedInUser, current);

      String result = token.replace("Bearer ", "Basic ");

      assertThat(jwtUtil.isIncludeTokenPrefix(result))
              .isFalse();
   }

   @DisplayName("Authorization 생성 성공")
   @Test
   void createAuthorizationSuccessfullyTest(){
      LoggedInUser loggedInUser = new LoggedInUser("ex@example.com", "username");
      Instant current = Instant.now();
      JwtUtil jwtUtil = new JwtUtil();
      jwtUtil.setSecret("secret");
      jwtUtil.createAuthorization(loggedInUser, current);
   }

   @DisplayName("토큰이 만료되지 않았을 경우 성공")
   @Test
   void validTokenNotExpiredTest(){
      LoggedInUser loggedInUser = new LoggedInUser("ex@example.com", "username");
      Instant current = Instant.now();
      JwtUtil jwtUtil = new JwtUtil();
      jwtUtil.setSecret("secret");
      String token = jwtUtil.createToken(loggedInUser, current);

      assertThat(jwtUtil.isTokenExpired(token))
              .isFalse();
   }

   @DisplayName("토큰이 만료되었을 경우 실패")
   @Test
   void validExpiredTokenTest(){
      LoggedInUser loggedInUser = new LoggedInUser("ex@example.com", "username");
      Instant current = Instant.now();
      JwtUtil jwtUtil = new JwtUtil();
      jwtUtil.setSecret("secret");

      current = current
              .minusMillis(jwtUtil.getExpirationTimeMillis()+10_000);
      String token = jwtUtil.createToken(loggedInUser, current);
      
      assertThat(jwtUtil.isTokenExpired(token))
              .isTrue();
   }
   
   @DisplayName("시그니처가 올바른 토큰일 경우 성공")
   @Test
   void validTokenWithCorrectSignatureTest(){
      LoggedInUser loggedInUser = new LoggedInUser("ex@example.com", "username");
      Instant current = Instant.now();
      JwtUtil jwtUtil = new JwtUtil();
      jwtUtil.setSecret("secret");
      String token = jwtUtil.createToken(loggedInUser, current);

      assertThat(jwtUtil.isTokenNotManipulated(token))
              .isTrue();
   }

   @DisplayName("시그니처가 올바르지 않은 토큰일 경우 실패")
   @Test
   void validTokenManipulatedTest(){
      LoggedInUser loggedInUser = new LoggedInUser("ex@example.com", "username");
      Instant current = Instant.now();
      JwtUtil jwtUtil = new JwtUtil();
      jwtUtil.setSecret("secret");
      String token = jwtUtil.createToken(loggedInUser, current);

      jwtUtil.setSecret("other");

      assertThat(jwtUtil.isTokenNotManipulated(token))
              .isFalse();
   }

   @DisplayName("토큰에서 유저 데이터를 정상적으로 추출할 경우 성공")
   @Test
   void extractUserFromTokenTest(){
      LoggedInUser loggedInUser = new LoggedInUser("ex@example.com", "username");
      Instant current = Instant.now();
      JwtUtil jwtUtil = new JwtUtil();
      jwtUtil.setSecret("secret");
      String token = jwtUtil.createToken(loggedInUser, current);

      LoggedInUser result = jwtUtil.extractUserFromToken(token);

      assertThat(result).isEqualTo(loggedInUser);
   }
}