package com.devteria.identity_service.configuration;

import com.devteria.identity_service.dto.request.IntrospectRequest;
import com.devteria.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signerKey}")
    private String signerKey;

    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // Gọi dịch vụ authenticationService để kiểm tra tính hợp lệ của token
            // Phương thức introspect sẽ xác minh chữ ký và kiểm tra thời gian hết hạn của token
            var response = authenticationService.introspect(
                    IntrospectRequest.builder()
                            .token(token) // Truyền token cần kiểm tra
                            .build()
            );

            if (!response.isValid()) {
                throw new JwtException("invalid token");
            }
        } catch (JOSEException | ParseException e) {
            // Nếu xảy ra lỗi khi kiểm tra token, ném JwtException với thông báo lỗi chi tiết
            throw new JwtException(e.getMessage());
        }

        // Kiểm tra nếu bộ giải mã NimbusJwtDecoder chưa được khởi tạo
        if (Objects.isNull(nimbusJwtDecoder)) {
            // Tạo SecretKeySpec từ chuỗi key bí mật (signerKey) sử dụng thuật toán HMAC-SHA512
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HmacSHA512");

            // Khởi tạo NimbusJwtDecoder với key bí mật và cấu hình thuật toán mã hóa (HS512)
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec) // Sử dụng key bí mật
                    .macAlgorithm(MacAlgorithm.HS512) // Cấu hình thuật toán HMAC-SHA512
                    .build();
        }

        // Giải mã token và trả về đối tượng Jwt
        // Nếu token không hợp lệ, NimbusJwtDecoder sẽ tự động ném JwtException
        return nimbusJwtDecoder.decode(token);
    }
}
