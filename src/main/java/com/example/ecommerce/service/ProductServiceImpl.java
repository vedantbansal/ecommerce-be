package com.example.ecommerce.service;

import com.example.ecommerce.constants.ProductConstants;
import com.example.ecommerce.exceptions.ApiException;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.payload.ProductDTO;
import com.example.ecommerce.payload.ProductResponse;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    @Value("${product.dir}")
    private String productDirPath;

    public ProductServiceImpl(CategoryRepository categoryRepository,
                              ModelMapper modelMapper,
                              ProductRepository productRepository,
                              FileService fileService, CartItemRepository cartItemRepository, CartRepository cartRepository){
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public ResponseEntity<ProductDTO> addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));
        int productCountFromDB = productRepository.countProductByNameAndCategory(productDTO.getProductName(), category.getCategoryName());
        if(productCountFromDB > 0){
            throw new ApiException("Product: " + productDTO.getProductName() + " in Category: " + category.getCategoryName() + " already exists.");
        }
        Product product = modelMapper.map(productDTO, Product.class);
        product.setImage(ProductConstants.DEFAULT_PRODUCT_IMAGE_NAME);
        product.setCategory(category);
        double specialPrice = (1-product.getDiscount()*0.01)*product.getPrice();
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return new ResponseEntity<>(modelMapper.map(savedProduct, ProductDTO.class), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pagedetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pagedetails);
        List<Product> productList = productPage.getContent();
        if(productList.isEmpty()) throw new ApiException("No products present.");
        List<ProductDTO> productDTOList = productList.stream().map(
                product -> modelMapper.map(product, ProductDTO.class)
        ).toList();
        ProductResponse productResponse = modelMapper.map(productPage, ProductResponse.class);
        productResponse.setContent(productDTOList);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductResponse> getProductsByCategory(Integer pageNumber, Integer pageSize, Long categoryId) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Page<Product> productPage = productRepository.findByCategory(category, pageDetails);
        List<Product> productList = productPage.getContent();
        if(productList.isEmpty()) throw new ApiException("No products found in category " + category.getCategoryName());
        List<ProductDTO> productDTOList = productList.stream().map(
                product -> modelMapper.map(product, ProductDTO.class)
        ).toList();
        ProductResponse productResponse = modelMapper.map(productPage, ProductResponse.class);
        productResponse.setContent(productDTOList);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductResponse> getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> productPage = productRepository.searchProductByKeyword(keyword, pageable);
        List<Product> productList = productPage.getContent();
        if(productList.isEmpty()) throw new ApiException("No products found with keyword " + keyword);
        List<ProductDTO> productDTOList = productList.stream().map(
                product -> modelMapper.map(product, ProductDTO.class)
        ).toList();
        ProductResponse productResponse = modelMapper.map(productPage, ProductResponse.class);
        productResponse.setContent(productDTOList);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<ProductDTO> updateProduct(ProductDTO productDTO, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->new ResourceNotFoundException("Product","productId",productId));
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());
        double specialPrice = (1-product.getDiscount()*0.01)*product.getPrice();
        double oldPrice = product.getSpecialPrice();
        product.setSpecialPrice(specialPrice);
        updateProductPricesInCarts(productId, specialPrice, oldPrice);
        Product savedProduct = productRepository.save(product);
        return new ResponseEntity<>(modelMapper.map(savedProduct, ProductDTO.class), HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<ProductDTO> deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->new ResourceNotFoundException("Product","productId",productId));
        updateProductPricesInCarts(productId, 0, product.getSpecialPrice());
        product.getCartItems().clear();
        productRepository.delete(product);
        return new ResponseEntity<>(modelMapper.map(product, ProductDTO.class), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductDTO> updateProductImage(Long productId, MultipartFile image){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        String imagePath =  fileService.uploadImage(productDirPath, image);
        product.setImage(imagePath);
        Product savedProduct = productRepository.save(product);
        return new ResponseEntity<>(modelMapper.map(savedProduct, ProductDTO.class), HttpStatus.OK);
    }

    @Transactional
    private void updateProductPricesInCarts(Long productId, double newPrice, double oldPrice){
        List<CartItem> cartItems = cartItemRepository.findByProductId(productId);
        cartItems.forEach(item -> {
            Cart cart = item.getCart();
            double newCartPrice = cart.getTotalPrice() - item.getQuantity() * (oldPrice - newPrice);
            cart.setTotalPrice(newCartPrice);
            cartRepository.save(cart);
        });
    }


}
