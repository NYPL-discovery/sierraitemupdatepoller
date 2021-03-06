package org.nypl.harvester.sierra.resourceid.poster;

import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.nypl.harvester.sierra.exception.SierraHarvesterException;
import org.nypl.harvester.sierra.model.Resource;

public interface ResourcePoster {

  public Integer postResources(ProducerTemplate template, List<Resource> resources,
      String resourceType) throws SierraHarvesterException;

}
