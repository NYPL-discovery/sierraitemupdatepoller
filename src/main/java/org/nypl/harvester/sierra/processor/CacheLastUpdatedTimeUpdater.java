package org.nypl.harvester.sierra.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.nypl.harvester.sierra.config.EnvironmentConfig;
import org.nypl.harvester.sierra.exception.SierraHarvesterException;
import org.nypl.harvester.sierra.utils.HarvesterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;

import redis.clients.jedis.Jedis;

public class CacheLastUpdatedTimeUpdater implements Processor {

  private RetryTemplate retryTemplate;

  private static Logger logger = LoggerFactory.getLogger(CacheLastUpdatedTimeUpdater.class);

  public CacheLastUpdatedTimeUpdater(RetryTemplate retryTemplate) {
    this.retryTemplate = retryTemplate;
  }

  @Override
  public void process(Exchange exchange) throws SierraHarvesterException {
    try {
      String timeToUpdateInCache = exchange.getIn().getBody(String.class);

      retryTemplate.execute(new RetryCallback<Boolean, SierraHarvesterException>() {

        @Override
        public Boolean doWithRetry(RetryContext context) throws SierraHarvesterException {
          return updateCache(timeToUpdateInCache);
        }

      });
    } catch (Exception e) {
      logger.error(HarvesterConstants.getResource()
          + " : Error occurred while updating redis with the last updated time - ", e);
      throw new SierraHarvesterException(HarvesterConstants.getResource()
          + " : Error occurred while updating redis with the last updated time - " + e.getMessage());
    }
  }

  private Boolean updateCache(String timeToUpdateInCache) throws SierraHarvesterException {
    Jedis jedis = null;
    try {
      jedis = new Jedis(EnvironmentConfig.redisHost, EnvironmentConfig.redisPort);
      jedis.set(HarvesterConstants.REDIS_KEY_LAST_UPDATED_TIME, timeToUpdateInCache);
      return true;
    } catch (Exception e) {
      logger.error(HarvesterConstants.getResource()
          + " : Error occurred while getting last updated time from redis server - ", e);
      throw new SierraHarvesterException(HarvesterConstants.getResource()
          + " : Error occurred while getting last updated time from redis server");
    } finally {
      jedis.close();
    }
  }

}
