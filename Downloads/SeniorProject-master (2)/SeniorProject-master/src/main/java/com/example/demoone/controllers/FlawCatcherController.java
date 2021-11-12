package com.example.demoone.controllers;

import com.example.demoone.model.Flaw;
import com.example.demoone.model.Student;
import com.example.demoone.services.AnalyzeService;
import com.example.demoone.util.CSVHelper;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FlawCatcherController implements Initializable {
    @FXML
    public TextArea errorOutput;
    @FXML
    private Label welcomeText;
    //@FXML
    //private TextArea output;
    @FXML
    private Label statusLabel;
    @FXML
    private ListView<String> files;

    private String selectedDir = System.getProperty("user.home");
    private final IntegerProperty flawCount = new SimpleIntegerProperty(0);
    private final StringProperty status = new SimpleStringProperty("Idle");
    private final StringProperty results = new SimpleStringProperty("");
    private final StringProperty errors = new SimpleStringProperty("");
    private final String STATUS_MESSAGE = " %,d flaws found.";
    private final SimpleListProperty<String> filenames =
            new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    private final Set<String> filenamesSet = new HashSet<>();
    private final Map<String, List<Flaw>> flawsByFilename = new HashMap<>();
    private final SimpleMapProperty<String, List<Flaw>> flaws =
            new SimpleMapProperty<>(FXCollections.observableMap(flawsByFilename));


    private boolean isFirstItem;

    //GUI

    @FXML
    AnchorPane middlePane;
    @FXML
    Label studentFlaws;
    @FXML
    TableView studentTable = new TableView<>();
    @FXML TableColumn<Student, String> lNameCol = new TableColumn<Student, String>("Last Name"),
            fNameCol = new TableColumn<Student, String>("First Name"),
            cNameCol = new TableColumn<Student, String>("Class Name");
    ArrayList<Student> students = new ArrayList<Student>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //flawCount.addListener((observable, oldValue, newValue) ->
                //statusLabel.setText(status.get() + String.format(STATUS_MESSAGE, newValue.intValue())));
        //.addListener((observable, oldValue, newValue) ->
                //statusLabel.setText(newValue + String.format(STATUS_MESSAGE, flawCount.get())));
        //output.textProperty().bind(results);
        //errorOutput.textProperty().bind(errors);
        //files.itemsProperty().bind(filenames);
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                studentFlaws.setText(studentTable.getSelectionModel().getSelectedItem().toString());
            }
        });
    }

    @FXML
    protected void onCatchFlawsClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file;
        if (((Button) event.getSource()).getId().equals("file")) {
            file = chooseFile(stage);
        } else {
            file = chooseDirectory(stage);
        }
        if (file != null) {
            analyzeAndDisplayResults(file);
        }
    }

    private File chooseDirectory(Stage stage) {
        var fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Choose a file");
        fileChooser.setInitialDirectory(new File(selectedDir));
        return fileChooser.showDialog(stage.getOwner());
    }

    private File chooseFile(Stage stage) {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file to analyze");
        fileChooser.setInitialDirectory(new File(selectedDir));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("C and C++ files", "*.c", "*.h", "*.cpp"));
        return fileChooser.showOpenDialog(stage.getOwner());
    }

    private void analyzeAndDisplayResults(File file) {
        selectedDir = file.getParentFile().getAbsolutePath();
        //welcomeText.setText(file.toURI().toString());
        //flawCount.set(0);
        results.set("");
        errors.set("");
        filenamesSet.clear();
        isFirstItem = true;
        var analyzeService = new AnalyzeService(file.getAbsolutePath());
        analyzeService.setOnScheduled(event1 -> status.set("Running."));
        analyzeService.setOnFailed(failEvent -> status.set("Failed."));
        analyzeService.setOnSucceeded(e -> {
            status.set("Done.");
            results.set(String.join("\n", analyzeService.getValue().stream().skip(1).toList()));
            errors.set(String.join("\n", analyzeService.getErrors()));
        });
        analyzeService.getResults().addListener(
                (ListChangeListener<? super String>)
                        change -> {
                            flawCount.set(analyzeService.getResults().size() - 1);
                            while (change.next()) {
                                processResults(Collections.unmodifiableList(change.getAddedSubList()));
                            }
                        }
        );
        analyzeService.start();
    }

    private void processResults(List<String> results) {
        try {
            CSVHelper.processResults(results)
                    .forEach(
                            flaw -> flawsByFilename
                                    .compute(flaw.fileName(),
                                            (s, flaws1) -> {
                                                if (flaws1 == null) {
                                                    return Arrays.asList(flaw);
                                                } else {
                                                    var newList = new ArrayList<>(flaws1);
                                                    newList.add(flaw);
                                                    return newList;
                                                }
                                            }));
            filenamesSet.addAll(CSVHelper.processResults(results).stream().map(Flaw::fileName).toList());
            filenames.clear();
            filenames.addAll(filenamesSet.stream().sorted().toList());
            loadStudents();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML protected void loadStudents() throws FileNotFoundException {
        String fName = "";
        String lName = "";
        String cName = "";
        String currentDirectory = com.example.demoone.util.ProgramDirectoryUtilities.getProgramDirectory();
        ObservableList<Student> list = FXCollections.observableArrayList();
        studentTable.getColumns().clear();

        for(String s : filenames.get()) {
            StringTokenizer tokens = new StringTokenizer(s, "_");
            tokens.nextToken();
            tokens.nextToken();
            tokens.nextToken();
            lName = tokens.nextToken();
            fName = tokens.nextToken();
            cName = tokens.nextToken();
            cName = cName.substring(0, cName.length() - 4);
            Student student = new Student(fName, lName, cName, "", flawsByFilename.get(s));
            students.add(student);
            list.add(student);
        }

        for (Student s : students) {
            s.getFlaws();
        }

        lNameCol.setCellValueFactory(cellData -> cellData.getValue().lNameProperty());
        fNameCol.setCellValueFactory(cellData -> cellData.getValue().fNameProperty());
        cNameCol.setCellValueFactory(cellData -> cellData.getValue().cNameProperty());

        studentTable.setItems(list);
        studentTable.setVisible(true);
        studentTable.getColumns().addAll(lNameCol, fNameCol, cNameCol);

        String accum = "";
        for(String s : filenames) {
            accum += flawsByFilename.get(s);
        }
        studentFlaws.setText(accum);
        System.out.println(accum);
    }

    @FXML protected void clearStudents() {
        studentTable.getItems().clear();
        students.clear();
    }

    @FXML protected void exportStudents() {
        for (Student s: students) {
            File dir = new File("C:\\Users\\Renzo Pretto\\Downloads\\SeniorProject-master (2)\\SeniorProject-master\\src\\main\\java\\com\\example\\demoone\\results\\" + s.fNameProperty() + ".txt");
            System.out.println(dir.getName());
        }
    }

}