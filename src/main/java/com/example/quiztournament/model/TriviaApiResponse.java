package com.example.quiztournament.model;

import java.util.List;

public class TriviaApiResponse {
    private List<TriviaQuestion> results;

    // Getters and setters
    public List<TriviaQuestion> getResults() {
        return results;
    }

    public void setResults(List<TriviaQuestion> results) {
        this.results = results;
    }
}
