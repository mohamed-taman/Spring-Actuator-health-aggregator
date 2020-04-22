package com.siriusx.composite.health;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.CompositeReactiveHealthIndicator;
import org.springframework.boot.actuate.health.DefaultReactiveHealthIndicatorRegistry;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class CompositeHealthApplication {

    @Autowired
    HealthAggregator healthAggregator;

    public static void main(String[] args) {
        SpringApplication.run(CompositeHealthApplication.class, args);
    }

    public Mono<Health> getProductHealth() {
        return getHealth("1");
    }

    public Mono<Health> getRecommendationHealth() {
        return getHealth(" ");
    }

    public Mono<Health> getReviewHealth() {
        return getHealth("123");
    }

    private Mono<Health> getHealth(String url) {
        if (!url.isBlank()) {
            return Mono.just(new Health.Builder()
                    .up()
                    .build());
        }
        return Mono.just(new Health.Builder()
                .down(new IllegalStateException("Out of Service."))
                .build());
    }
    
    @Bean(name = "Old API- Core Services")
    ReactiveHealthIndicator oldCoreServices() {

        var registry = new DefaultReactiveHealthIndicatorRegistry(new LinkedHashMap<>());

        registry.register("Product Service", this::getProductHealth);
        registry.register("Recommendation Service", this::getRecommendationHealth);
        registry.register("Review Service", this::getReviewHealth);

        return new CompositeReactiveHealthIndicator(healthAggregator, registry);
    }

    @Bean(name = "Core System Microservices")
    ReactiveHealthContributor CoreServicesHealth() {

        ReactiveHealthIndicator productHealthIndicator = this::getProductHealth,
                recommendationHealthIndicator = this::getRecommendationHealth,
                reviewHealthIndicator = this::getReviewHealth;

        Map<String, ReactiveHealthContributor> allIndicators = Map.of(
                "Product Service", productHealthIndicator,
                "Recommendation Service", recommendationHealthIndicator,
                "Review Service", reviewHealthIndicator);

        return CompositeReactiveHealthContributor.fromMap(allIndicators);
    }

}
