package com.lutheran.app.service.mapper;

import com.lutheran.app.domain.Congregant;
import com.lutheran.app.domain.League;
import com.lutheran.app.service.dto.CongregantDTO;
import com.lutheran.app.service.dto.LeagueDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link League} and its DTO {@link LeagueDTO}.
 */
@Mapper(componentModel = "spring")
public interface LeagueMapper extends EntityMapper<LeagueDTO, League> {
    @Mapping(target = "congregants", source = "congregants", qualifiedByName = "congregantIdSet")
    LeagueDTO toDto(League s);

    @Mapping(target = "removeCongregants", ignore = true)
    League toEntity(LeagueDTO leagueDTO);

    @Named("congregantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CongregantDTO toDtoCongregantId(Congregant congregant);

    @Named("congregantIdSet")
    default Set<CongregantDTO> toDtoCongregantIdSet(Set<Congregant> congregant) {
        return congregant.stream().map(this::toDtoCongregantId).collect(Collectors.toSet());
    }
}
