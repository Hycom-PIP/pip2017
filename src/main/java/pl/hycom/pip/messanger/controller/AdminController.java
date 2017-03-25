package pl.hycom.pip.messanger.controller;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.profile.MessengerProfileClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.hycom.pip.messanger.model.Greeting;
import pl.hycom.pip.messanger.model.GreetingList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piotr on 12.03.2017.
 */

@Log4j2
@Controller
public class AdminController {

    @Autowired
    private MessengerProfileClient profileClient;

    @RequestMapping(value = "/admin")
    public ModelAndView admin() {
        return new ModelAndView("admin", "showBackLink", false);
    }

    @GetMapping("/admin/greeting")
    public String adminGreeting(Model model) {
        /*Greeting greeting = new Greeting();

        try {
            List<com.github.messenger4j.profile.Greeting> greetings = profileClient.getWelcomeMessage().getGreetings();
            if (!greetings.isEmpty()) {
                greeting.setContent(greetings.get(0).getText());
            }

        } catch (MessengerApiException | MessengerIOException e) {
            log.error("Error during getting greeting text from facebook", e);
        }*/
        //model.addAttribute("greeting", new Greeting());

        List<Greeting> greetings = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            greetings.add(new Greeting());
            greetings.get(i).setContent("ajfnkssa");
        }
        GreetingList greetingList = new GreetingList();
        greetingList.setGreetings(greetings);
        model.addAttribute("greetings", greetingList);
        return "greeting";
    }

    @PostMapping("/admin/greeting")
    public String greetingSubmit(@ModelAttribute Greeting[] greeting) {
        try {
            profileClient.setupWelcomeMessage(greeting[0].getContent());
            log.info("Greeting text correctly updated");

        } catch (MessengerApiException | MessengerIOException e) {
            log.error("Error during changing greeting message", e);
        }

        return "greeting";
    }

}
