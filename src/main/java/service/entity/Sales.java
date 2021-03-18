package service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sales {
    private String product_id;
    private String product_name;
    private String modification_id;
    private String modification_name;
    private String category_id;
    private float count;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getModification_id() {
        return modification_id;
    }

    public void setModification_id(String modification_id) {
        this.modification_id = modification_id;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getModification_name() {
        return modification_name;
    }

    public void setModification_name(String modification_name) {
        this.modification_name = modification_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
