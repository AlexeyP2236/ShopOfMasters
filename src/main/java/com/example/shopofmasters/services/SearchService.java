package com.example.shopofmasters.services;

import com.example.shopofmasters.models.Product;
import com.example.shopofmasters.repositories.SearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    private final SearchRepository searchRepository;

    public SearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }


    //названия и категории по возрастанию цены
    public List<Product> searchTitleAndCategoryOrderByPriceAsc (String title, String ot, String Do, int category){
       return searchRepository.findByTitleAndCategoryOrderByPriceAsc(title.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), category);
    }
    //названия по возрастанию цены
    public List<Product> searchTitleOrderByPriceAsc (String title, String ot, String Do){
        return searchRepository.findByTitleOrderByPriceAsc(title.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do));
    }

    //названия и категории по убыванию цены
    public List<Product> searchTitleAndCategoryOrderByPriceDesc(String title, String ot, String Do, int category){
        return searchRepository.findByTitleAndCategoryOrderByPriceDesc(title.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), category);

    }

    //названия по убыванию цены
    public List<Product> searchTitleOrderByPriceDesc (String title, String ot, String Do){
        return searchRepository.findByTitleOrderByPriceDesc(title.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do));
    }

    //(перевод) найдите по Названию И Цене Больше, Чем Равно, И По Цене Меньше, Чем Равно
    public List<Product> searchTitleAndPriceGreaterThanAndPriceLessThan(String title, String ot, String Do){
        return searchRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(title.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do));
    }
    //(перевод) найти по названию, содержащему регистр игнорирования
    public List<Product> searchTitleContainingIgnoreCase(String title){
        return searchRepository.findByTitleContainingIgnoreCase(title);
    }
}
