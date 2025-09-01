package com.example.inventorymanagementsystem.service.impl;
import com.example.inventorymanagementsystem.dto.ProductDto;
import com.example.inventorymanagementsystem.dto.UserDto;
import com.example.inventorymanagementsystem.entity.Category;
import com.example.inventorymanagementsystem.entity.Product;
import com.example.inventorymanagementsystem.exception.NotFoundException;
import com.example.inventorymanagementsystem.repository.CategoryRepository;
import com.example.inventorymanagementsystem.repository.ProductRepository;
import com.example.inventorymanagementsystem.response.Response;
import com.example.inventorymanagementsystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-image/";


    @Override
    public Response saveProduct(ProductDto productDto, MultipartFile imageFile) {

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(()-> new NotFoundException("Category Not Found"));
        // map out the product dto to product entity
        Product productToSave = Product.builder()
                .name(productDto.getName())
                .sku(productDto.getSku())
                .price(productDto.getPrice())
                .expiryDate(productDto.getExpiryDate())
                .stockQuantity(productDto.getStockQuantity())
                .description(productDto.getDescription())
                .category(category)
                .build();
        if (imageFile !=null){
            String imagePath = saveImage(imageFile);
            productToSave.setImageUrl(imagePath);
        }

        // save the product to our database
        productRepository.save(productToSave);
        return Response.builder().status(200).message("Product sucessfully saved").build();
    }

    @Override
    public Response updateProduct(ProductDto productDto, MultipartFile imageFile) {
        Product existingProduct = productRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));
        // check if image is associated with the update request
        if (imageFile != null && !imageFile.isEmpty()){
            String imagePath = saveImage(imageFile);
            existingProduct.setImageUrl(imagePath);
        }
        // check if category is to be changed for the product
        if (productDto.getCategoryId() != null && productDto.getCategoryId() > 0){
            Category category =  categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            existingProduct.setCategory(category);
        }

        // check and update fails
        if (productDto.getName() !=null && !productDto.getName().isBlank()){
            existingProduct.setName(productDto.getName());
        }
        if (productDto.getSku() !=null && !productDto.getSku().isBlank()){
            existingProduct.setSku(productDto.getName());
        }
        if (productDto.getDescription() !=null && !productDto.getDescription().isBlank()){
            existingProduct.setDescription(productDto.getName());
        }
        if (productDto.getPrice() !=null && productDto.getPrice().compareTo(BigDecimal.ZERO) >=0){
            existingProduct.setPrice(productDto.getPrice());
        }
        if (productDto.getStockQuantity() !=null && productDto.getStockQuantity() >=0){
            existingProduct.setStockQuantity(productDto.getStockQuantity());
        }
        // update the product
        productRepository.save(existingProduct);
        return Response.builder().status(200).message("Product sucessfully updated").build();

    }

    @Override
    public Response getAllProducts() {
        List<Product> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<ProductDto> productDtos = modelMapper.map(productList,
                new TypeToken<List<UserDto>>() {}.getType());
        return Response.builder().status(200).message("success").products(productDtos).build();
    }

    @Override
    public Response getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        return Response.builder()
                .status(200)
                .message("success")
                .product(modelMapper.map(product,ProductDto.class))
                .build();
    }

    @Override
    public Response deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Product " + id  +" deleted sucessfully")
                .product(modelMapper.map(product,ProductDto.class))
                .build();
    }

    private String saveImage(MultipartFile imageFile){
        // validate image check
        if (!imageFile.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("Only image files are allowed");
        }
        // create the directory to store images if it does not exist
        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()){
            directory.mkdir();
            log.info("Directory was created");
        }
        // generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        // get the absolute path of the image
        String imagePath = IMAGE_DIRECTORY + uniqueFileName;
        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile); // we are transfering(writing to this folder)

        }catch (Exception e){
            throw  new IllegalArgumentException("Error Occured while saving IMAGE"+ e.getMessage());
        }
        return imagePath;
    }
}
