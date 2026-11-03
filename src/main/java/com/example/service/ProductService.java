package com.example.service;

import com.example.domain.Category;
import com.example.domain.ImageProduct;
import com.example.domain.Product;
import com.example.dto.ProductDto;
import com.example.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final String uploadDirProperty;
    private Path rootLocation;

    // 1. Внедряем значение из application.properties через конструктор
    public ProductService(ProductRepository productRepository, @Value("${app.upload.dir}") String uploadDirProperty) {
        this.productRepository = productRepository;
        this.uploadDirProperty = uploadDirProperty;
    }

    // 2. Инициализируем относительный путь при старте приложения
    @PostConstruct
    public void init() {
        try {
            this.rootLocation = Paths.get(uploadDirProperty);
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию для загрузки файлов", e);
        }
    }


    public List<ProductDto> getProducts(String category) {
        if (category == null || category.isBlank())
            return productRepository.findAll()
                    .stream()
                    .map(ProductDto::new)
                    .toList();

        try {
            Category searCategory = Category.valueOf(category.trim().toUpperCase());
            return productRepository.findByCategory(searCategory)
                    .stream()
                    .map(ProductDto::new)
                    .toList();
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }


    @Transactional
    public void saveProductsWithImages(ProductDto dto, List<MultipartFile> files) throws IOException {
        String cleanName = dto.getName().trim().replaceAll("\\s+", " ");

        if (productRepository.findByName(cleanName) != null) {
            throw new IllegalArgumentException("Товар с таким " + dto.getName() + " существует!");
        }

        Category categoryName = Category.valueOf(dto.getCategory().name().trim().toUpperCase());

        Product product = new Product();
        product.setCategory(categoryName);
        product.setDescription(dto.getDescription());
        product.setName(cleanName);
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setStorageLocation(dto.getStorageLocation());


        for (int i = 0; i < files.size(); i++) {
            MultipartFile file1 = files.get(i);

            String originalFileName = file1.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

            Path pathFile = this.rootLocation.resolve(uniqueFileName).normalize().toAbsolutePath();
            Files.copy(file1.getInputStream(), pathFile);

            ImageProduct imageProduct = new ImageProduct();
            imageProduct.setImageUrl(uniqueFileName);

            if (i == 0) {
                imageProduct.setMain(true);
            }

            product.addImage(imageProduct);
        }
        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(ProductDto productDto, Long id){
        Product databaseProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар под id " + id + " не найден!"));

        if (!Objects.equals(databaseProduct.getName().trim().replaceAll("\\s+", " "), productDto.getName().trim().replaceAll("\\s+", " "))) {
            Product productWithSameName = productRepository.findByName(productDto.getName().trim().replaceAll("\\s+", " "));
            if (productWithSameName != null) {
                throw new IllegalArgumentException("Название обновляемого товара " + databaseProduct.getName() + " занято под другим товаром!");
            }
//            databaseProduct.setName(product.getName().trim().replaceAll("\\s+", " "));
        }

        databaseProduct.setDescription(productDto.getDescription());
        databaseProduct.setPrice(productDto.getPrice());
        databaseProduct.setName(productDto.getName().trim().replaceAll("\\s+", " "));
        databaseProduct.setCategory(productDto.getCategory());
        databaseProduct.setStockQuantity(productDto.getStockQuantity());
    }

    @Transactional
    public void deleteProduct(Long id) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар под id " + id + " не найден!"));

        for (ImageProduct file : product.getImageProducts()) {
            Path path = this.rootLocation.resolve(file.getImageUrl()).normalize().toAbsolutePath();
            Files.deleteIfExists(path);
        }

//        for (int i = 0; i < product.getImageProducts().size(); i ++) {
//            ImageProduct file = product.getImageProducts().get(i);
//            String url = file.getImageUrl();
//
//            Path path =  Paths.get(UPLOAD_DIR, file.getImageUrl());
//            Files.deleteIfExists(path);
//        }

//        for (ImageProduct imageProduct : product.getImageProducts()) {
//            Path path = Paths.get(UPLOAD_DIR, imageProduct.getImageUrl());
//            Files.deleteIfExists(path);
//        }

        productRepository.delete(product);
    }
}


//package com.example.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import jakarta.annotation.PostConstruct;
//import java.io.IOException;
//import java.nio.file.*;
//        import java.util.UUID;
//
//@Service
//public class ProductService {
//
//    // Spring сам вставит сюда значение "uploads" из application.properties
//    @Value("${app.upload.dir}")
//    private String uploadDirProperty;
//
//    private Path rootLocation;
//
//    // Метод выполнится автоматически СРАЗУ после старта приложения
//    @PostConstruct
//    public void init() {
//        try {
//            // Создаем объект Path. Он автоматически поймет, что папка находится в корне проекта
//            this.rootLocation = Paths.get(uploadDirProperty);
//
//            // Если папки "uploads" еще нет, Spring сам создаст ее при старте
//            if (!Files.exists(rootLocation)) {
//                Files.createDirectories(rootLocation);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Не удалось инициализировать папку для загрузки файлов", e);
//        }
//    }
//
//    // Ваш метод сохранения файла теперь будет выглядеть очень чисто:
//    public String saveImage(MultipartFile file) {
//        if (file.isEmpty()) {
//            throw new IllegalArgumentException("Файл пустой");
//        }
//
//        try {
//            // Генерируем уникальное имя, чтобы файлы не перезаписывали друг друга
//            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//
//            // Безопасно разрешаем путь внутри нашей целевой папки
//            Path destinationFile = this.rootLocation.resolve(Paths.get(filename))
//                    .normalize().toAbsolutePath();
//
//            // Проверка безопасности: не пытается ли файл выйти за пределы нашей папки (Path Traversal атака)
//            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
//                throw new IllegalArgumentException("Попытка загрузить файл за пределы разрешенной директории");
//            }
//
//            // Копируем файл в файловую систему
//            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
//
//            // Возвращаем только имя файла (или относительный путь), который запишем в БД
//            return filename;
//
//        } catch (IOException e) {
//            throw new RuntimeException("Ошибка при сохранении файла", e);
//        }
//    }
//
//    // Ваш метод удаления файла тоже использует rootLocation:
//    public void deleteImageFile(String filename) {
//        try {
//            Path file = this.rootLocation.resolve(filename);
//            Files.deleteIfExists(file);
//        } catch (IOException e) {
//            throw new RuntimeException("Не удалось удалить файл: " + filename, e);
//        }
//    }
//}

