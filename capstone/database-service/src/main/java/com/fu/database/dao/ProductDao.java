package com.fu.database.dao;

import com.fu.database.entity.Product;
import com.fu.database.model.ProductApi;

import java.util.List;

/**
 * Created by manlm on 9/21/2016.
 */
public interface ProductDao extends GenericDao<Product, Long> {

    Product getProductById(long productId);

    List<Product> getProductBySearchName(String name, int positionInResult, int maxShowResult);

    List<Product> getProductInCart(List<Long> listProductId);

    List<Product> getProductPromotion();

    String getNameProduct(long productId);

    List<Long> getIdsByBeaconMinor(int minor);

    List<Product> getPromotionBaseOnWeightHistory(String userId);

    List<ProductApi> getProductForApi();

    List<Product> getSuggestProductById(long productId);

    boolean checkSuggestProductById(long productId);

    List<Product> getAllProduct();

    boolean  checkDuplicateCode(String code);
}
