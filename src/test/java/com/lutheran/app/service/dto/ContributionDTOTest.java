package com.lutheran.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lutheran.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContributionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContributionDTO.class);
        ContributionDTO contributionDTO1 = new ContributionDTO();
        contributionDTO1.setId(1L);
        ContributionDTO contributionDTO2 = new ContributionDTO();
        assertThat(contributionDTO1).isNotEqualTo(contributionDTO2);
        contributionDTO2.setId(contributionDTO1.getId());
        assertThat(contributionDTO1).isEqualTo(contributionDTO2);
        contributionDTO2.setId(2L);
        assertThat(contributionDTO1).isNotEqualTo(contributionDTO2);
        contributionDTO1.setId(null);
        assertThat(contributionDTO1).isNotEqualTo(contributionDTO2);
    }
}
