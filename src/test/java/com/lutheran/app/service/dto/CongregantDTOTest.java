package com.lutheran.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lutheran.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CongregantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CongregantDTO.class);
        CongregantDTO congregantDTO1 = new CongregantDTO();
        congregantDTO1.setId(1L);
        CongregantDTO congregantDTO2 = new CongregantDTO();
        assertThat(congregantDTO1).isNotEqualTo(congregantDTO2);
        congregantDTO2.setId(congregantDTO1.getId());
        assertThat(congregantDTO1).isEqualTo(congregantDTO2);
        congregantDTO2.setId(2L);
        assertThat(congregantDTO1).isNotEqualTo(congregantDTO2);
        congregantDTO1.setId(null);
        assertThat(congregantDTO1).isNotEqualTo(congregantDTO2);
    }
}
