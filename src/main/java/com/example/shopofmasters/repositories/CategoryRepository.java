package com.example.shopofmasters.repositories;


import com.example.shopofmasters.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    //Возвращает категорию по наименовванию из БД
    com.example.shopofmasters.models.Category findByName(String name);
}

