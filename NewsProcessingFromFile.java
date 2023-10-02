package com.example.opennlp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.namefind.*;
import opennlp.tools.util.Span;

public class NewsProcessingFromFile {
    public static void main(String[] args) {
        try {
            // Load models
            InputStream sentenceModelIn = new FileInputStream("C:/Users/Sumanth/OneDrive/Desktop/NLP/en-sent.bin");
            SentenceModel sentenceModel = new SentenceModel(sentenceModelIn);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);

            InputStream tokenizerModelIn = new FileInputStream("C:/Users/Sumanth/OneDrive/Desktop/NLP/en-token.bin");
            TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelIn);
            TokenizerME tokenizer = new TokenizerME(tokenizerModel);

            InputStream posModelIn = new FileInputStream("C:/Users/Sumanth/OneDrive/Desktop/NLP/en-pos-maxent.bin");
            POSModel posModel = new POSModel(posModelIn);
            POSTaggerME posTagger = new POSTaggerME(posModel);

            InputStream nerModelIn = new FileInputStream("C:/Users/Sumanth/OneDrive/Desktop/NLP/en-ner-person.bin");
            TokenNameFinderModel nerModel = new TokenNameFinderModel(nerModelIn);
            NameFinderME nerFinder = new NameFinderME(nerModel);

            // Read the news article from a text document
            String filePath = "C:/Users/Sumanth/Downloads/news article.txt";
            StringBuilder articleText = new StringBuilder();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    articleText.append(line).append("\n");
                }
            }

            String newsArticle = articleText.toString();

            // Detect sentences
            String[] sentences = sentenceDetector.sentDetect(newsArticle);

            for (String sentence : sentences) {
                // Tokenize sentence into words
                String[] words = tokenizer.tokenize(sentence);

                // Perform POS tagging
                String[] posTags = posTagger.tag(words);

                // Find named entities
                Span[] spans = nerFinder.find(words);
                String[] entities = Span.spansToStrings(spans, words);

                // Print results
                System.out.println("Sentence: " + sentence);
                System.out.println("Tokens: " + String.join(", ", words));
                System.out.println("POS Tags: " + String.join(", ", posTags));
                System.out.println("Named Entities: " + String.join(", ", entities));
                System.out.println();
            }

            // Close the input streams
            sentenceModelIn.close();
            tokenizerModelIn.close();
            posModelIn.close();
            nerModelIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
