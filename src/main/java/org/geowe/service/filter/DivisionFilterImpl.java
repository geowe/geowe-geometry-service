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
import org.geowe.service.geometry.engine.JTSGeoEngineerHelper;
import org.geowe.service.messages.Messages;
import org.geowe.service.messages.Messages.Bundle;
import org.geowe.service.model.DivisionData;
import org.geowe.service.model.error.ErrorEntityFactory;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Filter for validate input data for division. OperationData.source: must be a
 * collection of polygons OperationData.overlay: must be a LineString
 * 
 * @author rafa@geowe.org
 *
 */
@Provider
@DivisionFilter
public class DivisionFilterImpl implements ContainerRequestFilter {

	private final Logger log = Logger.getLogger(DivisionFilterImpl.class);
	private Messages errorMessages;

	JTSGeoEngineerHelper helper;
	@Context
	private HttpServletRequest request;

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		String json = IOUtils.toString(context.getEntityStream(), "UTF-8");
		
		DivisionData opData = getBody(json);

		String wktdivisionLine = opData.getDivisionLine().getWkt();
		helper = new JTSGeoEngineerHelper();
		if (!isWktLineString(wktdivisionLine)) {
			buildErrorResponse(context, "overlay.must.be.line");
		}
		if(request.getPathInfo().contains("polygon") && !isWktPolygon(opData.getGeomToDivide().getWkt())){
			buildErrorResponse(context, "overlay.must.be.polygon");
		}
		if(request.getPathInfo().contains("line") && !isWktLineString(opData.getGeomToDivide().getWkt())){
			buildErrorResponse(context, "overlay.must.be.line");
		}
		
		InputStream is = new ByteArrayInputStream(json.getBytes());
		context.setEntityStream(is);

	}

	private DivisionData getBody(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		DivisionData opData = mapper.readValue(json, DivisionData.class);

		return opData;
	}

	private void buildErrorResponse(ContainerRequestContext context, String msgKey) {
		ErrorEntityFactory errorEntityFactory = new ErrorEntityFactory(request.getLocale());
		errorMessages = new Messages(Bundle.ERRORS, request.getLocale());
		context.abortWith(
				Response.status(Status.BAD_REQUEST)
						.entity(errorEntityFactory.getBadRequestError(
								new IllegalArgumentException(
										errorMessages.getMessage(msgKey))))
						.build());
	}
	
	private boolean isWktPolygon(String wkt){
		return helper.getGeom(wkt).getGeometryType().equals("Polygon");
	}
	
	private boolean isWktLineString(String wkt){
		return helper.getGeom(wkt).getGeometryType().equals("LineString");
	}

}
