package com.javatechie.chatgptbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.javatechie.chatgptbot.dto.ChatGPTRequest;
import com.javatechie.chatgptbot.dto.ChatGPTResponse;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/bot")
public class CustomBotController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt) {

        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        // template.postForEntity(apiURL, request, ChatGPTResponse.class);
        // return ChatGPTResponse.getChoices().get(0).getMessage().getContent(); //
        // 16:37

        ChatGPTResponse response;
        try {

            // 반환된 객체에서 getChoices() 호출
            ResponseEntity<ChatGPTResponse> responseEntity = template.postForEntity(apiURL, request,
                    ChatGPTResponse.class);

            // System.err.println(responseEntity.getStatusCode());

            response = responseEntity.getBody();

            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "No choices available.";

    }

}
