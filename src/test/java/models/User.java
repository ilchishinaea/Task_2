package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String email;
    private String password;
    private String name;

    // Дополнительный конструктор на два поля
    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
