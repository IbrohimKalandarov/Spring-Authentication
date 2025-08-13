package startup.spring_auth.application.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * REST API response uchun generic javob classi.
 *
 * @param <T> Ma’lumot turi (data)
 * @author Ibrohim Kalandarov
 * @since 1.0
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ApiResponse<T> implements Serializable {
    private final boolean success;
    private final String message;
    private final T data;
    private final List<ErrorDetail> errors;


    /**
     * Muvaffaqiyatli javob uchun konstruktor data bilan.
     *
     * @param message Xabar (majburiy)
     * @param <T>     Ma’lumot turi (data)
     * @param data    Ma'lumot uchun (ixtiyoriy).
     *                <p>Agar data null bo'lsa message ni o'zi bilan object yaratiladi</p>
     */
    public static <T> ApiResponse<T> success(T data, @NonNull String message) {
        if (data == null) {
            return new ApiResponse<>(message);
        }
        return new ApiResponse<>(data, message);
    }

    private ApiResponse(T data, String message) {
        this.message = message;
        this.data = data;
        this.success = true;
        this.errors = null;
    }

    private ApiResponse(String message) {
        this.message = message;
        this.success = true;
        this.data = null;
        this.errors = null;
    }


    /**
     * Xato javobi uchun konstruktor.
     *
     * @param message   Xato xabari (majburiy)
     * @param errorCode Xatolik kodlari uchun (majburiy)
     */
    public static <T> ApiResponse<T> error(@NonNull String message, int errorCode) {
        return new ApiResponse<>(message, errorCode);
    }

    private ApiResponse(String message, int errorCode) {
        this.message = message;
        this.errors = Collections.singletonList(new ErrorDetail(message, errorCode));
        this.success = false;
        this.data = null;
    }

    /**
     * Xato detallari uchun inner record class.
     *
     * @param message   Xato xabari
     * @param errorCode Xatolik kodi
     */
    public record ErrorDetail(String message, int errorCode) implements Serializable {
    }

}
