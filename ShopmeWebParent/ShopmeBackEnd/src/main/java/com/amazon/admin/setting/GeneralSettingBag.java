package com.amazon.admin.setting;

import java.util.List;

import com.amazon.common.entity.setting.Setting;
import com.amazon.common.entity.setting.SettingBag;

public class GeneralSettingBag extends SettingBag {

	public GeneralSettingBag(List<Setting> listSettings) {
		super(listSettings);
		// TODO Auto-generated constructor stub
	}
	
	public void updateCurrencySymbol(String value) {
		super.update("CURRENCY_SYMBOL", value);
	}
public void updateSiteLogo(String value) {
	super.update("SITE_LOGO", value);
}
}
