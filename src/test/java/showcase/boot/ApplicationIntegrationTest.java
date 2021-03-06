package showcase.boot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import showcase.boot.domain.Contact;

import javax.net.ssl.SSLContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0"})
@ActiveProfiles("standalone")
public class ApplicationIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testContactGet() {

        ResponseEntity<Contact> responseEntity =
            restTemplate.getForEntity("http://localhost:" + port + "/contacts/1", Contact.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getFirstName()).isEqualTo("Donald");
    }

    @Test
    public void testSecurityAnon() {

        try {
            getTestRestTemplate(null, null).getForEntity("http://localhost:" + port + "/contacts/1", Contact.class);
            failBecauseExceptionWasNotThrown(HttpClientErrorException.class);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

    private static RestTemplate getTestRestTemplate(String username, String password) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);

        HttpClient httpClient;
        if (username == null) {
            httpClient = HttpClients.createDefault();
        } else {
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
        }
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setMessageConverters(ImmutableList.<HttpMessageConverter<?>>of(jackson2HttpMessageConverter));
        return restTemplate;
    }

    @Test
    public void testSecurityUnknown() {

        try {
            getTestRestTemplate("unknown", "dummy").getForEntity("http://localhost:" + port + "/contacts/1",
                                                                 Contact.class);
            failBecauseExceptionWasNotThrown(HttpClientErrorException.class);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

    @Test
    public void testSecurityWrongPass() {

        try {
            getTestRestTemplate("user", "wrongpass").getForEntity("http://localhost:" + port + "/contacts/1",
                                                                  Contact.class);
            failBecauseExceptionWasNotThrown(HttpClientErrorException.class);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

    @Configuration
    static class TestConfig {

        @Value("${thisapp.username:user}")
        private String username;
        @Value("${thisapp.password:pass}")
        private String password;

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate(requestFactory());
        }

        @Bean
        public ClientHttpRequestFactory requestFactory() {
            return new HttpComponentsClientHttpRequestFactory(httpClient());
        }

        @Bean
        public HttpClient httpClient() {
            BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
            basicCredentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

            // support self-signed certificates, without a keyStore
            SSLContext sslContext;
            try {
                sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return HttpClients.custom()
                              .setDefaultCredentialsProvider(basicCredentialsProvider)
                              .setSslcontext(sslContext)
                              .build();
        }

    }

}
