package com.amazon.admin.category;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.amazon.admin.user.export.AbstractExporter;
import com.amazon.common.entity.Category;
import com.amazon.common.entity.User;

public class CategoryCsvExporter extends AbstractExporter{
	
	

	public void export(List<Category> listCategory, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv",".csv","category_");
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//		String timestamp = format.format(new Date());
//		String fileName = "users_" + timestamp + ".csv";
//		response.setContentType("text/csv");
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=" + fileName;
//		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "Category ID", "Category Name" };
		String[] fieldMapping= {"id","name"};
		csvWriter.writeHeader(csvHeader);
		
		for (Category category : listCategory) {
			category.setName(category.getName().replace("--","  "));
			csvWriter.write(category, fieldMapping);
		}
		csvWriter.close();
	}
}
