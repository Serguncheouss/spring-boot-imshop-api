package my.app.imshop.controller;

import my.app.imshop.exception.NotFoundException;
import my.app.imshop.exception.OutOfStockException;
import my.app.imshop.model.dto.StoreDto;
import my.app.imshop.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/store")
public class StoreController {
    @Autowired
    StoreService storeService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping({"", "/"})
    public ResponseEntity<?> putOrShip(
            @RequestBody StoreDto storeDto
    ) {
        if (storeDto.getCount() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

        try {
            return new ResponseEntity<>(storeService.putOrShip(storeDto.getId(), storeDto.getCount()), HttpStatus.OK);
        } catch (OutOfStockException e) {
            return new ResponseEntity<>("Out of stock", HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
