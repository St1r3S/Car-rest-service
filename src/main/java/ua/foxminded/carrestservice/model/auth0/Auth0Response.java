package ua.foxminded.carrestservice.model.auth0;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth0Response {
    @JsonProperty("access_token")
    private String accessToken;
    private String type;
}
