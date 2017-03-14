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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response.Status;

import org.geowe.service.messages.Messages;
import org.geowe.service.messages.Messages.Bundle;
//TODO: create and complete errors
public class ErrorEntityFactory {

	private final Messages errorMessages;

	public ErrorEntityFactory(Locale locale) {
		super();
		this.errorMessages = new Messages(Bundle.ERRORS, locale);
	}

	public ErrorEntity getBadRequestError(Exception exception) {
		ErrorEntity error = new ErrorEntity();
		error.setId(1);
		error.setStatus(String.valueOf(Status.BAD_REQUEST.getStatusCode()));
		error.setCode("BR.422");
		error.setTitle(errorMessages.getMessage("bad.request"));
		error.setDetail(exception.getMessage());
		error.setSource("Unprocessable Entity: " + exception.getCause());
		error.setLink("TODO: Error documentation");

		return error;
	}

	public ErrorEntity getValidationFailRequest(String msg) {
		ErrorEntity error = new ErrorEntity();
		error.setId(11);
		error.setStatus(String.valueOf(Status.CONFLICT.getStatusCode()));
		error.setCode("BR.409");
		error.setTitle(errorMessages.getMessage("validation.fail"));
		error.setDetail(msg);
		error.setSource("Unprocessable Entity: ");
		error.setLink("TODO: Error documentation");

		return error;
	}

	public ErrorEntity getBadParameterError(Exception exception,
			HttpServletRequest request) {
		ErrorEntity error = new ErrorEntity();
		error.setId(2);
		error.setStatus(String.valueOf(Status.BAD_REQUEST.getStatusCode()));
		error.setCode("BR.400");
		error.setTitle(errorMessages.getMessage("incorrect.query.param"));
		error.setDetail(exception.getMessage());
		error.setSource(request.getRequestURI() + ": "
				+ exception.getCause());
		error.setLink("TODO: Error documentation");

		return error;
	}

	public ErrorEntity getUnExpectedError(Exception exception) {
		ErrorEntity error = new ErrorEntity();
		error.setId(500);
		error.setStatus(String.valueOf(Status.INTERNAL_SERVER_ERROR
				.getStatusCode()));
		error.setCode("ISE.500");
		error.setTitle(errorMessages.getMessage("internal.server.error"));
		error.setDetail(exception.getMessage());
		error.setSource(exception.getCause().getLocalizedMessage());
		error.setLink("TODO: Error documentation");

		return error;
	}
	
	public ErrorEntity getTopologyError(Exception exception,
			HttpServletRequest request) {
		ErrorEntity error = new ErrorEntity();
		error.setId(409);
		error.setStatus(String.valueOf(Status.CONFLICT.getStatusCode()));
		error.setCode("BR.409");
		error.setTitle(errorMessages.getMessage("topology.error"));
		error.setDetail(exception.getMessage());
		error.setSource(request.getRequestURI() + ": "
				+ exception.getCause());
		error.setLink("TODO: Error documentation");

		return error;
	}
}
