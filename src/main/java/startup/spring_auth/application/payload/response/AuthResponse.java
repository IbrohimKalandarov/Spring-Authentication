package startup.spring_auth.application.payload.response;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record AuthResponse(String accessToken, String refreshToken) implements Serializable {
}


