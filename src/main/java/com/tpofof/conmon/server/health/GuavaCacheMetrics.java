package com.tpofof.conmon.server.health;

import static com.codahale.metrics.MetricRegistry.name;

import java.util.HashMap;
import java.util.Map;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.google.common.cache.Cache;

/**
 * http://antrix.net/posts/2014/codahale-metrics-guava-cache/
 * 
 * @author david
 *
 */
@SuppressWarnings("serial")
public class GuavaCacheMetrics extends HashMap< String, Metric > implements MetricSet {

    /**
     * Wraps the provided Guava cache's statistics into Gauges suitable for reporting via Codahale Metrics
     * <p/>
     * The returned MetricSet is suitable for registration with a MetricRegistry like so:
     * <p/>
     * <code>registry.registerAll( GuavaCacheMetrics.metricsFor( "MyCache", cache ) );</code>
     * 
     * @param cacheName This will be prefixed to all the reported metrics
     * @param cache The cache from which to report the statistics
     * @return MetricSet suitable for registration with a MetricRegistry
     */
    public static MetricSet metricsFor( String cacheName, final Cache<?, ?> cache ) {

        GuavaCacheMetrics metrics = new GuavaCacheMetrics();

        metrics.put( name( cacheName, "hitRate" ), new Gauge< Double >() {
            public Double getValue() {
                return cache.stats().hitRate();
            }
        } );

        metrics.put( name( cacheName, "hitCount" ), new Gauge< Long >() {
            public Long getValue() {
                return cache.stats().hitCount();
            }
        } );

        metrics.put( name( cacheName, "missCount" ), new Gauge< Long >() {
            public Long getValue() {
                return cache.stats().missCount();
            }
        } );

        metrics.put( name( cacheName, "loadExceptionCount" ), new Gauge< Long >() {
            public Long getValue() {
                return cache.stats().loadExceptionCount();
            }
        } );

        metrics.put( name( cacheName, "evictionCount" ), new Gauge< Long >() {
            public Long getValue() {
                return cache.stats().evictionCount();
            }
        } );

        return metrics;
    }

    private GuavaCacheMetrics() {
    }

    public Map< String, Metric > getMetrics() {
        return this;
    }

}
