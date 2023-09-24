package com.example.shopofmasters.services;

import com.example.shopofmasters.models.Category;
import com.example.shopofmasters.models.Image;
import com.example.shopofmasters.models.Product;
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

    //путь для хранения файлов
    @Value("${upload.path}")
    private String uploadPath;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Данный метод позволяет получить список всех товаров
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    // Данный метод позволяет получить товар по id
    public Product getProductId(int id){
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
    public void updateProduct(int id, Product product){
        product.setId(id);
        productRepository.save(product);
    }

    // Данный метод позволяет удалить товар по id
    @Transactional
    public void deleteProduct(int id){
        productRepository.deleteById(id);
    }

    public MultipartFile addPhotoFile (MultipartFile photo, Product product) throws IOException {
        File uploadDir = new File(uploadPath);
        //Проверка на существование директории
        if(!uploadDir.exists()){
            //Создает директорию
            uploadDir.mkdir();
        }
        //Генерация названий файла
        String uuidFile = UUID.randomUUID().toString();
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
