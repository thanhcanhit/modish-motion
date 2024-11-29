package fit.iuh.modish_motion.repositories;
import fit.iuh.modish_motion.entities.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    // Additional query methods can be defined here
    @Query(value = "SELECT * FROM items i WHERE i.category_id = :categoryId AND EXISTS (SELECT 1 FROM variants v WHERE v.item_id = i.id) ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Item> findRandomItemsByCategory(@Param("categoryId") int categoryId, @Param("count") int count);
    @Query(value = "SELECT * FROM items i WHERE  EXISTS (SELECT 1 FROM variants v WHERE v.item_id = i.id) ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Item> findRandomItems( @Param("count") int count);

    @Query("SELECT i FROM Item i WHERE size(i.variants) > 0")
    Page<Item> findItemsWithVariants(Pageable pageable);

    List<Item> findByCategoryId(int categoryId);

    @Query(value = "SELECT * FROM items i WHERE i.category_id = :categoryId AND i.id != :itemId AND EXISTS (SELECT 1 FROM variants v WHERE v.item_id = i.id) ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Item> findRelatedItems(@Param("categoryId") int categoryId, @Param("itemId") String itemId, @Param("limit") int limit);
    Page<Item> findByCategoryId(int categoryId, Pageable pageable);
    List<Item> findByNameContaining(String name);

    @Query("SELECT i FROM Item i WHERE i.name LIKE %:name%")
    List<Item> findByNameContaining2(String name);
    long countByCategoryId(int categoryId);
    @Query("SELECT i FROM Item i JOIN i.variants v WHERE i.category.id = :categoryId " +
            "AND (:colors IS NULL OR v.color.color IN :colors) " +
            "AND (:sizes IS NULL OR v.size.size IN :sizes)")
    Page<Item> findByCategoryIdAndFilter(@Param("categoryId") int categoryId,
                                         @Param("colors") List<String> colors,
                                         @Param("sizes") List<String> sizes,
                                         Pageable pageable);

    @Query("SELECT COUNT(DISTINCT i) FROM Item i JOIN i.variants v WHERE i.category.id = :categoryId " +
            "AND (:colors IS NULL OR v.color.color IN :colors) " +
            "AND (:sizes IS NULL OR v.size.size IN :sizes)")
    long countByCategoryIdAndFilter(@Param("categoryId") int categoryId,
                                    @Param("colors") List<String> colors,
                                    @Param("sizes") List<String> sizes);



    @Query("""
        SELECT DISTINCT i FROM Item i
        LEFT JOIN FETCH i.variants v
        LEFT JOIN FETCH v.color c
        LEFT JOIN FETCH v.size s
        WHERE i.category.id = :categoryId
        AND (:colors IS NULL OR c.id IN :colors)
        AND (:sizes IS NULL OR s.id IN :sizes)
        AND (
            CASE 
                WHEN i.promotionPrice > 0 THEN i.promotionPrice
                ELSE (SELECT MIN(v2.price) FROM Variant v2 WHERE v2.item = i)
            END
        ) BETWEEN :minPrice AND :maxPrice
        ORDER BY i.quantitySold DESC
    """)
    Page<Item> findByCategoryIdAndFilter(
            @Param("categoryId") int categoryId,
            @Param("colors") List<Integer> colors,
            @Param("sizes") List<Integer> sizes,
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice,
            Pageable pageable
    );

    @Query("""
        SELECT COUNT(DISTINCT i) FROM Item i
        LEFT JOIN i.variants v
        LEFT JOIN v.color c
        LEFT JOIN v.size s
        WHERE i.category.id = :categoryId
        AND (:colors IS NULL OR c.id IN :colors)
        AND (:sizes IS NULL OR s.id IN :sizes)
        AND (
            CASE 
                WHEN i.promotionPrice > 0 THEN i.promotionPrice
                ELSE (SELECT MIN(v2.price) FROM Variant v2 WHERE v2.item = i)
            END
        ) BETWEEN :minPrice AND :maxPrice
    """)
    long countByFilters(
            @Param("categoryId") int categoryId,
            @Param("colors") List<Integer> colors,
            @Param("sizes") List<Integer> sizes,
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice
    );

    @Query("""
        SELECT DISTINCT i FROM Item i
        LEFT JOIN FETCH i.variants v
        LEFT JOIN FETCH v.color c
        LEFT JOIN FETCH v.size s
        WHERE i.name LIKE %:name%
        AND (:colors IS NULL OR c.id IN :colors)
        AND (:sizes IS NULL OR s.id IN :sizes)
        AND (
            CASE 
                WHEN i.promotionPrice > 0 THEN i.promotionPrice
                ELSE (SELECT MIN(v2.price) FROM Variant v2 WHERE v2.item = i)
            END
        ) BETWEEN :minPrice AND :maxPrice
        ORDER BY i.quantitySold DESC
    """)
    Page<Item> searchByNameAndFilter(
            @Param("name") String name,
            @Param("colors") List<Integer> colors,
            @Param("sizes") List<Integer> sizes,
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice,
            Pageable pageable
    );

    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Item> searchByName(@Param("name") String name, Pageable pageable);
}