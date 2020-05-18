package org.chonochonov.springintroex.services.impl;

import org.chonochonov.springintroex.constants.GlobalConstants;
import org.chonochonov.springintroex.entities.Category;
import org.chonochonov.springintroex.repositories.CategoryRepository;
import org.chonochonov.springintroex.services.CategoryService;
import org.chonochonov.springintroex.utilis.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileUtil fileUtil;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, FileUtil fileUtil) {
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedCategories() throws IOException {
        if (this.categoryRepository.count() != 0){
            return;
        }
        String[] fileContent = this.fileUtil.readFileContent(GlobalConstants.CATEGORIES_FILE_PATH);

        Arrays.stream(fileContent)
                .forEach(f -> {
                    Category category = new Category(f);
                    this.categoryRepository.saveAndFlush(category);
                });
    }

    @Override
    public Category getCategoryById(long id) {
        return this.categoryRepository.getOne(id);
    }
}
