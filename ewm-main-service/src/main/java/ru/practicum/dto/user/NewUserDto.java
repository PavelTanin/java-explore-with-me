package ru.practicum.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@ToString
@NoArgsConstructor
public class NewUserDto {

    @NotEmpty(message = "Поле email не может быть пустым")
    @Email
    private String email;

    @NotEmpty(message = "Поле name не может быть пустым")
    private String name;

}
