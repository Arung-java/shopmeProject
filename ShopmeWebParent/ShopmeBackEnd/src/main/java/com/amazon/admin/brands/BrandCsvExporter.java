package com.amazon.admin.brands;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.amazon.admin.user.export.AbstractExporter;
import com.amazon.common.entity.Brand;

public class BrandCsvExporter extends AbstractExporter {

	public void export(List<Brand> listBrand, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "Brands_");

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "Brand ID", "Brand Name", "Categories" };
		String[] fieldMapping = { "id", "name", "categories" };
		csvWriter.writeHeader(csvHeader);

		for (Brand brand : listBrand) {
			brand.setName(brand.getName().replace("--", "  "));
			csvWriter.write(brand, fieldMapping);
		}
		csvWriter.close();
	}
}
