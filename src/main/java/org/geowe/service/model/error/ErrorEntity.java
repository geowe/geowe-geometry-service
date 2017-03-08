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
package org.geowe.service.model.error;

import java.io.Serializable;

/**
 * It represents an error response. Provide additional information about
 * problems encountered while performing an operation. It is a simplified
 * implementation of JSON-API http://jsonapi.org/format/#error-objects
 * 
 * @author lotor
 */
public class ErrorEntity implements Serializable {

	private static final long serialVersionUID = -6779758535801989044L;

	private int id;
	private String code;
	private String status;
	private String title;
	private String detail;
	private Object source;
	private String link;

	public int getId() {
		return id;
	}

	/**
	 * A unique identifier for this particular occurrence of the problem
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	/**
	 * An application-specific error code, expressed as a string value
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * The HTTP status code applicable to this problem, expressed as a string
	 * value
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * A short, human-readable summary of the problem that SHOULD NOT change
	 * from occurrence to occurrence of the problem, except for purposes of
	 * localization
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	/**
	 * A human-readable explanation specific to this occurrence of the problem.
	 * 
	 * @param detail
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Object getSource() {
		return source;
	}

	/**
	 * An object containing references to the source of the error
	 * 
	 * @param source
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	public String getLink() {
		return link;
	}

	/**
	 * A link that leads to further details about this particular occurrence of
	 * the problem
	 * 
	 * @param link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + id;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		ErrorEntity other = (ErrorEntity) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (id != other.id)
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ErrorEntity [id=" + id + ", code=" + code + ", status="
				+ status + ", title=" + title + ", detail=" + detail
				+ ", source=" + source + ", link=" + link + "]";
	}

}
