/**
 *
 */
package gui;

import java.net.URL;
import java.util.*;

import com.sun.javafx.scene.control.skin.LabeledText;
import com.sun.javafx.scene.control.skin.TableCellSkin;

import main.Goods;
import main.Report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * @author nashetovich
 */
public class GoodsList implements Initializable {
    @FXML
    TableView<Goods> goodsTable;
    @FXML
    TableColumn<Goods, String> colName;
    @FXML
    TableColumn<Goods, Float> colConsumption;
    @FXML
    TableColumn<Goods, Float> colRealSupply;
    @FXML
    TableColumn<Goods, Float> colActualBought;
    @FXML
    TableColumn<Goods, Float> colAffordable;
    @FXML
    TableColumn<Goods, Float> colMaxDemand;
    @FXML
    TableColumn<Goods, Float> colBasePrice;
    @FXML
    TableColumn<Goods, Float> colMinPrice;
    @FXML
    TableColumn<Goods, Float> colMaxPrice;
    @FXML
    TableColumn<Goods, Float> colPrice;
    @FXML
    TableColumn<Goods, String> colTrend;
    @FXML
    TableColumn<Goods, Float> colOverproduced;
    @FXML
    TableColumn<Goods, Float> colInflation;
    @FXML
    TableColumn<Goods, Float> colFluctuation;

    private final ObservableList<Goods> goodsTableContent = FXCollections.observableArrayList();

    private Report thisSave;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colConsumption.setCellValueFactory(new PropertyValueFactory<>("Consumption"));
        colRealSupply.setCellValueFactory(new PropertyValueFactory<>("RealSupply"));
        colActualBought.setCellValueFactory(new PropertyValueFactory<>("ActualBought"));
        colActualBought.setVisible(false);
        colAffordable.setCellValueFactory(new PropertyValueFactory<>("Affordable"));
        colMaxDemand.setCellValueFactory(new PropertyValueFactory<>("MaxDemand"));
        colBasePrice.setCellValueFactory(new PropertyValueFactory<>("BasePrice"));
        colMinPrice.setCellValueFactory(new PropertyValueFactory<>("MinPrice"));
        colMaxPrice.setCellValueFactory(new PropertyValueFactory<>("MaxPrice"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        colTrend.setCellValueFactory(new PropertyValueFactory<>("Trend"));
        colOverproduced.setCellValueFactory(new PropertyValueFactory<>("Overproduced"));
        colInflation.setCellValueFactory(new PropertyValueFactory<>("Inflation"));
        colFluctuation.setCellValueFactory(new PropertyValueFactory<>("Fluctuation"));
    }

    public TableView<Goods> getGoodsTable() {
        return goodsTable;
    }

    public void setReport(Report in) {
        thisSave = in;
    }

    public Goods totalGoods() {
        Goods totalGoods = new Goods("Total (pounds)");
        for (Goods goods : thisSave.goodsByName().values()) {
            totalGoods.supply += goods.supply * goods.price;
            totalGoods.consumption += goods.consumption * goods.price;
            totalGoods.affordable += goods.affordable * goods.price;
            totalGoods.maxDemand += goods.maxDemand * goods.price;
            totalGoods.basePrice += goods.basePrice;
            totalGoods.price += goods.price;
            totalGoods.actualBought += goods.actualBought * goods.price;
        }

        return totalGoods;
    }

    public void fillGoodsTable(Collection<Goods> allGoods) {
        goodsTableContent.clear();

        final Goods total = totalGoods();

        total.basePrice = total.basePrice / allGoods.size();
        total.price = total.price / allGoods.size();

        goodsTableContent.addAll(allGoods);
        //Collections.sort(total);
        goodsTableContent.add(total);

        Comparator<Goods> comparator = Goods.nameSort;
        Collections.sort(goodsTableContent, comparator);
		
		/*for(int i=0;i<thisSave.countries.size();i++){
            //mainTable.getItems().add(Game.saveGame.countries.get(i));
			countryTableContent.add(thisSave.countries.get(i));

		}	*/

        goodsTable.setItems(goodsTableContent);
    }

    public final void onTableClicked(MouseEvent event) {
        String foundName = null;
        if (event != null && event.getTarget() != null) {
            if (event.getTarget() instanceof TableCellSkin) {
                foundName = ((TableCellSkin) event.getTarget()).bindings.getText();
            }
            if (event.getTarget() instanceof LabeledText) {
                foundName = ((LabeledText) event.getTarget()).getText();
            }
            if (foundName != null && !foundName.isEmpty()) {
                if (!foundName.equalsIgnoreCase("Total (pounds)")) {
                    Goods foundGoods = thisSave.findGoods(foundName);

                    if (foundGoods != null) {
                        GUIGoods window = new GUIGoods(thisSave, thisSave.countries, foundGoods);
                    }
                }
            }
        }
    }

}
