package org.wickedsource.coderadar.security.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.coderadar.factories.databases.DbUnitFactory;
import org.wickedsource.coderadar.testframework.template.IntegrationTestTemplate;
import org.wickedsource.coderadar.user.domain.User;

import com.github.springtestdbunit.annotation.DatabaseSetup;

public class RefreshTokenRepositoryTest extends IntegrationTestTemplate {

    @Autowired
    private RefreshTokenRepository repository;

    @Test
    @DatabaseSetup(DbUnitFactory.RefreshTokens.REFRESH_TOKENS)
    public void load() throws Exception {
        RefreshToken refreshToken = repository.findOne(1L);
        assertThat(refreshToken).isNotNull();
        User user = refreshToken.getUser();
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("radar");
    }
}
