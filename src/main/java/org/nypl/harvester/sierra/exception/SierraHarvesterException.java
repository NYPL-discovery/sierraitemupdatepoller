package org.nypl.harvester.sierra.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SierraHarvesterException extends Exception {

  private static final long serialVersionUID = -3886346817890754286L;

  private static Logger logger = LoggerFactory.getLogger(SierraHarvesterException.class);

  public SierraHarvesterException(String message, String resourceType) {
    logger
        .error("APP_ERROR: " + resourceType + " : SierraHarvesterException occurred - " + message);
  }
}
