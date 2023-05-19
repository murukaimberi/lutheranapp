package com.lutheran.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.lutheran.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeagueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeagueDTO.class);
        LeagueDTO leagueDTO1 = new LeagueDTO();
        leagueDTO1.setId(1L);
        LeagueDTO leagueDTO2 = new LeagueDTO();
        assertThat(leagueDTO1).isNotEqualTo(leagueDTO2);
        leagueDTO2.setId(leagueDTO1.getId());
        assertThat(leagueDTO1).isEqualTo(leagueDTO2);
        leagueDTO2.setId(2L);
        assertThat(leagueDTO1).isNotEqualTo(leagueDTO2);
        leagueDTO1.setId(null);
        assertThat(leagueDTO1).isNotEqualTo(leagueDTO2);
    }
}
