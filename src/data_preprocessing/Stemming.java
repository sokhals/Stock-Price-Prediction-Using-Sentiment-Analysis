package data_preprocessing;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import org.tartarus.snowball.ext.EnglishStemmer;

/**
 *
 * @author surindersokhal
 */
public class Stemming {

    static EnglishStemmer es = new EnglishStemmer();

    public static String[] performStemming(String words[]) {
        for (int i = 0; i < words.length; i++) {
            es.setCurrent(words[i]);
            es.stem();
            words[i] = es.getCurrent().toString();
        }
        return words;
    }
}
