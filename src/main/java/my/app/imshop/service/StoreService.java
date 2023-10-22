package my.app.imshop.service;

import my.app.imshop.exception.NotFoundException;
import my.app.imshop.exception.OutOfStockException;
import my.app.imshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
    @Autowired
    private ProductService productService;

    public int putOrShip(long id, int count) throws NotFoundException, OutOfStockException {
        Product product = productService.get(id);
        if (product != null) {
            if (count < 0 && Math.abs(count) > product.getCount()) {
                throw new OutOfStockException();
            }

            int newCount = product.getCount() + count;
            product.setCount(newCount);
            productService.update(product.getId(), product);

            return newCount;
        }

        throw new NotFoundException();
    }
}
