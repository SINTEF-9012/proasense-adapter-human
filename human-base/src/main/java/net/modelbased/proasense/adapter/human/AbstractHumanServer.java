/**
 * Copyright (C) 2014-2015 SINTEF
 *
 *     Brian Elves�ter <brian.elvesater@sintef.no>
 *     Shahzad Karamat <shazad.karamat@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.modelbased.proasense.adapter.human;

import net.modelbased.proasense.adapter.base.AbstractBaseAdapter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public abstract class AbstractHumanServer extends AbstractBaseAdapter {

	public AbstractHumanServer(){
	}


	@GET
	@Path("/adapter/status")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getServerStatus() {
		String result = "ProaSense Human Adapter Server running...";

		// Return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}


	@GET
	@Path("/adapter/properties")
	@Produces(MediaType.TEXT_PLAIN)
	public Response printServerProperties() {
		String result = this.adapterProperties.toString();

		// Return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}
}
