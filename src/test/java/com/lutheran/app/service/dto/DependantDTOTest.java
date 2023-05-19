package com.lutheran.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lutheran.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DependantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DependantDTO.class);
        DependantDTO dependantDTO1 = new DependantDTO();
        dependantDTO1.setId(1L);
        DependantDTO dependantDTO2 = new DependantDTO();
        assertThat(dependantDTO1).isNotEqualTo(dependantDTO2);
        dependantDTO2.setId(dependantDTO1.getId());
        assertThat(dependantDTO1).isEqualTo(dependantDTO2);
        dependantDTO2.setId(2L);
        assertThat(dependantDTO1).isNotEqualTo(dependantDTO2);
        dependantDTO1.setId(null);
        assertThat(dependantDTO1).isNotEqualTo(dependantDTO2);
    }
}
