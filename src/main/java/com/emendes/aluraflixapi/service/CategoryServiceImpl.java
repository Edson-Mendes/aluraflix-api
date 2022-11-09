package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.exception.CategoryNotFoundException;
import com.emendes.aluraflixapi.exception.OperationNotAllowedException;
import com.emendes.aluraflixapi.mapper.CategoryMapper;
import com.emendes.aluraflixapi.mapper.VideoMapper;
import com.emendes.aluraflixapi.model.entity.Category;
import com.emendes.aluraflixapi.repository.CategoryRepository;
import com.emendes.aluraflixapi.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final VideoRepository videoRepository;
  private final CategoryMapper categoryMapper;
  private final VideoMapper videoMapper;

  @Override
  public Page<CategoryResponse> findAll(Pageable pageable) {
    return categoryRepository.findByEnabled(pageable, true)
        .map(categoryMapper::toCategoryResponse);
  }

  @Override
  public CategoryResponse findById(int id) {
    return categoryMapper.toCategoryResponse(findCategoryById(id));
  }

  @Override
  public Page<VideoResponse> findVideosByCategoryId(int id, Pageable pageable) {
    Category category = findCategoryById(id);
    return videoRepository.findByCategory(category, pageable)
        .map(videoMapper::toVideoResponse);
  }

  @Override
  public CategoryResponse create(CategoryRequest categoryRequest) {
    Category categoryToBeSaved = categoryMapper.fromCategoryRequest(categoryRequest);

    categoryToBeSaved.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    categoryToBeSaved.setEnabled(true);

    return categoryMapper.toCategoryResponse(categoryRepository.save(categoryToBeSaved));
  }

  @Override
  public CategoryResponse update(int id, CategoryRequest categoryRequest) {
    verifyIfIsCategoryLivre(id);
    Category categoryToBeUpdated = findCategoryById(id);

    categoryToBeUpdated = categoryMapper.merge(categoryRequest, categoryToBeUpdated);

    return categoryMapper.toCategoryResponse(categoryRepository.save(categoryToBeUpdated));
  }

  @Override
  public void delete(int id) {
    verifyIfIsCategoryLivre(id);
    Category category = findCategoryById(id);
    verifyIfCategoryHasVideos(category);

    category.setEnabled(false);
    category.setDeletedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

    categoryRepository.save(category);
  }

  @Override
  public boolean existsEnabledCategoryWithId(Integer id) {
    return categoryRepository.existsByIdAndEnabled(id, true);
  }

  private Category findCategoryById(int id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new CategoryNotFoundException("Category not found for id: " + id));
  }

  /**
   * Verifica se o {@code categoryId} informado pertence a categoria Livre.<br/>
   * Usado para impedir alterações na Categoria Livre!
   * @param categoryId id da Category a ser verificada.
   * @throws OperationNotAllowedException se {@code categoryId} pertence a Categoria Livre.
   */
  private void verifyIfIsCategoryLivre(int categoryId) {
    if(categoryId == 1) {
      throw new OperationNotAllowedException("Change/delete 'Livre' category not allowed");
    }
  }

  /**
   * Verifica se a {@code categoria} possui vídeos associados.<br/>
   * Categoria com vídeos associados não deve ser deletada!
   * @param category categoria a ser verificada.
   * @throws OperationNotAllowedException se {@code category} se categoria possui vídeos associados.
   */
  private void verifyIfCategoryHasVideos(Category category) {
    if (videoRepository.existsByCategory(category)){
      throw new OperationNotAllowedException("This category has associated videos");
    }
  }

}
