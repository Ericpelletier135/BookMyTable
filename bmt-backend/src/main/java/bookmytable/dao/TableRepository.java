package bookmytable.dao;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import bookmytable.model.*;

public interface TableRepository extends CrudRepository<RestaurantTable, Long> {
  
  RestaurantTable findTableById(long id);
  
  List<RestaurantTable> findTablesByRestaurant(Restaurant restaurant);
  RestaurantTable findTableByRestaurantAndXAndY(Restaurant restaurant, int x, int y);
  RestaurantTable findTableByRestaurantAndTableNumber(Restaurant restaurant, int tableNumber);
  List<RestaurantTable> findTablesByRestaurantAndCapacity(Restaurant restaurant, int capacity);
}
