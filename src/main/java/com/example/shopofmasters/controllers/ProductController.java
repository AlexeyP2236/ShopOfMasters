package com.example.shopofmasters.controllers;

import com.example.shopofmasters.services.ProductService;
import com.example.shopofmasters.services.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("product")
public class ProductController {

    private final SearchService searchService;
    private  final ProductService productService;

    public ProductController(SearchService searchService, ProductService productService) {
        this.searchService = searchService;
        this.productService = productService;
    }

    //вывод списка товаров
    @GetMapping("")
    public String getAllProduct(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "/product/product";
    }

    //вывод товара по id
    @GetMapping("/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("product", productService.getProductId(id));
        return "/product/infoProduct";
    }

    @PostMapping("/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("ot") String ot, @RequestParam("do") String Do, @RequestParam(value = "price", required = false, defaultValue = "") String price, @RequestParam(value = "contract", required = false, defaultValue = "")String contract, Model model){
        model.addAttribute("products", productService.getAllProduct());

        if(!ot.isEmpty() & !Do.isEmpty()){
            if(!price.isEmpty()){
                if(price.equals("sorted_by_ascending_price")) {
                    if (!contract.isEmpty()) {
                        if (contract.equals("furniture")) {
                            model.addAttribute("search_product", searchService.searchTitleAndCategoryOrderByPriceAsc(search, ot, Do, 1));
                        } else if (contract.equals("appliances")) {
                            model.addAttribute("search_product", searchService.searchTitleAndCategoryOrderByPriceAsc(search, ot, Do, 3));
                        } else if (contract.equals("clothes")) {
                            model.addAttribute("search_product", searchService.searchTitleAndCategoryOrderByPriceAsc(search, ot, Do, 2));
                        }
                    } else {
                        model.addAttribute("search_product", searchService.searchTitleOrderByPriceAsc(search, ot, Do));
                    }
                } else if(price.equals("sorted_by_descending_price")){
                    if(!contract.isEmpty()){
                        System.out.println(contract);
                        if(contract.equals("furniture")){
                            model.addAttribute("search_product", searchService.searchTitleAndCategoryOrderByPriceDesc(search, ot, Do, 1));
                        }else if (contract.equals("appliances")) {
                            model.addAttribute("search_product", searchService.searchTitleAndCategoryOrderByPriceDesc(search, ot, Do, 3));
                        } else if (contract.equals("clothes")) {
                            model.addAttribute("search_product", searchService.searchTitleAndCategoryOrderByPriceDesc(search, ot, Do, 2));
                        }
                    }  else {
                        model.addAttribute("search_product", searchService.searchTitleOrderByPriceDesc(search, ot, Do));
                    }
                }
            } else {
                model.addAttribute("search_product", searchService.searchTitleAndPriceGreaterThanAndPriceLessThan(search, ot, Do));
            }
        } else {
            model.addAttribute("search_product", searchService.searchTitleContainingIgnoreCase(search));
        }

        model.addAttribute("value_search", search);
        model.addAttribute("value_price_ot", ot);
        model.addAttribute("value_price_do", Do);
        return "/product/product";

    }
}

