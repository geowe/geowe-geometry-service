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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.geowe.service.messages.Messages;
import org.geowe.service.messages.Messages.Bundle;
import org.geowe.service.model.error.ErrorEntityFactory;
import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.ReaderException;

/**
 * A mapper for aplication exceptions.
 * 
 * @author lotor
 *
 */
@Provider
public class GGSExceptionMapper implements ExceptionMapper<Exception> {

	private final Logger logger = Logger.getLogger(GGSExceptionMapper.class);
	private Messages errorMessages;

	@Context
	private HttpServletRequest request;

	@Override
	public Response toResponse(Exception exception) {
		errorMessages = new Messages(Bundle.ERRORS, request.getLocale());
		Response response = null;
		ErrorEntityFactory errorEntityFactory = new ErrorEntityFactory(
				request.getLocale());
		if (exception instanceof ReaderException
				|| exception instanceof IllegalArgumentException
				|| exception instanceof UnsupportedOperationException) {
			log(exception);
			response = Response.status(Status.BAD_REQUEST)
					.entity(errorEntityFactory.getBadRequestError(exception))
					.build();
		} else {
			log(exception);
			exception.printStackTrace();
			response = Response.status(Status.INTERNAL_SERVER_ERROR)
					.tag(errorMessages.getMessage("internal.server.error"))
					.build();
		}

		return response;
	}

	private void log(Exception exception) {
		logger.warn("Request: " + request.getRequestURI() + "\n"
				+ errorMessages.getMessage("controled.error") + "\n"
				+ exception.getMessage(), exception);
	}

}
