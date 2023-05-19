package com.lutheran.app.service.mapper;

import com.lutheran.app.domain.BaptismHistory;
import com.lutheran.app.domain.Congregant;
import com.lutheran.app.domain.MarriageHistory;
import com.lutheran.app.domain.User;
import com.lutheran.app.service.dto.BaptismHistoryDTO;
import com.lutheran.app.service.dto.CongregantDTO;
import com.lutheran.app.service.dto.MarriageHistoryDTO;
import com.lutheran.app.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Congregant} and its DTO {@link CongregantDTO}.
 */
@Mapper(componentModel = "spring")
public interface CongregantMapper extends EntityMapper<CongregantDTO, Congregant> {
    @Mapping(target = "marriageHistory", source = "marriageHistory", qualifiedByName = "marriageHistoryId")
    @Mapping(target = "baptismHistory", source = "baptismHistory", qualifiedByName = "baptismHistoryId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    CongregantDTO toDto(Congregant s);

    @Named("marriageHistoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MarriageHistoryDTO toDtoMarriageHistoryId(MarriageHistory marriageHistory);

    @Named("baptismHistoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BaptismHistoryDTO toDtoBaptismHistoryId(BaptismHistory baptismHistory);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
