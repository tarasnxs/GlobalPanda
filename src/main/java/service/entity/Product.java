package service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String barcode;
    private String category_name;
    private String hidden;
    private String unit;
    private String cost;
    private String fiscal;
    private String menu_category_id;
    private String workshop;
    private String nodiscount;
    private String photo;
    private String photo_origin;
    private String product_code;
    private String product_id;
    private String product_name;
    private String sort_order;
    private String tax_id;
    private String product_tax_id;
    private String type;
    private String weight_flag;
    private String color;

    public Product() {
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getFiscal() {
        return fiscal;
    }

    public void setFiscal(String fiscal) {
        this.fiscal = fiscal;
    }

    public String getMenu_category_id() {
        return menu_category_id;
    }

    public void setMenu_category_id(String menu_category_id) {
        this.menu_category_id = menu_category_id;
    }

    public String getWorkshop() {
        return workshop;
    }

    public void setWorkshop(String workshop) {
        this.workshop = workshop;
    }

    public String getNodiscount() {
        return nodiscount;
    }

    public void setNodiscount(String nodiscount) {
        this.nodiscount = nodiscount;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto_origin() {
        return photo_origin;
    }

    public void setPhoto_origin(String photo_origin) {
        this.photo_origin = photo_origin;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getTax_id() {
        return tax_id;
    }

    public void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }

    public String getProduct_tax_id() {
        return product_tax_id;
    }

    public void setProduct_tax_id(String product_tax_id) {
        this.product_tax_id = product_tax_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeight_flag() {
        return weight_flag;
    }

    public void setWeight_flag(String weight_flag) {
        this.weight_flag = weight_flag;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
