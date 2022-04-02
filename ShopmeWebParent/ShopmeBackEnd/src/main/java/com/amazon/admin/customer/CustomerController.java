package com.amazon.admin.customer;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazon.admin.paging.PagingAndSortingHelper;
import com.amazon.admin.paging.PagingAndSortingParam;
import com.amazon.common.entity.Country;
import com.amazon.common.entity.Customer;

import com.amazon.common.exception.CustomerNotFoundException;


@Controller
public class CustomerController {
	
	@Autowired CustomerService service;
	
	@GetMapping("/customers")
	public String listFirstPage(Model model) {

		//return listByPage(1, model, "firstName", "asc", null);
		return "redirect:/customers/page/1?sortField=firstName&sortDir=asc";

	}

	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(@PagingAndSortingParam(listName="listCustomers",moduleURL = "/customers") PagingAndSortingHelper helper,
			@PathVariable("pageNum") int pageNum) {

		service.listByPage(pageNum, helper);
		

		return "customer/customers";

	}



	@PostMapping("/customers/save")
	public String saveProduct(Customer customer, RedirectAttributes ra,
			Model model) throws IOException {

	
		 service.save(customer);


		ra.addFlashAttribute("message", "The Customer ID " + customer.getId() + "has been updated successfully.");

		return "redirect:/customers";

	}

	

	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes) {

		service.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Customer ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/customers";

	}

	@GetMapping("/customers/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message",
					"The Customer ID  " + id + " has been deleted successfully.");

		} catch (CustomerNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/customers";

	}

	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {

		try {
			Customer customer = service.get(id);
			List<Country> countries = service.listAllCountries();

			model.addAttribute("customer", customer);
			model.addAttribute("listCountries", countries);
			model.addAttribute("pageTitle", String.format("Edit Customer  (ID: %d)",id));

			return "customer/customer_form";
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}

	}

	@GetMapping("/customers/detail/{id}")
	public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {

		try {
			Customer customer = service.get(id);

			model.addAttribute("customer", customer);

			return "customer/customer_detail_modal";
		} catch (CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}

	}
	
	

}
