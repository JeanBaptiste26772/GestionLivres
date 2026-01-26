package bf.isge.gsn.mapper;

import bf.isge.gsn.dto.UserDto;
import bf.isge.gsn.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    /**
     * Convertit une entité User en UserDTO (sans le mot de passe)
     */
    public UserDto toDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    /**
     * Convertit une liste d'entités User en liste de UserDTO
     */
    public List<UserDto> toDTOList(List<User> users) {
        if (users == null) {
            return null;
        }

        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}