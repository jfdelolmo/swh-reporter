package org.jfo.swaggerhub.swhreporter.service;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@DataJpaTest

public class CartTest {
//
//    @Autowired
//    CartRepository cartRepository;
//    @Autowired
//    ItemsRepository itemsRepository;
//
//    @Test
//    void test() {
//        Cart cart = new Cart();
//        Items item1 = new Items();
//        item1.setCart(cart);
//        item1.setName("Name A");
//        Items item2 = new Items();
//        item2.setCart(cart);
//        item2.setName("Name B");
//
//        cart.setItems(new ArrayList<>());
//        cart.getItems().add(item1);
//        cart.getItems().add(item2);
//
//        cart = cartRepository.save(cart);
//        itemsRepository.save(item1);
//        itemsRepository.save(item2);
//
//        Assertions.assertThat(cart).isNotNull();
//
//        cart = cartRepository.findById(cart.getId()).orElse(null);
//
//        Items item3 = new Items();
//        item3.setCart(cart);
//        item3.setName("Name C");
//        cart.getItems().add(item3);
//
////        cartRepository.save(cart);
//        itemsRepository.save(item3);
//        Assertions.assertThat(cart).isNotNull();
//
//        Iterable<Cart> finded = cartRepository.findAll();
//        List<Cart> listOfCarts = new ArrayList<>();
//        finded.iterator().forEachRemaining(listOfCarts::add);
//        Assertions.assertThat(listOfCarts.get(0).getItems().size()).isEqualTo(3);
//        Assertions.assertThat(listOfCarts.get(0).getItems().stream().filter(f->null==f.getId()).count()).isEqualTo(0);
//    }
}
