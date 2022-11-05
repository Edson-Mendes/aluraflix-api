package com.emendes.aluraflixapi.service;

import com.emendes.aluraflixapi.dto.request.CategoryRequest;
import com.emendes.aluraflixapi.dto.response.CategoryResponse;
import com.emendes.aluraflixapi.dto.response.VideoResponse;
import com.emendes.aluraflixapi.exception.CategoryNotFoundException;
import com.emendes.aluraflixapi.exception.OperationNotAllowedException;
import com.emendes.aluraflixapi.model.entity.Category;
import com.emendes.aluraflixapi.repository.CategoryRepository;
import com.emendes.aluraflixapi.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final VideoRepository videoRepository;
  private final ModelMapper mapper;

//  TODO: Não devolver para o cliente categorias desativadas!
  public Page<CategoryResponse> findAll(Pageable pageable) {
    return categoryRepository.findAll(pageable)
        .map(c -> mapper.map(c, CategoryResponse.class));
  }

  public CategoryResponse findById(int id) {
    return mapper.map(findCategoryById(id), CategoryResponse.class);
  }

  public Page<VideoResponse> findVideosByCategoryId(int id, Pageable pageable) {
    Category category = findCategoryById(id);
    return videoRepository.findByCategory(category, pageable)
        .map(v -> mapper.map(v, VideoResponse.class));
  }

  public CategoryResponse create(CategoryRequest categoryRequest) {
    Category categoryToBeSaved = mapper.map(categoryRequest, Category.class);

    categoryToBeSaved.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    categoryToBeSaved.setEnabled(true);

    return mapper.map(categoryRepository.save(categoryToBeSaved), CategoryResponse.class);
  }

  public CategoryResponse update(int id, CategoryRequest categoryRequest) {
    verifyIfIsCategoryLivre(id);
    Category categoryToBeUpdated = findCategoryById(id);

    categoryToBeUpdated.setTitle(categoryRequest.getTitle());
    categoryToBeUpdated.setColor(categoryRequest.getColor());

    return mapper.map(categoryRepository.save(categoryToBeUpdated), CategoryResponse.class);
  }

//  TODO: Pensar/Pesquisar o que fazer com os vídeos associados a categoria deletada.
//  Talvez não permitir que o cliente delete uma categoria com vídeos associados.
  public void delete(int id) {
    verifyIfIsCategoryLivre(id);
    Category category = findCategoryById(id);

    category.setEnabled(false);
    category.setDeletedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

    categoryRepository.save(category);
  }

  public boolean existsEnabledCategoryWithId(Integer id) {
    return categoryRepository.existsByIdAndEnabled(id, true);
  }

  private Category findCategoryById(int id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new CategoryNotFoundException("Category not found for id: " + id));
  }

  private void verifyIfIsCategoryLivre(int id) {
    if(id == 1) {
      throw new OperationNotAllowedException("Change/delete 'Livre' category not allowed");
    }
  }

}
