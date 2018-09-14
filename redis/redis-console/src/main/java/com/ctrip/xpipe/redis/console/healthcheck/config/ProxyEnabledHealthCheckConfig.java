package com.ctrip.xpipe.redis.console.healthcheck.config;

import com.ctrip.xpipe.redis.console.config.ConsoleConfig;

/**
 * @author chen.zhu
 * <p>
 * Aug 30, 2018
 */
public class ProxyEnabledHealthCheckConfig extends AbstractHealthCheckConfig {

    public ProxyEnabledHealthCheckConfig(ConsoleConfig consoleConfig) {
        super(consoleConfig);
    }

    @Override
    public int downAfterMilli() {
        return consoleConfig.getDownAfterCheckNumsThroughProxy() * consoleConfig.getHealthyDelayMilliThroughProxy();
    }

    @Override
    public int getHealthyDelayMilli() {
        return consoleConfig.getHealthyDelayMilliThroughProxy();
    }
}
