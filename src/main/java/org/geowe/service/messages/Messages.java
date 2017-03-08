/*******************************************************************************
 * Copyright 2017 geowe.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.geowe.service.messages;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class represents Application messages. It is responsible for retrieving
 * messages from property files TODO: i18n
 * 
 * @author lotor
 *
 */
public class Messages {

	public enum Bundle {
		MESSAGES("Messages"), ERRORS("ErrorMessages");

		private String bundleName;

		Bundle(String bundleName) {
			this.bundleName = bundleName;
		}

		public String getBundleName() {
			return bundleName;
		}
	}

	Locale[] supportedLocales = { new Locale("es"), Locale.ENGLISH };

	private ResourceBundle resourceBundle;

	public Messages(Bundle bundle, Locale locale) {
		super();
		this.resourceBundle = ResourceBundle.getBundle(bundle.getBundleName(),
				isLocaleSupported(locale) ? locale : Locale.ENGLISH);
	}

	private boolean isLocaleSupported(Locale locale) {
		boolean localeSupported = false;
		for (Locale supportedLocale : supportedLocales) {
			if (supportedLocale.equals(locale)) {
				localeSupported = true;
				break;
			}
		}
		return localeSupported;
	}

	public String getMessage(String key) {
		return resourceBundle.getString(key);
	}

	public void getErrorMessage(String key) {
		resourceBundle.getString(key);
	}

}
