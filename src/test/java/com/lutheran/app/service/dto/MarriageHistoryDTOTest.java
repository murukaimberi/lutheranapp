package com.lutheran.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lutheran.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarriageHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarriageHistoryDTO.class);
        MarriageHistoryDTO marriageHistoryDTO1 = new MarriageHistoryDTO();
        marriageHistoryDTO1.setId(1L);
        MarriageHistoryDTO marriageHistoryDTO2 = new MarriageHistoryDTO();
        assertThat(marriageHistoryDTO1).isNotEqualTo(marriageHistoryDTO2);
        marriageHistoryDTO2.setId(marriageHistoryDTO1.getId());
        assertThat(marriageHistoryDTO1).isEqualTo(marriageHistoryDTO2);
        marriageHistoryDTO2.setId(2L);
        assertThat(marriageHistoryDTO1).isNotEqualTo(marriageHistoryDTO2);
        marriageHistoryDTO1.setId(null);
        assertThat(marriageHistoryDTO1).isNotEqualTo(marriageHistoryDTO2);
    }
}
