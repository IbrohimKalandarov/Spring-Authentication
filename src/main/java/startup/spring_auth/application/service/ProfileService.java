package startup.spring_auth.application.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import startup.spring_auth.application.payload.ApiResponse;
import startup.spring_auth.application.payload.request.ProfilePatchDTO;
import startup.spring_auth.application.payload.response.UserDTO;

@Component
public interface ProfileService {

    ApiResponse<String> profileMePatch(ProfilePatchDTO dto, HttpServletRequest request);

    ApiResponse<UserDTO> profileMeGet(HttpServletRequest request);

    ApiResponse<String> updatePhoneNumber(String phoneNumber, HttpServletRequest request);
}
