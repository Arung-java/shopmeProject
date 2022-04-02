package com.amazon.admin.shippingrate;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazon.admin.customer.CustomerService;
import com.amazon.admin.paging.PagingAndSortingHelper;
import com.amazon.admin.paging.PagingAndSortingParam;
import com.amazon.common.entity.Country;
import com.amazon.common.entity.ShippingRate;

@Controller
public class ShippingRateController {
	private String defaultRedirectURL = "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc";

	@Autowired
	private ShippingRateService rateService;

	@GetMapping("/shipping_rates")
	public String listFirstPage(Model model) {

		// return listByPage(1, model, "firstName", "asc", null);
		return defaultRedirectURL;

	}

	@GetMapping("/shipping_rates/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "shippingRates", moduleURL = "/shipping_rates") PagingAndSortingHelper helper,
			@PathVariable("pageNum") int pageNum) {

		rateService.listByPage(pageNum, helper);

		return "shipping_rates/shipping_rates";

	}

	@GetMapping("/shipping_rates/new")
	public String newRate(Model model) {
		List<Country> listCountries = rateService.listAllCountries();
		model.addAttribute("rate", new ShippingRate());
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "New Rate");
		return "shipping_rates/shipping_rate_form";

	}

	@PostMapping("/shipping_rates/save")
	public String saveRate(ShippingRate rate, RedirectAttributes ra, Model model) throws IOException {
		try {
			rateService.save(rate);
			ra.addFlashAttribute("message", "The ShippingRate  rate " + rate.getId() + "has been saved successfully.");
		} catch (ShippingRateAlreadyExistsException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return defaultRedirectURL;
	}

	@GetMapping("/shipping_rates/edit/{id}")
	public String editRate(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {

		try {
			ShippingRate rate = rateService.get(id);
			List<Country> listCountries = rateService.listAllCountries();

			model.addAttribute("rate", rate);
			model.addAttribute("listCountries", listCountries);
			model.addAttribute("pageTitle", "Edit Rate (ID: " + id + ")");

			return "shipping_rates/shipping_rate_form";
		} catch (ShippingRateNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return defaultRedirectURL;
		}
	}

	@GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
	public String updateCODSupport(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "supported") boolean supported, RedirectAttributes ra) {

		try {
			rateService.updateCODSupport(id, supported);
			ra.addFlashAttribute("message",
					"COD support for  ShippingRate  rate  ID " + id + "has been updated successfully.");
		} catch (ShippingRateNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}

		return defaultRedirectURL;

	}

	@GetMapping("/shipping_rates/delete/{id}")
	public String deleteRate(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			rateService.delete(id);
			redirectAttributes.addFlashAttribute("message",
					"The Shipping rate ID  " + id + " has been deleted successfully.");

		} catch (ShippingRateNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return defaultRedirectURL;

	}

}
