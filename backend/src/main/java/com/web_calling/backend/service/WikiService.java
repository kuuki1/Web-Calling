package com.web_calling.backend.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WikiService {

    private final String API_URL = "https://en.wikipedia.org/w/api.php";

    public List<String> getLinks(String name) {

        RestTemplate restTemplate = new RestTemplate();

        String url = API_URL +
                "?action=query&prop=links&titles=" + name.replace(" ", "_") +
                "&format=json&pllimit=50";

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "web-calling-app/1.0");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map body = response.getBody();

        List<String> result = new ArrayList<>();

        try {
            Map query = (Map) body.get("query");
            Map pages = (Map) query.get("pages");

            for (Object pageObj : pages.values()) {
                Map page = (Map) pageObj;

                List<Map> links = (List<Map>) page.get("links");

                if (links != null) {
                    for (Map link : links) {
                        String title = (String) link.get("title");

                        if (isValidPerson(title)) {
                            result.add(normalize(title));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Wiki parse error");
        }

        return result;
    }

    private boolean isValidPerson(String title) {
        if (title == null) return false;
        if (title.contains("(")) return false;

        int wordCount = title.split(" ").length;
        if (wordCount < 2 || wordCount > 3) return false;

        String lower = title.toLowerCase();
        return !(lower.contains("election") ||
                lower.contains("company") ||
                lower.contains("project") ||
                lower.contains("organization"));
    }

    private String normalize(String name) {
        return name.trim().replace("_", " ");
    }
}