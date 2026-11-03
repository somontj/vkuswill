package com.example.controller;

import com.example.domain.Category;
import com.example.dto.ProductDto;
import com.example.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getProducts(@RequestParam(name = "category", required = false) String category) {

        return productService.getProducts(category);
    }


    @PostMapping("/addProduct")
    public ResponseEntity<String> addProduct(
            @Valid @ModelAttribute ProductDto dto,
            @RequestParam("files") List<MultipartFile> files
    ) throws IOException {
        try {
            productService.saveProductsWithImages(dto, files);
            return ResponseEntity.ok("Товар успешно добавлен с фотографиями");
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        } catch (Exception exception) {
            // 1. Обязательно выводим в консоль, чтобы увидеть стектрейс!
            exception.printStackTrace();

            // 2. Временно отдаем реальный текст ошибки в Postman для диагностики
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка: " + exception.getMessage());
        }
    }



    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        try {
            productService.updateProduct(productDto, id);
            return ResponseEntity.ok("Товар успешно обновлен!");
        }
        catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка со стороны сервера!");
        }
    }


    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Товар успешно удален!");
        }
        catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка на стороне сервера!");
        }
    }



}

