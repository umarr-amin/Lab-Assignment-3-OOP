package com.example.formbyfilehandling;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class FormWithFi extends Application {


    private TextField fullNameField;
    private TextField idField;
    private DatePicker dobPicker;
    private ToggleGroup genderGroup;
    private ComboBox<String> homeProvinceCombo;


    private TextField searchField;
    private TextArea resultArea;


    private final File dataFile = new File("form_data.txt");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Form with File Handling");


        fullNameField = new TextField();
        idField = new TextField();
        dobPicker = new
                DatePicker();


        genderGroup = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("Male");
        RadioButton femaleRadio = new RadioButton("Female");

        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);





        homeProvinceCombo = new ComboBox<>();
        homeProvinceCombo.getItems().addAll("Punjab", "Sindh", "Khyber Pakhtunkhwa", "Balochistan", "Islamabad Capital Territory", "Gilgit-Baltistan", "Azad Jammu and Kashmir");
        homeProvinceCombo.setPromptText("Select Province");

        searchField = new TextField();
        resultArea = new TextArea();
        resultArea.setEditable(false);


        Label fullNameLabel = new Label("Full Name:");
        Label idLabel = new Label("ID:");
        Label genderLabel = new Label("Gender:");
        Label homeProvinceLabel = new Label("Home Province:");
        Label dobLabel = new Label("DOB:");
        Label searchLabel = new Label("Search:");


        Button newButton = new Button("New");
        Button deleteButton = new Button("Delete");
        Button searchButton = new Button("Search");
        Button closeButton = new Button("Close");


        newButton.setOnAction(e -> saveData());
        deleteButton.setOnAction(e -> clearFields());
        searchButton.setOnAction(e -> searchData());
        closeButton.setOnAction(e -> primaryStage.close());


        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));


        formGrid.add(fullNameLabel, 0, 0);
        formGrid.add(fullNameField, 1, 0);
        formGrid.add(idLabel, 0, 1);
        formGrid.add(idField, 1, 1);
        formGrid.add(genderLabel, 0, 2);

        HBox genderBox = new HBox(10, maleRadio, femaleRadio);
        formGrid.add(genderBox, 1, 2);

        formGrid.add(homeProvinceLabel, 0, 3);
        formGrid.add(homeProvinceCombo, 1, 3);
        formGrid.add(dobLabel, 0, 4);
        formGrid.add(dobPicker, 1, 4);

        VBox buttonBox = new VBox(10, newButton, deleteButton, searchButton, closeButton);
        buttonBox.setAlignment(Pos.TOP_CENTER);
        buttonBox.setPadding(new Insets(10));


        HBox searchBox = new HBox(10, searchLabel, searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(10));


        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(formGrid);
        mainLayout.setRight(buttonBox);
        mainLayout.setBottom(new VBox(10, searchBox, resultArea));


        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void saveData() {
        String fullName = fullNameField.getText();
        String id = idField.getText();
        String dob = dobPicker.getValue() != null ? dobPicker.getValue().toString() : "";
        String gender = ((RadioButton) genderGroup.getSelectedToggle()).getText();
        String homeProvince = homeProvinceCombo.getValue();

        if (fullName.isEmpty() || id.isEmpty() || dob.isEmpty() || homeProvince == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled out!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile, true))) {
            writer.write(fullName + "," + id + "," + gender + "," + homeProvince + "," + dob);
            writer.newLine();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Data saved successfully!");
            clearFields();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save data!");
        }
    }


    private void clearFields() {
        fullNameField.clear();
        idField.clear();
        dobPicker.setValue(null);
        genderGroup.selectToggle(genderGroup.getToggles().get(0));
        homeProvinceCombo.setValue(null);
    }


    private void searchData() {
        String query = searchField.getText().toLowerCase();
        if (query.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Search field cannot be empty!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            List<String> results = reader.lines()
                    .filter(line -> line.toLowerCase().contains(query))
                    .collect(Collectors.toList());

            if (results.isEmpty()) {
                resultArea.setText("No matching records found.");
            } else {
                resultArea.setText(String.join("\n", results));
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search data!");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
}