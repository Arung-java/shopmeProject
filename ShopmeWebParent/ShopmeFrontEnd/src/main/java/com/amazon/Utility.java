package com.amazon;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.amazon.security.oauth.CustomerOAuth2User;
import com.amazon.setting.CurrencySettingBag;
import com.amazon.setting.EmailSettingBag;

public class Utility {
	
	public static String getSiteUrl(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		
		return siteURL.replace(request.getServletPath(),"");
		
	}
	
	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		JavaMailSenderImpl mailSender=new JavaMailSenderImpl();
		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPort());
		mailSender.setUsername(settings.getUsername());
		mailSender.setPassword(settings.getPassword());
		
		Properties mailProperties=new Properties(); 
		mailProperties.setProperty("mail.smtp.ssl.trust", settings.getHost());
		//mailProperties.setProperty("mail.smtp.host", "smtp.gmail.com");
		//mailProperties.setProperty("mail.smtp.port", "587");
		//mailProperties.setProperty("mail.smtp.debug", "true");
		//mailProperties.setProperty("mail.smtp.auth", "true");
		//mailProperties.setProperty("mail.smtp.starttls.enable", "true");
		
	//	mailProperties.setProperty("mail.smtp.starttls.required", "true");
		mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable",settings.getSmtpSecured());
		
		mailSender.setJavaMailProperties(mailProperties);
		return mailSender;
	}
	
	public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		if(principal == null) return null;
		String customerEmail=null;
		
		if(principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof  RememberMeAuthenticationToken) {
			customerEmail=request.getUserPrincipal().getName();
		}else if(principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken auth2Token=	(OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User=(CustomerOAuth2User) auth2Token.getPrincipal();
			customerEmail=oauth2User.getEmail();
		}
		return customerEmail;
		
	}
	public static String formatCurrency(float amount,CurrencySettingBag settings) {
		String symbol = settings.getSymbol();
		String symbolPosition = settings.getSymbolPosition();
		String decimalPointType = settings.getDecimalPointType();
		String thousandPointType = settings.getThousandPointType();
		int decimalDigits = settings.getDecimalDigits();
		
		String pattern=symbolPosition.equals("Before price") ? symbol : "";
		pattern += "###,###";
		
		if(decimalDigits >0 ) {
			pattern += ".";
			for (int count = 0; count < decimalDigits; count++) pattern += "#";
		}
		 pattern=symbolPosition.equals("After price") ? symbol : "";
		
		char thousandSeparator=thousandPointType.equals("POINT") ? '.' : ',';
		char decimalSeparator=decimalPointType.equals("POINT") ? '.' : ',';
		
		DecimalFormatSymbols decimalFormatSymbols=DecimalFormatSymbols.getInstance();
		decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
		decimalFormatSymbols.setGroupingSeparator(thousandSeparator);
		
		DecimalFormat formatter=new DecimalFormat(pattern,decimalFormatSymbols);
		return formatter.format(amount);
	}
	/*
	 * public static void main(String[] args) { float amount =123678.995f; String
	 * formattedCurrency = formatCurrency(amount);
	 * System.out.println(formattedCurrency); }
	 */

}
