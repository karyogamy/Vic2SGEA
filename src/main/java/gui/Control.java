/**
 *
 */
package gui;


import java.io.File;

import java.net.URL;


import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;


import com.sun.javafx.scene.control.skin.LabeledText;
import com.sun.javafx.scene.control.skin.TableCellSkin;

import eug.shared.GenericObject;
import eug.shared.ObjectVariable;
//import javafx.concurrent.*;


import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;


import main.*;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


/**
 * @author nashet
 */
public class Control implements Initializable {

    @FXML
    Button btnOldLoader;
    @FXML
    Button btnReLoad;
    @FXML
    Button btnLoad;
    @FXML
    Button btnTest;
    @FXML
    Button btnGoods;
    @FXML
    Button btnBrowseSave;
    @FXML
    Button btnBrowseLocal;
    @FXML
    Button btnBrowseMod;
    @FXML
    public Label lblEerror;
    @FXML
    public Label lblStartDate;
    @FXML
    public Label lblCurrentDate;
    @FXML
    public Label lblPlayer;
    @FXML
    public Label lblPopCount;
    @FXML
    TableView<Country> mainTable;
    @FXML
    TextField tfSaveGame;
    @FXML
    TextField tfLocalization;
    @FXML
    TextField tfModPatch;

    @FXML
    ProgressIndicator piLoad;

    final ObservableList<Country> countryTableContent = FXCollections.observableArrayList();
    @FXML
    TableColumn<Country, String> colCountry;
    @FXML
    // TableColumn<Country, Long> colPopulation;
            TableColumn<Country, String> colPopulation;
    @FXML
    TableColumn<Country, String> colGDP;
    @FXML
    TableColumn<Country, Double> colConsuption;
    @FXML
    TableColumn<Country, String> colGDPPer;
    @FXML
    TableColumn<Country, String> colGDPPCNominal;
    @FXML
    TableColumn<Country, Integer> colGDPPlace;
    @FXML
    TableColumn<Country, String> colGDPPart;
    @FXML
    TableColumn<Country, String> colGDPNominal;
    @FXML
    TableColumn<Country, Long> colGoldIncome;
    @FXML
    TableColumn<Country, Long> colWorkforce;
    @FXML
    TableColumn<Country, Long> colEmployment;
    @FXML
    TableColumn<Country, Long> colExport;
    @FXML
    TableColumn<Country, Long> colImport;
    @FXML
    TableColumn<Country, String> colUnemploymentProcent;
    @FXML
    TableColumn<Country, String> colUnemploymentProcentFactory;


    public void fillMainTable() {
        countryTableContent.clear();
//
//        for (Country iterator : report.countries) {
//            if (iterator.population > 0)
//                countryTableContent.add(iterator);
//        }

        report.countries.stream().filter(country -> country.population > 0).forEach(countryTableContent::add);
        mainTable.setItems(countryTableContent);
    }

    public Label getErrorLabel() {
        return lblEerror;
    }

    private Control self;

    public ObservableList<Country> getcountryTableContent() {
        return countryTableContent;
    }

    public TableView<Country> getMainTable() {
        return mainTable;
    }

    private Report report;// = new SaveGame();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub

		/* Listening to selections in warTable */
        // final ObservableList<MyCountry> warTableSelection = mainTable.getSelectionModel().getSelectedItems();
        //warTableSelection.addListener(tableSelectionChanged);


        colCountry.setCellValueFactory(new PropertyValueFactory<>("officialName"));
        //colPopulation.setCellValueFactory(new PropertyValueFactory<>("population"));
        colPopulation.setCellValueFactory(new PropertyValueFactory<>("PopulationTV"));
        colGDP.setCellValueFactory(new PropertyValueFactory<>("actualSupply"));
        colConsuption.setCellValueFactory(new PropertyValueFactory<>("actualDemand"));
        colConsuption.setVisible(false);
        colGDPPer.setCellValueFactory(new PropertyValueFactory<>("GDPPerCapitaTV"));
        colGDPPCNominal.setCellValueFactory(new PropertyValueFactory<>("GDPNominalPerCapita"));
        colGDPPlace.setCellValueFactory(new PropertyValueFactory<>("GDPPlace"));
        colGDPPart.setCellValueFactory(new PropertyValueFactory<>("GDPPartTV"));
        colGDPNominal.setCellValueFactory(new PropertyValueFactory<>("GDPNominal"));
        colGoldIncome.setCellValueFactory(new PropertyValueFactory<>("goldIncome"));

