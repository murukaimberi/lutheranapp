package com.lutheran.app.service.mapper;

import com.lutheran.app.domain.Congregant;
import com.lutheran.app.domain.Post;
import com.lutheran.app.service.dto.CongregantDTO;
import com.lutheran.app.service.dto.PostDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Post} and its DTO {@link PostDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostMapper extends EntityMapper<PostDTO, Post> {
    @Mapping(target = "congregant", source = "congregant", qualifiedByName = "congregantSurname")
    PostDTO toDto(Post s);

    @Named("congregantSurname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "surname", source = "surname")
    CongregantDTO toDtoCongregantSurname(Congregant congregant);
}
