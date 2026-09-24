package dto;

import lombok.*;
import net.datafaker.Faker;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class User {
    private String email;
    private String password;
    private String totpSecret;

    public static User getValidUser(){
        return User.builder()
                .email(System.getenv("TRELLO_EMAIL"))
                .password(System.getenv("TRELLO_PASSWORD"))
                .totpSecret(System.getenv("TRELLO_TOTP_SECRET"))
                .build();
    }

    public static User getUserWithWrongEmail(){
        User user = User.getValidUser();
        user.setEmail(new Faker().internet().emailAddress());
        return user;
    }

    public static User getUserWithWrongPassword(){
        User user = User.getValidUser();
        user.setPassword("wrong_password");
        return user;
    }

    public static User getUserWithWrongTotpSecret(){
        User user = User.getValidUser();
        user.setTotpSecret("XXXXXX");
        return user;
    }
}
