package org.example.warehouse;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private final String name;
    private final static Map<String, Category> categories = new HashMap<>();

    private Category(String name) {
        this.name = capitalize(name);
    }

    public static Category of(String name) {
        if (name == null) throw new IllegalArgumentException("Category name can't be null");
        String capitalizedName = capitalize(name);
        if (categories.containsKey(capitalizedName)) {
            return categories.get(capitalizedName);
        } else {
            Category category = new Category(capitalizedName);
            categories.put(capitalizedName, category);
            return category;
        }
    }

    public String getName() {
        return name;
    }

    private static String capitalize(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
