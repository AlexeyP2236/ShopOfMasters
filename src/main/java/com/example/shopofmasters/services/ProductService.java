package com.example.shopofmasters.services;

import com.example.shopofmasters.models.Category;
import com.example.shopofmasters.models.Image;
import com.example.shopofmasters.models.Product;
import com.example.shopofmasters.repositories.ImageRepository;
import com.example.shopofmasters.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    public ProductService(ProductRepository productRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    //путь для хранения файлов
    @Value("${upload.path}")
    private String uploadPath;
    //Генерация названий файла
    String uuidFile = UUID.randomUUID().toString();

    // Данный метод позволяет получить список всех товаров
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    // Данный метод позволяет получить товар по id
    public Product getProductId(int id){
        //Информация об Optional и об orElse и orElseTrow
        //https://habr.com/ru/articles/346782/
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    // Данный метод позволяет сохранить товар
    @Transactional
    public void saveProduct(Product product, Category category){
        product.setCategory(category);
        productRepository.save(product);
    }

    // Данный метод позволяет обновить данные о товаре
    @Transactional
    public void updateProduct(int id, Product product) {
        product.setId(id);
        productRepository.save(product);

    }
    @Transactional
    public void updatePhotoFile(int id, Product product, MultipartFile photo) throws IOException {
        product.setId(id);
        //Сгенерированное имя + оригинальное имя
        String resultFileName = uuidFile + "." + photo.getOriginalFilename();
        //отправка файла по указаной директории
        photo.transferTo(new File(uploadPath + "/" + resultFileName));
        Image image = new Image();
        image.setProduct(product);
        image.setFileName(resultFileName);
        product.addImageToProduct(image);
        imageRepository.save(image);
    }

    // Данный метод позволяет удалить товар по id
    @Transactional
    public void deleteProduct(int id){
        productRepository.deleteById(id);
        //Почитать про добавление и удаление по другому
        //https://javarush.com/groups/posts/2476-sokhranenie-faylov-v-prilozhenie-i-dannihkh-o-nikh-na-bd
        //https://www.bezkoder.com/spring-boot-delete-file/
    }

    @Transactional
    public MultipartFile addPhotoFile (MultipartFile photo, Product product) throws IOException {
        File uploadDir = new File(uploadPath);
        //Проверка на существование директории
        if(!uploadDir.exists()){
            //Создает директорию
            uploadDir.mkdir();
        }
        //Сгенерированное имя + оригинальное имя
        String resultFileName = uuidFile + "." + photo.getOriginalFilename();
        //отправка файла по указаной директории
        photo.transferTo(new File(uploadPath + "/" + resultFileName));
        //Добавление файла
        Image image = new Image();
        image.setProduct(product);
        image.setFileName(resultFileName);
        product.addImageToProduct(image);
        return photo;
    }
}