        colWorkforce.setCellValueFactory(new PropertyValueFactory<>("workforce"));
        colWorkforce.setVisible(false);
        //
        colEmployment.setCellValueFactory(new PropertyValueFactory<>("employment"));
        colEmployment.setVisible(false);
        colExport.setCellValueFactory(new PropertyValueFactory<>("export"));
        colExport.setVisible(false);
        colImport.setCellValueFactory(new PropertyValueFactory<>("imported"));
        colImport.setVisible(false);

        colUnemploymentProcent.setCellValueFactory(new PropertyValueFactory<>("UnemploymentProcentTV"));
        colUnemploymentProcentFactory.setCellValueFactory(new PropertyValueFactory<>("unemploymentProcentFactoryTV"));


        PathKeeper.pathLoader();
        tfLocalization.setText(PathKeeper.LOCALIZATIONPATCH);
        tfSaveGame.setText(PathKeeper.SAVEGAMEPATH);
        tfModPatch.setText(PathKeeper.MODPATCH);

        lblPlayer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (report != null) {
                    String country = lblPlayer.getText().replaceFirst(" :playeR", "");
                    Country foundCountry = report.findCountry(country);
                    if (foundCountry != null)
                        new GUICountry(report, foundCountry);
                }
            }
        });

        colUnemploymentProcentFactory.setComparator(PercentComparator);
        colGDPPer.setComparator(KMGComparator);
        colGDPPart.setComparator(PercentComparator);
        colGDP.setComparator(KMGComparator);

        colPopulation.setComparator(KMGComparator);
        colUnemploymentProcent.setComparator(PercentComparator);
        //Game.window.

        self = this;
    }

    private static final Comparator<String> KMGComparator = new Comparator<String>() {
        @Override
        public int compare(String arg0, String arg1) {
            float first = Wrapper.fromKMG(arg0);
            float second = Wrapper.fromKMG(arg1);

            if (first == second) return 0;
            else if (second > first) return 1;
            else return -1;
        }
    };

    private static final Comparator<String> PercentComparator = new Comparator<String>() {
        @Override
        public int compare(String arg0, String arg1) {
            float first = Wrapper.fromPercent(arg0);
            float second = Wrapper.fromPercent(arg1);

            if (first == second) return 0;
            else if (second > first) return 1;
            else return -1;
        }
    };

    public final void onGoods(ActionEvent event) {
        Game.goodsStage.show();
    }

    private void disable() {
        this.btnBrowseLocal.setDisable(true);
        this.btnBrowseSave.setDisable(true);
        this.btnBrowseMod.setDisable(true);
        this.btnGoods.setDisable(true);
        this.btnLoad.setDisable(true);

        this.tfLocalization.setDisable(true);
        this.tfModPatch.setDisable(true);
        this.tfSaveGame.setDisable(true);

        this.mainTable.setDisable(true);
        this.lblPlayer.setDisable(true);
        this.piLoad.setVisible(true);
    }

    private void enable() {
        this.btnBrowseLocal.setDisable(false);
        this.btnBrowseSave.setDisable(false);
        this.btnBrowseMod.setDisable(false);
        this.btnGoods.setDisable(false);
        this.btnLoad.setDisable(false);

        this.tfLocalization.setDisable(false);
        this.tfModPatch.setDisable(false);
        this.tfSaveGame.setDisable(false);

        this.mainTable.setDisable(false);
        this.lblPlayer.setDisable(false);
        this.piLoad.setVisible(false);
    }

    public final void onLoad(ActionEvent event) {

		Game.goodsStage.hide();
        this.disable();

        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                fillTables();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        self.SetLabels();
                        enable();
                    }
                });

                return 0;
            }
        };
        Thread th = new Thread(task);
        th.start();
    }

    private String freeMemoryString() {
        return Wrapper.toKMG(Runtime.getRuntime().freeMemory());
    }
    
    private Integer fillTables() {
        System.out.println();
        System.out.println("Nash: calc thread started...");
        float startTime = System.nanoTime();
        //float startTime=0;

        try {
            countryTableContent.clear();
            report = new Report();
            report.modPatch = tfModPatch.getText();

            // Read Goods Data
            System.out.println("Nash: reading EUG head... free memory is " + freeMemoryString());
            GenericObject head = report.readSaveHead(tfSaveGame.getText());

            System.out.println("Processing goods... free memory is " + freeMemoryString());
            Game.goodsList.setReport(report);
            Game.goodsList.fillGoodsTable(report.goodsByName().values());

            // Read Country Data
            System.out.println("Nash: reading EUG... free memory is " + freeMemoryString());
            Vic2SaveGameNash save = report.readSaveBody(tfSaveGame.getText(), head);
            System.out.println("Nash: processing POPs... free memory is " + freeMemoryString());
            report.populationProcess(save);

            if (!report.readPrices(tfModPatch.getText()))
                report.readPrices(tfLocalization.getText());

            System.out.println("Nash: reading localizations...  free memory is " + freeMemoryString());
            report.LocalisationReader(tfLocalization.getText());

            PathKeeper.save();

            System.out.println("Nash: processing data...  free memory is " + freeMemoryString());
            report.process();
            System.out.println("Nash: filling table...  free memory is " + freeMemoryString());
            fillMainTable();

            //btnGoods.setDisable(false);

            float res = ((float) System.nanoTime() - startTime) / 1000000000;
            System.out.println("Nash: total time is " + res + " secunds");
        } catch (Exception e) {
            // TODO copy from
            e.printStackTrace();
            System.out.println("Nash: ups... " + e.getLocalizedMessage());
        }

        return 0;
    }

    private void SetLabels() {
        for (ObjectVariable everyHead : report.Head) {
            if (everyHead.varname.equalsIgnoreCase("date"))
                lblCurrentDate.setText("Current date: " + everyHead.getValue());
            if (everyHead.varname.equalsIgnoreCase("player"))
                lblPlayer.setText(report.findOfficalName(everyHead.getValue()) + " :playeR");
            //lblPlayer.setText(everyHead.getValue());
            if (everyHead.varname.equalsIgnoreCase("start_date")) {
                lblStartDate.setText("Start date: " + everyHead.getValue());
                break;
            }
        }
        lblPopCount.setText(report.popCount + " :pop counT");
    }

    public final void onBrowseSave(ActionEvent event) {
        // Throws error when user cancels selection
        // SaveGame saveGame=new SaveGame();
        FileChooser fileChooser = new FileChooser();

        if (!PathKeeper.SAVEGAMEPATH.isEmpty()) {
            //initialFile
            File initialFile = new File(PathKeeper.SAVEGAMEPATH);
            initialFile = new File(initialFile.getParent());

            fileChooser.setInitialDirectory(initialFile);
        }

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String temp = file.getPath().replace("\\", "/"); // Usable path
            tfSaveGame.setText(temp);
            PathKeeper.SAVEGAMEPATH = temp;
        }
    }

    public final void onBrowseLocal(ActionEvent event) {
        // Throws error when user cancels selection
        // SaveGame saveGame=new SaveGame();
        DirectoryChooser dirChooser = new DirectoryChooser();
        File file = new File(PathKeeper.LOCALIZATIONPATCH);
        if (!PathKeeper.LOCALIZATIONPATCH.isEmpty()) {
            dirChooser.setInitialDirectory(file);
        }

        file = dirChooser.showDialog(null);
        if (file != null) {
            String temp = file.getPath().replace("\\", "/"); // Usable path
            tfLocalization.setText(temp);
            PathKeeper.LOCALIZATIONPATCH = temp;
        }
    }

    public final void onBrowseMod(ActionEvent event) {
        // Throws error when user cancels selection
        // SaveGame saveGame=new SaveGame();
        try {
            DirectoryChooser dirChooser = new DirectoryChooser();
            File file = null;
            if (PathKeeper.MODPATCH != null) {
                file = new File(PathKeeper.MODPATCH);
                if (!PathKeeper.MODPATCH.isEmpty()) dirChooser.setInitialDirectory(file);
            }
            file = dirChooser.showDialog(null);
            String temp = file.getPath().replace("\\", "/"); // Usable path
            tfModPatch.setText(temp);
            PathKeeper.MODPATCH = temp;
        } catch (NullPointerException e) {
        }
    }

    public final void onTableClicked(MouseEvent event) {

        this.getClass();

        String offName = null;
        if (event != null && event.getTarget() != null) {
            if (event.getTarget() instanceof TableCellSkin) {
                TableCellSkin fer = null;
                fer = (TableCellSkin) event.getTarget();

                offName = fer.bindings.getText();

                this.getClass();
            }
            if (event.getTarget() instanceof LabeledText) {
                LabeledText fer = null;
                fer = (LabeledText) event.getTarget();

                offName = fer.getText();

                this.getClass();
            }
            if (offName != null && !offName.isEmpty()) {
                //if (!offName.equalsIgnoreCase("Total")){
                Country country = report.findCountry(offName);
                if (country != null) {
                    country.getClass();
                    GUICountry window = new GUICountry(report, country);

                }
            }
        }
    }


}