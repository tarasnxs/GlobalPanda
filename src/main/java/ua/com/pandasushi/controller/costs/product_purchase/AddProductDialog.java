package ua.com.pandasushi.controller.costs.product_purchase;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ua.com.pandasushi.controller.inputconvert.FloatFieldChangeListener;
import ua.com.pandasushi.controller.inputconvert.IntFieldChangeListener;
import ua.com.pandasushi.database.common.menu.INGREDIENTS;
import ua.com.pandasushi.database.common.menu.PRODUCTS;
import ua.com.pandasushi.database.common.menu.PRODUCTS_INGREDIENTS;
import ua.com.pandasushi.main.GlobalPandaApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Тарас on 27.01.2017.
 */
public class AddProductDialog {
    TextField nameProd;
    ComboBox<String> unit1;
    ComboBox<String> unit2;
    TextField relation;
    ComboBox<String> ingredient;
    TextField coef;
    Button cancel;
    Button add;
    String name;
    ArrayList<INGREDIENTS> ingredients;
    ArrayList<PurchaseTab> tabs;
    Stage dialog;

    public AddProductDialog(String name, ArrayList<PurchaseTab> tabs) {
        this.name = name;
        this.tabs = tabs;
        ingredients = GlobalPandaApp.site.getIngredients();
        dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initModality(Modality.APPLICATION_MODAL);
        AnchorPane parent = null;
        try {
            parent = FXMLLoader.load(AddProductDialog.class.getResource("/view/main/costs/product_input.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = null;
        if(parent != null) {
            scene = new Scene(parent, 300, 350);
        }

        if(scene != null) {
            nameProd = (TextField) scene.lookup("#nameProd");
            unit1 = (ComboBox) scene.lookup("#unit1");
            unit2 = (ComboBox) scene.lookup("#unit2");
            relation = (TextField) scene.lookup("#relation");
            ingredient = (ComboBox) scene.lookup("#ingredient");
            coef = (TextField) scene.lookup("#coef");
            cancel = (Button) scene.lookup("#cancel");
            add = (Button) scene.lookup("#add");
            setFields();
            dialog.setScene(scene);
            dialog.show();
        }
    }

    void setFields () {
        nameProd.setText(name);
        unit1.getItems().addAll(GlobalPandaApp.site.getProdFirstUnits());
        unit2.getItems().addAll("гр", "мл");
        relation.textProperty().addListener(new IntFieldChangeListener(relation));
        relation.setText("0");
        for(INGREDIENTS ing : ingredients)
            ingredient.getItems().add(ing.getIngredientName());
        coef.textProperty().addListener(new FloatFieldChangeListener(coef));
        coef.setText("1.0");
        coef.setEditable(false);
        coef.setDisable(true);
        cancel.setOnAction(event -> {
            dialog.close();
        });
        add.setOnAction(event -> {
            if ( !nameProd.getText().isEmpty() && unit1.getSelectionModel().getSelectedIndex() > -1 &&
                    unit2.getSelectionModel().getSelectedIndex() > -1 && ingredient.getSelectionModel().getSelectedIndex() > -1) {
                Integer productId = GlobalPandaApp.site.getNextProductId();

                PRODUCTS product = new PRODUCTS();
                product.setProductId(productId);
                product.setAvgPriceCur(1.01f);
                product.setAvgPriceUah(1.01f);
                product.setCurToUah(27.1f);
                product.setCurrency("USD");
                product.setExpiredPeriod(180);
                product.setLastPriceCur(1.01f);
                product.setLastPriceUah(1.01f);
                product.setProductName(nameProd.getText());
                product.setFirstUnits(unit1.getSelectionModel().getSelectedItem());
                product.setSecondUnits(unit2.getSelectionModel().getSelectedItem());
                product.setUnitsRelation(Integer.parseInt(relation.getText()));
                product.setChecked(false);

                PRODUCTS_INGREDIENTS prodIng = new PRODUCTS_INGREDIENTS();
                prodIng.setProdIngId(GlobalPandaApp.site.getNextProdIngId());
                prodIng.setProductId(productId);
                prodIng.setProductName(nameProd.getText());
                String ingredientName = ingredient.getSelectionModel().getSelectedItem();
                prodIng.setIngredientName(ingredientName);
                for (INGREDIENTS ing : ingredients)
                    if(ing.getIngredientName().equals(ingredientName)) {
                        prodIng.setIngredientId(ing.getIngredientId());
                        break;
                    }
                prodIng.setAuto(Integer.parseInt(relation.getText()) > 0 ? true : false);
                prodIng.setUnits(unit2.getSelectionModel().getSelectedItem());
                prodIng.setLastCoef(Float.parseFloat(coef.getText()));
                prodIng.setAvgCoef(Float.parseFloat(coef.getText()));
                prodIng.setAvgPrice(1.0f);
                prodIng.setLastCheck(new Date());

                GlobalPandaApp.site.save(product);
                GlobalPandaApp.site.save(prodIng);

                for( PurchaseTab tab : tabs)
                    tab.reloadProducts();
                dialog.close();
            }
        });
    }
}
