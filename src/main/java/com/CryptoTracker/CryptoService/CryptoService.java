package com.CryptoTracker.CryptoService;

import com.CryptoTracker.model.CryptoCoin;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoService {

    private static final String API_URL = "https://api.coincap.io/v2/assets";
    private static final String API_KEY = "71a53454423bc412eb42bc81efed08c2186913cd21f642ecdddd705feb9fd404";

    public List<CryptoCoin> getCryptoPrices(List<String> coins) {

        List<CryptoCoin> coinList = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

        JSONObject json = new JSONObject(response.getBody());
        JSONArray data = json.getJSONArray("data");

        for (int i = 0; i < data.length(); i++) {

            JSONObject coinJson = data.getJSONObject(i);
            String id = coinJson.getString("id");

            if (coins.contains(id)) {

                double price = Double.parseDouble(coinJson.getString("priceUsd"));
                double change24h = Double.parseDouble(coinJson.getString("changePercent24Hr"));

                coinList.add(
                        new CryptoCoin(id, coinJson.getString("name"), price, change24h)
                );
            }
        }

        return coinList;
    }
}
