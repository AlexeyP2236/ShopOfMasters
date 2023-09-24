package com.example.shopofmasters.controllers;


import com.example.shopofmasters.models.Cart;
import com.example.shopofmasters.models.Person;
import com.example.shopofmasters.models.Product;
import com.example.shopofmasters.repositories.CartRepository;
import com.example.shopofmasters.repositories.OrderRepository;
import com.example.shopofmasters.security.PersonDetails;
import com.example.shopofmasters.services.OrdersService;
import com.example.shopofmasters.services.PersonService;
import com.example.shopofmasters.services.ProductService;
import com.example.shopofmasters.services.SearchService;
import com.example.shopofmasters.util.PersonValidator;
import com.example.shopofmasters.util.ValidatedDataPerson;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
public class MainController {

    private final PersonValidator personValidator;
    private final PersonService personService;

    private final ProductService productService;
    private final SearchService searchService;
    private final OrdersService ordersService;

    public MainController(PersonValidator personValidator, PersonService personService, ProductService productService, CartRepository cartRepository, OrderRepository orderRepository, SearchService searchService, OrdersService ordersService) {
        this.personValidator = personValidator;
        this.personService = personService;
        this.productService = productService;
        this.searchService = searchService;
        this.ordersService = ordersService;
    }

    @GetMapping("/person_account")
    public String index(Model model){
        // Получаем объект аутентификации -> с помощью SpringContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. Из сессии текущего пользователя получаем объект, который был положен в данную сессию после аутентификации пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String role = personDetails.getPerson().getRole();
        if(role.equals("ROLE_ADMIN")){
            return "redirect:/admin";
        }
        model.addAttribute("products", productService.getAllProduct());
        return "/user/index";
    }


    //    @GetMapping("/registration")
    //    public String registration(Model model){
    //        model.addAttribute("person", new Person());
    //        return "registration";
    //    }
    //Метод регистрации, упрощенный метод которы выше
    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person){
        return "registration";
    }

    //Отправка и проверка данных регистрации с переходом на страницу
    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "registration";
        }
        personService.register(person);
        return "redirect:/person_account";
    }


    @GetMapping("/person_account/product/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("product", productService.getProductId(id));
        return "/user/infoProduct";
    }

    //Принятие параметров для обработки поиска
    @PostMapping("/person_account/product/search")
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
        return "/user/index";

    }

    @GetMapping("/cart/add/{id}")
    public String addProductInCart(@PathVariable("id") int id){
        // Получаем продукт по id
        Product product = productService.getProductId(id);
        // Извлекаем объект аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        // Извлекаем id пользователя из объекта
        int id_person = personDetails.getPerson().getId();
        Cart cart = new Cart(id_person, product.getId());
        ordersService.addCart(cart);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cart(Model model){
        int id_person = personService.personIdSecurityContextHolder();
        // Вычисление итоговой цена
        float price = 0;
        for (Product product: ordersService.getOrderForPersonId(id_person)) {
            price += product.getPrice();
        }
        model.addAttribute("price", price);
        model.addAttribute("cart_product", ordersService.getOrderForPersonId(id_person));
        return "/user/cart";
    }

    @GetMapping("/cart/delete/{id}")
    public String deleteProductFromCart(@PathVariable("id") int id){
        ordersService.deleteOrderForPersonId(id);
        return "redirect:/cart";
    }

    @GetMapping("/order/create")
    public String order(){
        int id_person = personService.personIdSecurityContextHolder();
        ordersService.saveAndDeleteCart(id_person);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderUser(Model model){
        model.addAttribute("orders", ordersService.getOrderId());
        return "/user/orders";
    }

    @GetMapping("/cabinet")
    public String getPerson(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();
        model.addAttribute("person", personService.getPersonId(id_person));
        return "user/privateCabinet";
    }

    @PostMapping("/cabinet/{id}")
    public String roleAdmin(@ModelAttribute("person") @Validated(ValidatedDataPerson.class) Person person, BindingResult bindingResult, @PathVariable("id") int id){
        if (bindingResult.hasErrors()){
            return "user/privateCabinet";
        }
        personService.addInfoPerson(id, person);
        return "redirect:/person_account";
    }
}
