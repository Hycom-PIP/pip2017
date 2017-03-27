package pl.hycom.pip.messanger.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.hycom.pip.messanger.model.Keyword;
import pl.hycom.pip.messanger.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {


    @Query("Select p from Product p where p not in (:productsForCustomer)")
    public List<Product> findSomeProducts(@Param("productsForCustomer") List<Product> products, Pageable pa);

//    @Query("Select p from Product p inner join ProductKeyword p on pr.id = pk.productId inner join Keyword k " +
//            "on k.id = pk.keywordsId where k.word in (:requiredKeywords)")
//    public List<Product> findProductsWithKeywords(@Param("requiredKeywords") List<Keyword> keywords, Pageable pa);
}
