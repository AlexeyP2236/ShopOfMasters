package com.example.shopofmasters.controllers;

import com.example.shopofmasters.models.Category;
import com.example.shopofmasters.models.Order;
import com.example.shopofmasters.models.Person;
import com.example.shopofmasters.models.Product;
import com.example.shopofmasters.services.CategoryService;
import com.example.shopofmasters.services.OrdersService;
import com.example.shopofmasters.services.PersonService;
import com.example.shopofmasters.services.ProductService;
import com.example.shopofmasters.util.ValidatedDataPerson;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class AdminController {

    private final ProductService productService;
    private final OrdersService ordersService;
    private final PersonService personService;
    private final CategoryService categoryService;

    public AdminController(ProductService productService, OrdersService ordersService, PersonService personService, CategoryService categoryService) {
        this.productService = productService;
        this.ordersService = ordersService;
        this.personService = personService;
        this.categoryService = categoryService;
    }

    @GetMapping("/admin")
    public String admin(Model model)
    {
        //Получение продуктов. Вывод продуктов на странице admin
        model.addAttribute("products", productService.getAllProduct());
        return "admin/indexAdmin";
    }

    @GetMapping("admin/product/add")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        //подгрузка категорий
        model.addAttribute("category", categoryService.getAllCategory());
        return "product/addProduct";
    }


    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult,
                             //принятие файлов
                             @RequestParam("file_one")MultipartFile file_one,
                             @RequestParam("file_two")MultipartFile file_two,
                             @RequestParam("file_three")MultipartFile file_three,
                             @RequestParam("file_four")MultipartFile file_four,
                             @RequestParam("file_five")MultipartFile file_five,
                             //принятие категории
                             @RequestParam("category") Category category, Model model)
            //исключение
            throws IOException {
        model.addAttribute("category", categoryService.getAllCategory());
        if(bindingResult.hasErrors()){
            return "product/addProduct";
        }
        //Добавление и проверка файла
        if(!file_one.isEmpty()){
            productService.addPhotoFile(file_one, product);
        }
        if(!file_two.isEmpty()){
            productService.addPhotoFile(file_two, product);
        }
        if(!file_three.isEmpty()){
            productService.addPhotoFile(file_three, product);
        }
        if(!file_four.isEmpty()){
            productService.addPhotoFile(file_four, product);
        }
        if(!file_five.isEmpty()){
            productService.addPhotoFile(file_five, product);
        }
        productService.saveProduct(product, category);
        return "redirect:/admin";
    }



    @GetMapping("admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return "redirect:/admin";
    }

    @GetMapping("admin/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id){
        //получение тавара по id
        model.addAttribute("product", productService.getProductId(id));
        model.addAttribute("category", categoryService.getAllCategory());
        return "product/editProduct";
    }

    @PostMapping("admin/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id,
                              @RequestParam("file_one")MultipartFile file_one,
                              @RequestParam("file_two")MultipartFile file_two,
                              @RequestParam("file_three")MultipartFile file_three,
                              @RequestParam("file_four")MultipartFile file_four,
                              @RequestParam("file_five")MultipartFile file_five) throws IOException {
        //проверка товара на ощибки и возврат на страницу редактирования если они имеются
        if(bindingResult.hasErrors()){
            return "product/editProduct";
        }
        //Добавление и проверка файла
        if(!file_one.isEmpty()){
            productService.updatePhotoFile(id, product, file_one);
        }
        if(!file_two.isEmpty()){
            productService.updatePhotoFile(id, product, file_two);
        }
        if(!file_three.isEmpty()){
            productService.updatePhotoFile(id, product, file_three);
        }
        if(!file_four.isEmpty()){
            productService.updatePhotoFile(id, product, file_four);
        }
        if(!file_five.isEmpty()){
            productService.updatePhotoFile(id, product, file_five);
        }
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }

    @GetMapping("admin/orders")
    public String infoOrders(Model model){
        model.addAttribute("orders", ordersService.getOrdersList());
        return "/admin/ordersAll";
    }

    @PostMapping("admin/orders/search")
    public String searchOrders(@RequestParam("search") String search, Model model){
        model.addAttribute("orders", ordersService.getOrdersList());
        model.addAttribute("search_order", ordersService.searchOrders(search));
        model.addAttribute("value_search", search);
        return "/admin/ordersAll";
    }

    @GetMapping("admin/orders/edit/{id}")
    public String editOrder(Model model, @PathVariable("id") int id){
        model.addAttribute("order", ordersService.getOrdersId(id));
        return "admin/editOrder";
    }

    @PostMapping("admin/orders/edit/{id}")
    public String editOrder(@ModelAttribute("order") @Valid Order order, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "admin/editOrder";
        }
        ordersService.editOrder(order);
        return "redirect:/admin/orders";
    }

    @GetMapping("admin/person")
    public String getAllPerson(Model model) {
        model.addAttribute("persons", personService.getAllPerson());
        return "/admin/infoPerson";
    }

    @GetMapping("admin/person/{id}")
    public String editPerson(Model model, @PathVariable("id") int id){
        model.addAttribute("person", personService.getPersonId(id));
        return "admin/editPerson";
    }

    @PostMapping("admin/person/{id}")
    public String roleAdmin(@ModelAttribute("person") @Validated(ValidatedDataPerson.class) Person person, BindingResult bindingResult, @PathVariable("id") int id){
        if (bindingResult.hasErrors()){
            return "admin/editPerson";
        }
        personService.rolePerson(person);
        personService.addInfoPerson(id, person);
        personService.confirmedPerson(person);
        return "redirect:/admin/person";
    }
}
