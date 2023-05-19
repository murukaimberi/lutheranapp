package com.lutheran.app.service.mapper;

import com.lutheran.app.domain.Congregant;
import com.lutheran.app.domain.Dependant;
import com.lutheran.app.service.dto.CongregantDTO;
import com.lutheran.app.service.dto.DependantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dependant} and its DTO {@link DependantDTO}.
 */
@Mapper(componentModel = "spring")
public interface DependantMapper extends EntityMapper<DependantDTO, Dependant> {
    @Mapping(target = "congregant", source = "congregant", qualifiedByName = "congregantSurname")
    DependantDTO toDto(Dependant s);

    @Named("congregantSurname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "surname", source = "surname")
    CongregantDTO toDtoCongregantSurname(Congregant congregant);
}
