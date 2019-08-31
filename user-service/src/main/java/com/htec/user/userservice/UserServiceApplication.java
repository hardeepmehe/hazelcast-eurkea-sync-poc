package com.htec.user.userservice;

import com.hazelcast.cache.HazelcastCacheManager;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.core.DistributedObjectEvent;
import com.hazelcast.core.DistributedObjectListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.eureka.one.EurekaOneDiscoveryStrategyFactory;
import com.hazelcast.instance.HazelcastInstanceCacheManager;
import com.hazelcast.instance.HazelcastInstanceImpl;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableCaching
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Value("${hazelcast.port:5701}")
    private int hazelcastPort;
    @Bean
    public HazelcastInstance getHazelcastInstance(EurekaClient eurekaClient) {
        EurekaOneDiscoveryStrategyFactory.setEurekaClient(eurekaClient);
        Config config = new Config();
        config.getNetworkConfig().setPort(hazelcastPort);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getEurekaConfig()
                .setEnabled(true)
                .setProperty("self-registration", "true")
                .setProperty("namespace", "hazelcast")

                .setProperty("use-metadata-for-host-and-port", "true");

        config.addMapConfig(
                new MapConfig()
                        .setName("users")
                        .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                        .setEvictionPolicy(EvictionPolicy.LRU)
                        .setTimeToLiveSeconds(20));


//		Config config = new Config();
//		config.setInstanceName("hazelcast-cache");
//
//		MapConfig allUsersCache = new MapConfig();
//		allUsersCache.setTimeToLiveSeconds(20);
//		allUsersCache.setEvictionPolicy(EvictionPolicy.LFU);
//		config.getMapConfigs().put("alluserscache",allUsersCache);
//
//		MapConfig usercache = new MapConfig();
//		usercache.setTimeToLiveSeconds(20);
//		usercache.setEvictionPolicy(EvictionPolicy.LFU);
//		config.getMapConfigs().put("usercache",usercache);
//
//		return config;


        return Hazelcast.newHazelcastInstance(config);
    }

    @Autowired
    HazelcastInstance hazelcastInstance;

//    @Bean
//    public CacheManager cacheManager(EurekaClient eurekaClient){
//        // The Stormpath SDK knows to use the Spring CacheManager automatically
//
//        return new HazelcastInstanceCacheManager((HazelcastInstanceImpl) getHazelcastInstance(eurekaClient));
//    }

    @PostConstruct
    public void registerListner() {
        hazelcastInstance.addDistributedObjectListener(new DistributedObjectListener() {
            @Override
            public void distributedObjectCreated(DistributedObjectEvent distributedObjectEvent) {
                System.out.println("distributed object creater" + distributedObjectEvent.getObjectName());
            }

            @Override
            public void distributedObjectDestroyed(DistributedObjectEvent distributedObjectEvent) {
                System.out.println("distributed object deleted" + distributedObjectEvent.getObjectName());

            }
        });
    }
}
