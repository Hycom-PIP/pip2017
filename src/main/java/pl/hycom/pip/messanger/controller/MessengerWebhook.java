package pl.hycom.pip.messanger.controller;

import com.github.messenger4j.exceptions.MessengerVerificationException;
import com.github.messenger4j.receive.MessengerReceiveClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Rafal Lebioda on 02.03.2017.
 */
@Log4j2
@Controller
public class MessengerWebhook {

    @Autowired
    private MessengerReceiveClient receiveClient;

    @RequestMapping(value = "/webhook", method = GET, produces = MediaType.TEXT_PLAIN)
    @ResponseBody
    public ResponseEntity<String> verify(
            @RequestParam("hub.verify_token") final String verifyToken,
            @RequestParam("hub.mode") final String mode,
            @RequestParam("hub.challenge") final String challenge) {

        try {
            return ResponseEntity.ok(receiveClient.verifyWebhook(mode, verifyToken, challenge));

        } catch (MessengerVerificationException e) {
            log.error("Webhook verification failed", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/webhook", method = POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Void> sendMessage(
            @RequestBody final String payload,
            @RequestHeader(value = "X-Hub-Signature") String signature) {

        log.info("Received message - starting prepare answer");

        try {
            receiveClient.processCallbackPayload(payload, signature);
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (MessengerVerificationException e) {
            log.error("Error during token verification [" + payload + "]", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
