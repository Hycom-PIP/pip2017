package pl.hycom.pip.messanger.config;

import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.common.MessengerHttpClient;
import com.github.messenger4j.common.MessengerHttpClient.HttpMethod;
import com.github.messenger4j.common.MessengerHttpClient.HttpResponse;
import com.github.messenger4j.receive.MessengerReceiveClient;
import com.github.messenger4j.send.MessengerSendClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import pl.hycom.pip.messanger.handler.PipelineMessageHandler;
import pl.hycom.pip.messanger.pipeline.PipelineManager;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
@Import(MessengerConfiguration.class)
@Log4j2
public class MessengerConfigurationTest {

    @Value("${messenger.pipeline.filepath}")
    private String pipelineFilepath;

    @Bean
    public MessengerHttpClient mockHttpClient() {
        return mock(MessengerHttpClient.class);
    }

    @Bean
    @Primary
    public PipelineManager pipelineManager() {
        return new PipelineManager(pipelineFilepath);
    }

    @Bean
    @Primary
    public PipelineMessageHandler pipelineMessageHandler() {
        return new PipelineMessageHandler(pipelineManager());
    }

    @Bean
    @Primary
    public MessengerReceiveClient receiveClient() throws Exception {
        return MessengerPlatform.newReceiveClientBuilder("X", "X")
                .disableSignatureVerification()
                .onTextMessageEvent(pipelineMessageHandler())
                .build();
    }

    @Bean
    @Primary
    public MessengerSendClient sendClient() throws Exception {

        HttpResponse fakeResponse = new HttpResponse(200, "{\n" +
                "  \"recipient_id\": \"USER_ID\",\n" +
                "  \"message_id\": \"mid.1473372944816:94f72b88c597657974\"\n" +
                "}");

        when(mockHttpClient().execute(any(HttpMethod.class), anyString(), anyString())).thenReturn(fakeResponse);

        return MessengerPlatform.newSendClientBuilder("X")
                .httpClient(mockHttpClient())
                .build();
    }
}
