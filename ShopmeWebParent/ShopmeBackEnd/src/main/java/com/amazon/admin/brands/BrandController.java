package com.amazon.admin.brands;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazon.admin.AmazonS3Util;
import com.amazon.admin.FileUploadUtil;
import com.amazon.admin.category.CategoryCsvExporter;
import com.amazon.admin.category.CategoryPageInfo;
import com.amazon.admin.category.CategoryService;
import com.amazon.admin.paging.PagingAndSortingHelper;
import com.amazon.admin.paging.PagingAndSortingParam;
import com.amazon.admin.user.UserNotFoundException;
import com.amazon.common.entity.Brand;
import com.amazon.common.entity.Category;
import com.amazon.common.entity.Role;
import com.amazon.common.entity.User;

@Controller
public class BrandController {
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService service;

	@GetMapping("/brands")
	public String getAll(Model model) {

		return "redirect:/brands/page/1?sortField=name&sortDir=asc";
	}

	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName="listBrands",moduleURL = "/brands") PagingAndSortingHelper helper,
			@PathVariable("pageNum") int pageNum) {

		 brandService.listByPage(pageNum,helper );

		return "brand/brands";

	}

	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		List<Category> listCategories = service.listCategoriesUserInForm();
		Brand brand = new Brand();

		model.addAttribute("brand", brand);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Create New Brand");
		return "brand/brand_form";
	}

	@PostMapping("/brands/save")
	public String saveUser(Brand brand, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if (!multipartFile.isEmpty()) {

			// System.out.println(multipartFile.getOriginalFilename());
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);

			Brand savedBrand = brandService.save(brand);
			//String uploadDir = "../brand-logos/" + savedBrand.getId();
			String uploadDir = "brand-logos/" + savedBrand.getId();
			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
			
		//	FileUploadUtil.cleanDir(uploadDir);
		//	FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if (brand.getLogo().isEmpty())
				brand.setLogo(null);
			brandService.save(brand);
		}

		// System.out.println(user);
		redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully");

		return "redirect:/brands";

	}

	@GetMapping("/brands/edit/{id}")
	public String editUser(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = service.listCategoriesUserInForm();
			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");
			return "brand/brand_form";
		} catch (BrandNotFoundException e) {
			// TODO Auto-generated catch block
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}
	}

	@GetMapping("/brands/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			brandService.delete(id);
			String brandDir="brand-logos/"+id;
			AmazonS3Util.removeFolder(brandDir);
			redirectAttributes.addFlashAttribute("message", "The brands ID " + id + " has been deleted successfully.");

		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/brands";

	}

	@GetMapping("/brands/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<Brand> listBrand = brandService.getAll();
		BrandCsvExporter exporter = new BrandCsvExporter();
		exporter.export(listBrand, response);
	}

}
