/*******************************************************************************
 * Copyright 2016 lotor
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
package org.geowe.service.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.geowe.service.model.OperationData;
import org.geowe.service.model.error.ErrorEntityFactory;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Filter example. Filters are mainly used to modify or process incoming and
 * outgoing request headers or response headers. They execute before and after
 * request and response processing.
 * 
 * @author lotor
 *
 */
@Provider
@IntersectionFilter
public class IntersectionFilterImpl implements ContainerRequestFilter {

	private final Logger log = Logger.getLogger(IntersectionFilterImpl.class);

	@Context
	private HttpServletRequest request;

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		String json = IOUtils.toString(context.getEntityStream(), "UTF-8");

		OperationData opData = getBody(json);

		if (opData.getSourceData().isEmpty()
				|| opData.getOverlayData().isEmpty()) {
			buildErrorResponse(context);
		}

		InputStream is = new ByteArrayInputStream(json.getBytes());
		context.setEntityStream(is);

	}

	private OperationData getBody(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		OperationData opData = mapper.readValue(json, OperationData.class);

		return opData;
	}

	private void buildErrorResponse(ContainerRequestContext context) {
		ErrorEntityFactory errorEntityFactory = new ErrorEntityFactory(
				request.getLocale());
		context.abortWith(Response
				.status(Status.BAD_REQUEST)
				.entity(errorEntityFactory
						.getBadRequestError(new IllegalArgumentException(
								"Error reported by Intersecion Filter")))
				.build());
	}

}
