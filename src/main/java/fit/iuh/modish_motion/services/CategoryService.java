package fit.iuh.modish_motion.services;

import fit.iuh.modish_motion.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDTO> findAll();
    Optional<CategoryDTO> findById(Integer id);
    CategoryDTO save(CategoryDTO category);
    void deleteById(Integer id);
    Page<CategoryDTO> findByPage(Pageable pageable);
    List<CategoryDTO> findRandomCategories(int count);
    CategoryDTO findByName(String name);
}