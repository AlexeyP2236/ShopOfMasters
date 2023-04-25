package com.example.shopofmasters.controllers;

import com.example.shopofmasters.models.*;
import com.example.shopofmasters.repositories.CategoryRepository;
import com.example.shopofmasters.services.OrdersService;
import com.example.shopofmasters.services.PersonService;
import com.example.shopofmasters.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AdminController {

    private final ProductService productService;
    private final OrdersService ordersService;

    private final PersonService personService;

    //путь для хранения файлов
    @Value("${upload.path}")
    private String uploadPath;

    private final CategoryRepository categoryRepository;

    public AdminController(ProductService productService, OrdersService ordersService, PersonService personService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.ordersService = ordersService;
        this.personService = personService;
        this.categoryRepository = categoryRepository;
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
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }


    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult,
                             //принятие файлов
                             @RequestParam("file_one")MultipartFile file_one, @RequestParam("file_two")MultipartFile file_two, @RequestParam("file_three")MultipartFile file_three, @RequestParam("file_four")MultipartFile file_four, @RequestParam("file_five")MultipartFile file_five,
                             //принятие категории
                             @RequestParam("category") int category, Model model)
            //исключение
            throws IOException {
        Category category_db = (Category) categoryRepository.findById(category).orElseThrow();
        //System.out.println(category_db.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/addProduct";
        }
        //Добавление и проверка файла
        if(file_one != null){
            File uploadDir = new File(uploadPath);
            //Проверка на существование директории
            if(!uploadDir.exists()){
                //Создает директорию
                uploadDir.mkdir();
            }
            //Генерация названий файла
            String uuidFile = UUID.randomUUID().toString();
            //Сгенерированное имя + оригинальное имя
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            //отправка файла по указаной директории
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            //Добавление файла
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);

        }

        if(file_two != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_three != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_four != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_five != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five .getOriginalFilename();
            file_five .transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        productService.saveProduct(product, category_db);
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
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";
    }

    @PostMapping("admin/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id, Model model){
        //проверка товара на ощибки и возврат на страницу редактирования если они имеются
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/editProduct";
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
//        Order stat = new Order();
//        stat.setStatus(order);
        ordersService.editOrder(order);
        return "redirect:/admin/orders";
    }

    @GetMapping("admin/person")
    public String getAllPerson(Model model) {
        model.addAttribute("persons", personService.getAllPerson());
        return "/admin/infoPerson";
    }

    @GetMapping("admin/person/{id}")
    public String getPerson(Model model, @PathVariable("id") int id){
        model.addAttribute("person", personService.getPersonId(id));
        return "/admin/editPerson";
    }

    @PostMapping("admin/person/{id}")
    public String roleAdmin(@ModelAttribute("person") Person person,  @PathVariable("id") int id){
        personService.rolePerson(person);
        return "redirect:/admin/person";
    }
}
