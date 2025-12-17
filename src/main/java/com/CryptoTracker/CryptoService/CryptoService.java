package com.CryptoTracker.CryptoService;

import com.CryptoTracker.model.CryptoCoin;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoService {

    private static final String API_URL =
            "https://api.coingecko.com/api/v3/simple/price" +
                    "?ids=%s&vs_currencies=usd&include_24hr_change=true";

    public List<CryptoCoin> getCryptoPrices(List<String> coins) {

        List<CryptoCoin> coinList = new ArrayList<>();

        String ids = String.join(",", coins);
        String url = String.format(API_URL, ids);

        RestTemplate restTemplate = new RestTemplate();

        try {
            String response = restTemplate.getForObject(url, String.class);

            System.out.println("API Response: " + response);

            JSONObject json = new JSONObject(response);

            for (String coin : coins) {
                if (json.has(coin)) {

                    JSONObject coinJson = json.getJSONObject(coin);

                    double price = coinJson.getDouble("usd");
                    double change24h = coinJson.getDouble("usd_24h_change");

                    coinList.add(new CryptoCoin(
                            coin,
                            coin.toUpperCase(),
                            price,
                            change24h
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return coinList;
    }
}
