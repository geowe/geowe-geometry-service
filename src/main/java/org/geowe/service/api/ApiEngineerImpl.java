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
package org.geowe.service.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import javax.ws.rs.core.MediaType;

import org.geowe.service.messages.Messages;
import org.geowe.service.messages.Messages.Bundle;
import org.geowe.service.model.ResourceDef;


public class ApiEngineerImpl implements ApiEngineer {

	private final Messages messages;

	public ApiEngineerImpl(Locale locale) {
		super();
		messages = new Messages(Bundle.MESSAGES, locale);
	}

	@Override
	public Collection<ResourceDef> getAll() {
		Collection<ResourceDef> operations = new HashSet<ResourceDef>();
		operations.add(getBufferOptions());
		operations.add(getUnionOptions());
		operations.add(getIntersectionOptions());
		return operations;
	}

	// TODO: Build ResourceDef in a more intuitive and elegant way
	public ResourceDef getBufferOptions() {
		ResourceDef resourceDef = new ResourceDef();
		resourceDef.setDescription(messages
				.getMessage("resource.def.buffer.description"));
		resourceDef.addHttpMethod("POST");
		resourceDef.addQueryParameter("distance", messages
				.getMessage("resource.def.buffer.query.param.distance.description"),
				"double", true);
		resourceDef.addQueryParameter("segments", messages
				.getMessage("resource.def.buffer.query.param.segments.description"),
				"int", false);
		resourceDef.body(MediaType.APPLICATION_JSON,
				messages.getMessage("resource.def.buffer.body.description"));
		resourceDef
				.setExampleUrl("http://your-server/ggs/operations/jts/buffer?distance=20");
		return resourceDef;
	}

	public ResourceDef getUnionOptions() {
		ResourceDef opEntity = new ResourceDef();
		opEntity.setDescription(messages
				.getMessage("resource.def.union.description"));
		opEntity.addHttpMethod("POST");
		opEntity.body(MediaType.APPLICATION_JSON,
				messages.getMessage("resource.def.union.body.description"));
		opEntity.setExampleUrl("http://your-server/ggs/operations/jts/union");
		return opEntity;
	}

	public ResourceDef getIntersectionOptions() {
		ResourceDef opEntity = new ResourceDef();
		opEntity.setDescription(messages
				.getMessage("resource.def.intersection.description"));
		opEntity.addHttpMethod("POST");
		opEntity.body(MediaType.APPLICATION_JSON, messages
				.getMessage("resource.def.intersection.body.description"));
		opEntity.setExampleUrl("http://your-server/ggs/operations/jts/intersection");
		return opEntity;
	}

}
