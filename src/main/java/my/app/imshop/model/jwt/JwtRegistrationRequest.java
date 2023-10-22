package my.app.imshop.model.jwt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRegistrationRequest {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
}
