package org.goncharov.parcertelebot.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParcingService {
    private final TransliteratingService transliteratingService;
    @Autowired
    public ParcingService(TransliteratingService transliteratingService) {
        this.transliteratingService = transliteratingService;
    }

    public String parse(String value){
        value = value.replace("ь", "");
        value = value.replace("-", "_");
        value = transliteratingService.transliterate(value);

        List<String> params = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://pogoda.mail.ru/prognoz/" + value)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 YaBrowser/24.1.0.0 Safari/537.36")
                    .get();
            Element temperatureDiv = doc.selectFirst("div.information__content__temperature");
            params.add("Значение температуры: " + temperatureDiv.text());
            Elements divs = doc.select("div.information__content__additional__item");
            divs.remove(1);
            for (Element div : divs) {
                Element span = div.getElementsByTag("span").first();
                if (span != null) {
                    String title = span.attr("title");
                    params.add(title);
                }
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        StringBuilder res = new StringBuilder();
        for(String p : params){
            res.append(p).append("\n");
        }
        return res.toString();
    }


}
