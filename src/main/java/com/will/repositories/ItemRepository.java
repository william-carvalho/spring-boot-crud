package com.will.repositories;

import com.will.model.Item;
import org.springframework.data.repository.CrudRepository;


public interface ItemRepository extends CrudRepository<Item, Long>  {
}
