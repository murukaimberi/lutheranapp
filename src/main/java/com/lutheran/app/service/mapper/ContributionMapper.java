package com.lutheran.app.service.mapper;

import com.lutheran.app.domain.Congregant;
import com.lutheran.app.domain.Contribution;
import com.lutheran.app.service.dto.CongregantDTO;
import com.lutheran.app.service.dto.ContributionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contribution} and its DTO {@link ContributionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContributionMapper extends EntityMapper<ContributionDTO, Contribution> {
    @Mapping(target = "congregant", source = "congregant", qualifiedByName = "congregantSurname")
    ContributionDTO toDto(Contribution s);

    @Named("congregantSurname")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "surname", source = "surname")
    CongregantDTO toDtoCongregantSurname(Congregant congregant);
}
