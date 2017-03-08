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
package org.geowe.service.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an entity with service API information
 * 
 * @author lotor
 *
 */
public class ResourceDef implements Serializable {

	private static final long serialVersionUID = 2399195074298616002L;
	private Set<String> httpMethod;
	private String description;
	private Set<ParamDef> queryParameters;
	private Set<ParamDef> pathParameters;
	private BodyDef body;
	private String exampleUrl;

	public ResourceDef() {
		super();
		this.httpMethod = new HashSet<String>();
	}

	public Set<String> getHttpMethod() {
		return httpMethod;
	}

	public void addHttpMethod(String method) {
		httpMethod.add(method);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<ParamDef> getQueryParameters() {
		return queryParameters;
	}

	public void addQueryParameter(String name, String description, String type, boolean required) {

		addQueryParameter(new ParamDef(name, description, type, required));
	}

	public void addQueryParameter(ParamDef paramDef) {
		if (queryParameters == null) {
			this.queryParameters = new HashSet<ResourceDef.ParamDef>();
		}
		this.queryParameters.add(paramDef);
	}

	public Set<ParamDef> getPathParameters() {
		return pathParameters;
	}

	public void addPathParameter(String name, String description, String type, boolean required) {
		if (this.pathParameters == null) {
			this.pathParameters = new HashSet<ResourceDef.ParamDef>();
		}
		this.pathParameters.add(new ParamDef(name, description, type, required));
	}

	public BodyDef getBody() {
		return body;
	}

	public void body(String type, String description) {
		this.body = new BodyDef(type, description);
	}

	public String getExampleUrl() {
		return exampleUrl;
	}

	public void setExampleUrl(String exampleUrl) {
		this.exampleUrl = exampleUrl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((exampleUrl == null) ? 0 : exampleUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceDef other = (ResourceDef) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (exampleUrl == null) {
			if (other.exampleUrl != null)
				return false;
		} else if (!exampleUrl.equals(other.exampleUrl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OptionsEntity [description=" + description + ", exampleUrl=" + exampleUrl + "]";
	}

	/**
	 * 
	 * @author lotor
	 *
	 */
	public class ParamDef {
		private String name;
		private String description;
		private String type;
		private boolean required;

		public ParamDef(String name, String description, String type, boolean required) {
			super();
			this.name = name;
			this.description = description;
			this.type = type;
			this.required = required;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public boolean isRequired() {
			return required;
		}

		public void setRequired(boolean required) {
			this.required = required;
		}
	}

	/**
	 * 
	 * @author lotor
	 *
	 */
	public class BodyDef {
		private String type;
		private String description;

		public BodyDef(String type, String description) {
			this.type = type;
			this.description = description;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}
}
