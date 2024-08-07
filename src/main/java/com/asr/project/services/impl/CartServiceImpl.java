package com.asr.project.services.impl;

import com.asr.project.dtos.CartDto;
import com.asr.project.entities.Cart;
import com.asr.project.entities.CartItem;
import com.asr.project.entities.Product;
import com.asr.project.entities.User;
import com.asr.project.exceptions.BadApiResponseException;
import com.asr.project.exceptions.ResourceNotFoundException;
import com.asr.project.models.AddItemCartRequest;
import com.asr.project.repositories.CartItemRepository;
import com.asr.project.repositories.CartRepository;
import com.asr.project.repositories.ProductRepository;
import com.asr.project.repositories.UserRepository;
import com.asr.project.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto getCartByUser(String userId) {

        User user = userRepository.findById(userId).
                orElseThrow(()->new ResourceNotFoundException("User ID not found !!"));
        Cart cart = cartRepository.findByUser(user).
                orElseThrow(()->new ResourceNotFoundException("Cart of given user not found !!"));
        return entityToDto(cart);
    }

    @Override
    public CartDto addItemToCart(String userId, AddItemCartRequest addItemCartRequest) {

        String productId = addItemCartRequest.getProductId();
        int quantity = addItemCartRequest.getQuantity();

        if(quantity<=0) {
            throw new BadApiResponseException("Request quantity is not valid !!");
        }

        Product product = productRepository.findById(productId).
                orElseThrow(()-> new ResourceNotFoundException("Product ID not found !!"));
        User user = userRepository.findById(userId).
                orElseThrow(()->new ResourceNotFoundException("User ID not found !!"));

        Cart cart;
        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException ex) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        cart.setUser(user);

        AtomicBoolean isItemUpdated = new AtomicBoolean(false);
        List<CartItem> cartItems = cart.getCartItems();
        List<CartItem> updatedCartItems = cartItems.stream().map(item->{
            if(item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                isItemUpdated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

//        Replace elements in the existing collection (if orphanRemoval = true)
        cart.getCartItems().clear();
        cart.getCartItems().addAll(updatedCartItems);

        if(!isItemUpdated.get()) {
            CartItem cartItem = CartItem.builder()
                    .cartItemId(UUID.randomUUID().toString())
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .product(product)
                    .cart(cart).build();
            cart.getCartItems().add(cartItem);
        }

        Cart cart1 = cartRepository.save(cart);
        return entityToDto(cart1);
    }

    @Override
    public void removeItemFromCart(String userId, String itemId) {

        User user = userRepository.findById(userId).
                orElseThrow(()->new ResourceNotFoundException("User ID not found !!"));
        Cart cart = cartRepository.findByUser(user).
                orElseThrow(()->new ResourceNotFoundException("Cart of given user not found !!"));

        List<CartItem> filteredCartItems = cart.getCartItems().stream().filter(item-> !item.getCartItemId().
                    equals(itemId)).toList();

        if(filteredCartItems.size() == cart.getCartItems().size()) {
            throw new ResourceNotFoundException("Given item not found in user cart !!");
        }

        cart.getCartItems().clear();
        cart.getCartItems().addAll(filteredCartItems);
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(String userId) {

        User user = userRepository.findById(userId).
                orElseThrow(()->new ResourceNotFoundException("User ID not found !!"));
        Cart cart = cartRepository.findByUser(user).
                orElseThrow(()->new ResourceNotFoundException("Cart of given user not found !!"));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    private CartDto entityToDto(Cart cart) {

        return modelMapper.map(cart, CartDto.class);
    }

    private Cart dtoToEntity(CartDto cartDto) {

        return modelMapper.map(cartDto, Cart.class);
    }
}
