package com.example.cocoonshop.ModelClasses.RecyclerModels;

public class CategoryItem {

    private String categoryName;

    private String categoryImagePath;

    //---------------------------------------------------------------------r----------------------------------------------------


    public CategoryItem(String categoryName, String categoryImagePath) {
        this.categoryName = categoryName;
        this.categoryImagePath = categoryImagePath;
    }

    public CategoryItem() {
    }

    //-------------------------------------------------------------------------------------------------------------------------


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImagePath() {
        return categoryImagePath;
    }

    public void setCategoryImagePath(String categoryImagePath) {
        this.categoryImagePath = categoryImagePath;
    }
}
