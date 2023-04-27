package ru.practicum.dto.user;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class UserDto {

    private String email;

    private Long id;

    private String name;

}
