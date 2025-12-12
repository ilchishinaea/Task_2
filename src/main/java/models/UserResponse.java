package models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;

}
