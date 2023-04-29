package com.github.psahu81011.persister.health;

import com.codahale.metrics.health.HealthCheck;
import com.github.psahu81011.persister.ESBulkProcessor;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.cluster.health.ClusterHealthStatus;


public class ESHealthCheck
        extends HealthCheck {

  private final ESBulkProcessor bulkProcessor;

  public ESHealthCheck(final ESBulkProcessor bulkProcessor) {
    this.bulkProcessor = bulkProcessor;
  }

  @Override
  protected Result check() throws Exception {
    ClusterHealthStatus status = bulkProcessor.getClient()
            .cluster()
            .health(new ClusterHealthRequest("*"), RequestOptions.DEFAULT)
            .getStatus();
    String clusterName = bulkProcessor.getClient()
            .cluster()
            .health(new ClusterHealthRequest("*"), RequestOptions.DEFAULT)
            .getClusterName();
    if (status.value() > ClusterHealthStatus.YELLOW.value()) {
      return Result.unhealthy("ES cluster " + clusterName + " is unhealthy.");
    }
    return Result.healthy("ES cluster " + clusterName + " is healthy.");
  }
}