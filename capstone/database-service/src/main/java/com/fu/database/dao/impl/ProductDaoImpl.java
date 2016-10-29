package com.fu.database.dao.impl;

import com.amazonaws.services.directory.model.EntityAlreadyExistsException;
import com.fu.common.constant.KeyConstant;
import com.fu.database.dao.ProductDao;
import com.fu.database.entity.Product;
import com.fu.database.model.ProductApi;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manlm on 9/21/2016.
 */
@Repository
public class ProductDaoImpl extends GenericDaoImpl<Product, Long> implements ProductDao {

    private static final Logger LOG = Logger.getLogger(ProductDaoImpl.class);

    @Override
    public Product getProductById(long productId) {
        LOG.info("[getProductById] Start: productId = " + productId);
        LOG.info("[getProductById] End");
        return getEntityManager().createQuery("SELECT c FROM Product c WHERE c.id=:id ", Product.class)
                .setParameter("id", productId)
                .getSingleResult();
    }

    @Override
    public List<Product> getProductBySearchName(String name, int positionInResult, int maxShowResult) {
        LOG.info("[getProductBySearchName] Start: name = " + name);
        List<Product> product = getEntityManager().createQuery("SELECT c FROM Product c WHERE LOWER(c.name)=:name ", Product.class)
                .setParameter("name", name.toLowerCase())
                .getResultList();

        if (!product.isEmpty()) {
            return product;
        }

        LOG.info("[getProductBySearchName] End");
        return getEntityManager().createQuery("SELECT c FROM Product c WHERE LOWER(c.name)  LIKE :name  ORDER BY c.name  ", Product.class)
                .setParameter("name", name.toLowerCase() + "%")

                .setFirstResult(positionInResult)
                .setMaxResults(maxShowResult)
                .getResultList();

    }

    @Override
    public List<Product> getProductInCart(List<Long> listProductId) {
        LOG.info("[getProductInCart] Start: listProductId size = " + listProductId.size());

        String strId = "p.id=:";
        String strOr = " OR ";
        StringBuilder hql = new StringBuilder("FROM Product p WHERE ");

        hql.append(strId).append("id0");

        for (int i = 1; i < listProductId.size(); i++) {
            hql.append(strOr).append(strId).append("id").append(i);
        }
        hql.append(" ORDER BY p.areaWeight");
        Query query = getEntityManager().createQuery(String.valueOf(hql));

        for (int i = 0; i < listProductId.size(); i++) {
            query.setParameter("id" + i, listProductId.get(i));
        }

        LOG.info("[getProductInCart] End");
        return query.getResultList();
    }

    @Override
    public List<Product> getProductPromotion() {
        LOG.info("[getProductPromotion] Start");
        LOG.info("[getProductPromotion] End");
        return getEntityManager().createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }

    @Override
    public String getNameProduct(long productId) {
        LOG.info("[getNameProduct] Start: productId =" + productId);
        LOG.info("[getNameProduct] End");
        return getEntityManager().createQuery("SELECT p.name FROM Product p WHERE p.id=:productId", String.class)
                .setParameter("productId", productId)
                .getSingleResult();
    }

    @Override
    public List<Long> getIdsByBeaconMinor(int minor) {
        LOG.info("[getIdsByBeaconMinor] Start: minor =" + minor);
        LOG.info("[getIdsByBeaconMinor] End");
        return getEntityManager().createQuery("SELECT c.id FROM Product c WHERE c.beaconMinor = :minor", Long.class)
                .setParameter("minor", minor)
                .getResultList();
    }

    @Override
    public List<Product> getPromotionBaseOnWeightHistory(String userId) {
        LOG.info("[getPromotionBaseOnWeightHistory] Start");
        LOG.info("[getPromotionBaseOnWeightHistory] End");
        return getEntityManager().createQuery("SELECT p FROM WeightedHistory w inner join Product p ON p.id=w.productId WHERE w.botFbId=:userId AND p.id IN (SELECT pp.promotionProductKey.productId FROM PromotionProduct pp) ORDER BY w.weight desc ")
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<ProductApi> getProductForApi() {
        LOG.info("[getProductForApi] Start");

        List<Product> productList = getEntityManager().createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();

        List<ProductApi> productApiList = new ArrayList<>();
        ProductApi productApi;
        for (Product product : productList) {
            productApi = new ProductApi();
            productApi.setId(Math.toIntExact(product.getId()));
            productApi.setName(product.getName());
            productApi.setAreaName(product.getAreaName());
            productApi.setAreaLocation(product.getAreaLocation());
            productApi.setAreaWeight(product.getAreaWeight());
            productApi.setBeaconMinor1(product.getBeaconMinor1());
            productApi.setBeaconMinor2(product.getBeaconMinor2());
            productApi.setBeaconMinor3(product.getBeaconMinor3());
            productApi.setBeaconMinor4(product.getBeaconMinor4());
            productApiList.add(productApi);
        }

        LOG.info("[getProductForApi] End");
        return productApiList;
    }

    @Override
    public List<Product> getSuggestProductById(long productId) {
        return getEntityManager().createQuery("SELECT p FROM Product p WHERE p.id IN (SELECT r.relatingProductKey.productId2 FROM RelatingProduct r WHERE r.relatingProductKey.productId1=:productId)", Product.class)
                .setParameter("productId", productId).setMaxResults(KeyConstant.MAX_SHOW).getResultList();
    }

    @Override
    public boolean checkSuggestProductById(long productId) {

        List<Long> check = getEntityManager().createQuery("SELECT r.relatingProductKey.productId2 FROM RelatingProduct r WHERE r.relatingProductKey.productId1=:productId", Long.class)
                .setParameter("productId", productId).getResultList();
        if (!check.isEmpty()) {
            return true;
        }
        return false;
    }
    @Override
    public List<Product> getAllProduct() {
        LOG.info("[getAllProduct] Start:");
        LOG.info("[getAllProduct] End");
        return getEntityManager().createQuery("SELECT c FROM Product c ORDER BY c.name", Product.class)
                .getResultList();
    }

    @Override
    public boolean checkDuplicateCode(String code) {
        LOG.info("[checkDuplicateCode] Start:");
            List<Product> product=
            getEntityManager().createQuery("SELECT p FROM Product p WHERE p.code=:code", Product.class)
                    .setParameter("code", code)
                    .getResultList();
            if(!product.isEmpty()){
                LOG.info("[checkDuplicateCode] End");
                return  true;
            }
        LOG.info("[checkDuplicateCode] End");
            return false;
    }
}
