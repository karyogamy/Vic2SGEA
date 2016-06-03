package gui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.Country;
import main.Goods;
import main.GoodsStorage;
import main.Report;

public class GUIGoods {
    GridPane grid;

    void addUniChart(final Report               inReport,
                     final ArrayList<Country>   inCountry,
                     final Goods                inGoods,
                     final String               dataField,
                     final int                  column,
                     final int                  row,
                     final String               name) throws ReflectiveOperationException, SecurityException {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        //Collections.sort(inGoods.);

        final Field storageData = GoodsStorage.class.getField(dataField);
        //Field goodsData=Goods.class.getField(dataField);

        float total = 0;
        float totalSum = 0;

        //Comparator comparator= new Goods.nameSort();
        //Collections.sort(goodsTableContent,comparator);

        for (Country everyCountry : inCountry) {
            if (!everyCountry.getOfficialName().equalsIgnoreCase("Total"))
                for (GoodsStorage everyStorage : everyCountry.storage) {
                    if (everyStorage.item == inGoods) {
                        PieChart.Data temp = new PieChart.Data(everyCountry.getOfficialName(), storageData.getFloat(everyStorage));
                        pieChartData.add(temp);
                        total += storageData.getFloat(everyStorage);
                    }
                }
        }

        //Collections.sort(pieChartData);

        final PieChart chart = new PieChart(pieChartData);
        //chart.setLabelsVisible(false);
        //chart.setScaleX(1.6);
        //chart.setScaleY(1.6);
        totalSum = total * inGoods.price;

        chart.setTitle(name + inGoods.getName() + " (" + (long) total + " items by " + (long) totalSum + "£)");
        /*float result=0;
		for (Goods storageData.get(everyStorage): inReport.availableGoods){
			if (everygood==inGoods){
				result+=storageData.get(everyStorage).
			}

		}*/


        chart.setStartAngle(90);
        chart.setLegendVisible(false);
        chart.setLabelsVisible(false);

        //chart.minHeightProperty().set(400);
        //chart.minWidthProperty().set(400);


        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 20 arial;");

        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED,
                    //MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            //caption.setTranslateX(e.getSceneX());
                            // caption.setTranslateY(e.getSceneY());
                            //GoodsStorage temp=inCountry.findStorage(data.getName());
                            //if (temp!=null)	{
                            //String toFill=data.getName()+": "+String.valueOf((long)data.getPieValue())+" pounds "+String.valueOf((long)temp.savedCountrySupply)+ " items";
                            //String toFill=data.getName()+": "+String.valueOf((long)data.getPieValue())+"£ ("+String.valueOf((long)temp.actualSupply)+ " items, consumption is "+String.valueOf((long)temp.actualDemand+")");

                            caption.setText(data.getName() + ": " + String.valueOf((long) data.getPieValue()) + " items");
                            //caption.getClass();
                            //}
                            //else caption.setText("findStorage(data.getName()) returned NULL");
                        }
                    });
        }
        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
                    //MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            for (Country everyCountry : inCountry) {
                                if (everyCountry.getOfficialName().equalsIgnoreCase(data.getName())) {
                                    GUICountry Window = new GUICountry(inReport, everyCountry);
                                    break;
                                }
                            }
                        }
                    });
        }


        grid.add(chart, column, row);
        grid.add(caption, column, row + 1);
    }

    GUIGoods(final Report inReport, final ArrayList<Country> inCountry, Goods inGoods) {
        Stage thisStage = new Stage();

        thisStage.setTitle(inGoods.getName() + " - Goods window");
        thisStage.getIcons().add(new Image("/flags/EST.png")); /* Cause I'm Estonian, thats why */

        grid = new GridPane();

        //grid.setAlignment(Pos.CENTER);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        try {
            addUniChart(inReport, inCountry, inGoods, "actualSupply", 0, 0, "Producers of ");
            addUniChart(inReport, inCountry, inGoods, "actualDemand", 0, 2, "Consumers of ");
            addUniChart(inReport, inCountry, inGoods, "export", 1, 0, "Exporters of ");
            addUniChart(inReport, inCountry, inGoods, "imported", 1, 2, "Importers of ");
            //addUniChart(inReport, inCountry,inGoods, "MaxDemand",2,0, "MaxDemand ");
//            addUniChart(inReport, inCountry, inGoods, "worldmarketPool", 0, 4, "worldmarketPool ");
//            addUniChart(inReport, inCountry, inGoods, "actualSoldWorld", 1, 4, "actualSoldWorld ");
            addUniChart(inReport, inCountry, inGoods, "savedCountrySupply", 0, 6, "Stockpiles of ");
        } catch (SecurityException | ReflectiveOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);

		/*scrollPane.setFitToHeight(false);
		scrollPane.setFitToWidth(true);
		scrollPane.autosize();*/

        Scene scene = new Scene(scrollPane, 1200, 950);


        thisStage.setScene(scene);

        thisStage.show();
        //Game.countryStage.setScene(scene);
        //Game.countryStage.show();
    }
}
