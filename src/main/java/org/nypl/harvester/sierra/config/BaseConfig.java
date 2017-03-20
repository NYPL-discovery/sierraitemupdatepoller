package org.nypl.harvester.sierra.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.AmazonKinesisClient;

import org.nypl.harvester.sierra.exception.SierraHarvesterException;
import org.nypl.harvester.sierra.utils.HarvesterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class BaseConfig {

  private static Logger logger = LoggerFactory.getLogger(BaseConfig.class);

  @Bean
  public AmazonKinesisClient getAmazonKinesisClient() {
    AWSCredentials awsCredentials =
        new BasicAWSCredentials(EnvironmentConfig.awsAccessKey, EnvironmentConfig.awsSecretKey);

    AmazonKinesisClient amazonKinesisClient = new AmazonKinesisClient(awsCredentials);

    logger.info(HarvesterConstants.getResource() + " : Configured Kinesis Client");

    return amazonKinesisClient;
  }

  @Bean
  public RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();
    FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
    backOffPolicy.setBackOffPeriod(60000);
    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(100);
    retryTemplate.setBackOffPolicy(backOffPolicy);
    retryTemplate.setRetryPolicy(retryPolicy);
    return retryTemplate;
  }

}
