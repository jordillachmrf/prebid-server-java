package org.rtb.vexing.adapter;

import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixList;
import de.malkusch.whoisServerList.publicSuffixList.PublicSuffixListFactory;
import io.vertx.core.http.HttpClient;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.rtb.vexing.adapter.rubicon.RubiconAdapter;
import org.rtb.vexing.config.ApplicationConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

public class AdapterCatalogTest {

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ApplicationConfig applicationConfig;
    @Mock
    private HttpClient httpClient;

    private PublicSuffixList psl;

    @Before
    public void setupClass() {
        psl = new PublicSuffixListFactory().build();
    }

    @Test
    public void createShouldFailOnNullArguments() {
        assertThatNullPointerException().isThrownBy(() -> AdapterCatalog.create(null, null, null));
        assertThatNullPointerException().isThrownBy(() -> AdapterCatalog.create(applicationConfig, null, null));
        assertThatNullPointerException().isThrownBy(() -> AdapterCatalog.create(applicationConfig, httpClient, null));
    }

    @Test
    public void getShouldReturnConfiguredAdapter() {
        // given
        given(applicationConfig.getString(eq("adapters.rubicon.endpoint"))).willReturn("http://rubiconproject.com/x");
        given(applicationConfig.getString(eq("adapters.rubicon.usersync_url")))
                .willReturn("http://rubiconproject.com/x/cookie/x");
        given(applicationConfig.getString(eq("adapters.rubicon.XAPI.Username"))).willReturn("rubicon_user");
        given(applicationConfig.getString(eq("adapters.rubicon.XAPI.Password"))).willReturn("rubicon_password");

        // when
        final Adapter rubiconAdapter = AdapterCatalog.create(applicationConfig, httpClient, psl).get("rubicon");

        // then
        assertThat(rubiconAdapter)
                .isNotNull()
                .isInstanceOf(RubiconAdapter.class);
    }
}