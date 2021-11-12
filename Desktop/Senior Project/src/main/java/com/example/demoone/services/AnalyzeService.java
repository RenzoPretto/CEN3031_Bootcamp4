package com.example.demoone.services;

import com.example.demoone.analyzers.FlawFinderDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeService extends Service<List<String>> {

    private final String inputPath;
    private final ObservableList<String> results = FXCollections.observableArrayList(new ArrayList<>());
    private final ObservableList<String> errors = FXCollections.observableArrayList(new ArrayList<>());

    public AnalyzeService(String inputPath) {
        this.inputPath = inputPath;
    }

    @Override
    protected Task<List<String>> createTask() {
        return new FlawFinderDriver(inputPath, results, errors);
    }

    public ObservableList<String> getResults() {
        return results;
    }

    public ObservableList<String> getErrors() {
        return errors;
    }
}
