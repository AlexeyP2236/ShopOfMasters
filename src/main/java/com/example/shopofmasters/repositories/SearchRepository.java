package com.example.shopofmasters.repositories;

import com.example.shopofmasters.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SearchRepository extends JpaRepository<Product, Integer> {
    // Поиск всех продуктов по части наименования продукта в не зависомости от регистра
    List<Product> findByTitleContainingIgnoreCase(String name);

    // Поиск по наименованию и фильтрация по диапазону цены
    //Error: Ordinal parameters did not start with base 1 : 2
    //Странно, но работает без кавычек, с кавычками выдает ошибку
    @Query(value = "SELECT * FROM product WHERE (lower(title) LIKE %?1%) AND price >= ?2 AND price <= ?3", nativeQuery = true) // убрал OR (lower(title) LIKE %?1) OR (lower(title) LIKE ?1%)
    List<Product> findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(String title, float ot, float Do);

    // Поиск по наименованию и фильтрация по диапазону цены, а также сортировка по возрастанию цены
    @Query(value = "select * from product where (lower(title) LIKE %?1%) and (price >= ?2 and price <= ?3) order by price",nativeQuery = true)
    List<Product> findByTitleOrderByPriceAsc(String title, float ot, float Do);

    // Поиск по наименованию и фильтрация по диапазону цены, а также сортировка по убыванию цены
    @Query(value = "select * from product where (lower(title) LIKE %?1%) and (price >= ?2 and price <= ?3) order by price desc", nativeQuery = true)
    List<Product> findByTitleOrderByPriceDesc(String title, float ot, float Do);

    // Поиск по наименованию и фильтрация по диапазону цены, сортировка по возрастанию цены,  а также фильтрация по категории
    @Query(value = "select * from product where category_id = ?4 and(lower(title) LIKE %?1%) and (price >= ?2 and price <= ?3) order by price", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceAsc(String title, float ot, float Do, int category);

    // Поиск по наименованию и фильтрация по диапазону цены, сортировка по убыванию цены,  а также фильтрация по категории
    @Query(value = "select * from product where category_id = ?4 and(lower(title) LIKE %?1%) and (price >= ?2 and price <= ?3) order by price desc", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceDesc(String title, float ot, float Do, int category);
}
