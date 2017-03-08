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
package org.geowe.service.exception;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.geowe.service.messages.Messages;
import org.geowe.service.messages.Messages.Bundle;
import org.geowe.service.model.error.ErrorEntityFactory;
import org.jboss.logging.Logger;

/**
 * A mapper for aplication resource not found exceptions.
 * 
 * @author lotor
 *
 */
@Provider
public class ResourceNotFoundMapper implements
		ExceptionMapper<NotFoundException> {

	private final Logger logger = Logger
			.getLogger(ResourceNotFoundMapper.class);

	@Context
	private HttpServletRequest request;

	@Override
	public Response toResponse(NotFoundException exception) {
		ErrorEntityFactory errorEntityFactory = new ErrorEntityFactory(
				request.getLocale());

		log(exception);

		Response response = Response
				.status(Status.BAD_REQUEST)
				.entity(errorEntityFactory.getBadParameterError(exception,
						request)).build();

		return response;
	}

	private void log(Exception exception) {
		logger.warn("Request: " + request.getRequestURI() + "\n"
						+ new Messages(Bundle.ERRORS, request.getLocale())
								.getMessage("controled.error")
				+ "\n" + exception.getMessage(), exception);
	}

}
