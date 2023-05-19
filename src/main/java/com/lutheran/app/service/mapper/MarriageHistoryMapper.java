package com.lutheran.app.service.mapper;

import com.lutheran.app.domain.MarriageHistory;
import com.lutheran.app.service.dto.MarriageHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MarriageHistory} and its DTO {@link MarriageHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface MarriageHistoryMapper extends EntityMapper<MarriageHistoryDTO, MarriageHistory> {}
