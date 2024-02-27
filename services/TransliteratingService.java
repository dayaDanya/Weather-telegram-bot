package org.goncharov.parcertelebot.services;

import com.ibm.icu.text.Transliterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransliteratingService {

    private  final Transliterator transliterator;
    @Autowired
    public TransliteratingService(Transliterator transliterator) {
        this.transliterator = transliterator;
    }

    public String transliterate(String value) {
        String result = transliterator.transliterate(value.toLowerCase());
        return result;
    }
}
