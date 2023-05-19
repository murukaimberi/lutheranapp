package com.lutheran.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.lutheran.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarriageHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarriageHistory.class);
        MarriageHistory marriageHistory1 = new MarriageHistory();
        marriageHistory1.setId(1L);
        MarriageHistory marriageHistory2 = new MarriageHistory();
        marriageHistory2.setId(marriageHistory1.getId());
        assertThat(marriageHistory1).isEqualTo(marriageHistory2);
        marriageHistory2.setId(2L);
        assertThat(marriageHistory1).isNotEqualTo(marriageHistory2);
        marriageHistory1.setId(null);
        assertThat(marriageHistory1).isNotEqualTo(marriageHistory2);
    }
}
