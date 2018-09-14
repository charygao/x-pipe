package com.ctrip.xpipe.redis.console.health.redisconf.rewrite;

import com.ctrip.xpipe.endpoint.HostPort;
import com.ctrip.xpipe.redis.console.alert.ALERT_TYPE;
import com.ctrip.xpipe.redis.console.alert.AlertManager;
import com.ctrip.xpipe.redis.console.health.BaseSamplePlan;
import com.ctrip.xpipe.redis.console.health.Sample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author wenchao.meng
 *         <p>
 *         Jun 13, 2017
 */
@Component
@Lazy
public class DefaultRedisConfCollector implements RedisConfCollector{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AlertManager alertManager;

    @Override
    public void collect(Sample<InstanceRedisConfResult> sample) {


        BaseSamplePlan<InstanceRedisConfResult> samplePlan = sample.getSamplePlan();
        String clusterId = samplePlan.getClusterId();
        String shardId = samplePlan.getShardId();

        samplePlan.getHostPort2SampleResult().forEach((endpoint, result) -> {

            if(result.isSuccess()){
                logger.info("{}: success", endpoint);
            }else {

                logger.info("{}: fail:{}", endpoint, result.getFailReason());
                if(result.getFailReason() instanceof RedisConfFailException){
                    alertManager.alert(clusterId, shardId, new HostPort(endpoint.getHost(), endpoint.getPort()), ALERT_TYPE.REDIS_CONF_REWRITE_FAILURE, String.format("%s:%s",
                            result.getFailReason().getClass().getSimpleName(), new HostPort(endpoint.getHost(), endpoint.getPort())));
                }
            }

        });

    }
}
