package TESTING;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class APIClient {
    private static final String BASE_URL = "http://10.210.13.207:8080/api/foh/menus";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // Make a GET request to retrieve active menus
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "/menus", String.class);
        String activeMenus = response.getBody();

        // Process the response
        System.out.println("Active Menus: " + activeMenus);
    }
}
