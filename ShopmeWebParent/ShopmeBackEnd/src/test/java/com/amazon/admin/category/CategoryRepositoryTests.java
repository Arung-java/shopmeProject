package com.amazon.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.amazon.common.entity.Category;
import com.amazon.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {

	@Autowired
	private CategoryRepository repository;

	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics");
		Category savedCategory = repository.save(category);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	@Test
	public void getAll() {
	List<Category> all=	(List<Category>) repository.findAll();
	System.out.println("All Category : " + all);
	}
	
	@Test
	public void testUpdateCategoryDetalis() {
		Category category = repository.findById(2).get();
		category.setEnabled(true);
		// user.setEmail("Ravikumar@Cts.com");
		repository.save(category);
		System.out.println(category);
		assertThat(category.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(7);
		Category subCategory = new Category("Iphone", parent);
		Category savedCategory = repository.save(subCategory);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Test
	public void testGetCategory() {
		Category category = repository.findById(2).get();
		System.out.println(category.getName());
		Set<Category> children = category.getChildren();

		for (Category subCategory : children) {

			System.out.println(subCategory.getName());
		}
		assertThat(children.size()).isGreaterThan(0);
	}

	@Test
	public void testPrintHierarchicalCategories() {
		Iterable<Category> categories = repository.findAll();

		for (Category category : categories) {
			if (category.getParent() == null) {
				System.out.println(category.getName());

				Set<Category> children = category.getChildren();

				for (Category subCategory : children) {
					System.out.println("--" + subCategory.getName());
					printChildren(subCategory, 1);
				}
			}

		}
	}

	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();

		for (Category subCategory : children) {
			for (int i = 0; i < newSubLevel; i++) {
				System.out.print("--");
			}

			System.out.println(subCategory.getName());
			printChildren(subCategory, newSubLevel);
		}

	}
	
	@Test
	public void testListRootCategories() {
	List<Category> rootCategories=	repository.findRootCategories(Sort.by("name").ascending());
	rootCategories.forEach(cat->System.out.println(cat.getName()));
		
	}
	
	@Test
	public void findByName() {
		String name="Computer";
		Category category=repository.findByName(name);
		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
	}
	
	@Test
	public void findByAlias() {
		String name="books1";
		Category category=repository.findByAlias(name);
		assertThat(category).isNotNull();
		assertThat(category.getAlias()).isEqualTo(name);
	}
}
