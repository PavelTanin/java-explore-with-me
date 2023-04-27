package ru.practicum.mapper.user;

import ru.practicum.dto.user.NewUserDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.user.User;

public class UserMapper {

    public static User toUser(NewUserDto newUserDto) {
        return new User(newUserDto.getEmail(), newUserDto.getName());
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getEmail(), user.getId(), user.getName());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
