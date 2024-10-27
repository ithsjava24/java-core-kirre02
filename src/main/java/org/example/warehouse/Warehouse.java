package org.example.warehouse;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Warehouse {
    private final String name;
    private final List<ProductRecord> products = new ArrayList<>();
    private final static Map<String, Warehouse> warehouses = new HashMap<>();
    private final List<ProductRecord> changedProducts = new ArrayList<>();

    private Warehouse(String name) {
        this.name = name;
    }

    private Warehouse() {
        this.name = "default";
    }

    public static Warehouse getInstance() {
        return new Warehouse();
    }

    public static Warehouse getInstance(String name) {
        if (warehouses.containsKey(name)) {
            return warehouses.get(name);
        } else {
            Warehouse warehouse = new Warehouse(name);
            warehouses.put(name, warehouse);
            return warehouse;
        }
    }


    public boolean isEmpty() {
        return products.isEmpty();
    }

    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Product name can't be null or empty.");
        if (category == null) throw new IllegalArgumentException("Category can't be null.");

        id = Objects.requireNonNullElse(id, UUID.randomUUID());

        for (ProductRecord product : products) {
            if (id.equals(product.uuid()))
                throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }

        BigDecimal finalPrice = Objects.requireNonNullElse(price, BigDecimal.ZERO);

        ProductRecord newProduct = new ProductRecord(id, name, category, finalPrice);
        products.add(newProduct);

        return newProduct;
    }

    public List<ProductRecord> getProducts() {
        return List.copyOf(products);
    }

    public Optional<ProductRecord> getProductById(UUID uuid) {
        for (ProductRecord product : products) {
            if (product.uuid().equals(uuid)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    public List<ProductRecord> getProductsBy(Category category) {
        return products.stream()
        .filter(product -> product.category().equals(category))
        .collect(Collectors.toList());
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return products.stream().collect(Collectors.groupingBy(ProductRecord::category));
    }

    public List<ProductRecord> getChangedProducts() {
        return List.copyOf(changedProducts);
    }

    public void updateProductPrice(UUID uuid, BigDecimal price) {
        for (int i = 0; i < products.size(); i++) {
            ProductRecord product = products.get(i);
            if (product.uuid().equals(uuid)) {
                changedProducts.add(product);
                products.set(i, new ProductRecord(product.uuid(), product.name(), product.category(), price));
                return;
            }
        }
        throw new IllegalArgumentException("Product with that id doesn't exist.");
    }
}

