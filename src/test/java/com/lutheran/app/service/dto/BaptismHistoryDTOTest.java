package com.lutheran.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lutheran.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BaptismHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaptismHistoryDTO.class);
        BaptismHistoryDTO baptismHistoryDTO1 = new BaptismHistoryDTO();
        baptismHistoryDTO1.setId(1L);
        BaptismHistoryDTO baptismHistoryDTO2 = new BaptismHistoryDTO();
        assertThat(baptismHistoryDTO1).isNotEqualTo(baptismHistoryDTO2);
        baptismHistoryDTO2.setId(baptismHistoryDTO1.getId());
        assertThat(baptismHistoryDTO1).isEqualTo(baptismHistoryDTO2);
        baptismHistoryDTO2.setId(2L);
        assertThat(baptismHistoryDTO1).isNotEqualTo(baptismHistoryDTO2);
        baptismHistoryDTO1.setId(null);
        assertThat(baptismHistoryDTO1).isNotEqualTo(baptismHistoryDTO2);
    }
}
