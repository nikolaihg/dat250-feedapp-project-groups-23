package dat250.FeedApp.config

import com.fasterxml.jackson.databind.ObjectMapper
import java.time.Duration
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext

@Configuration
@EnableCaching
class CacheConfig(
        @Value("\${feedapp.cache.ttl:PT60S}") private val defaultTtl: Duration,
        private val objectMapper: ObjectMapper
) {

    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): RedisCacheManager {
        val serializer = GenericJackson2JsonRedisSerializer(objectMapper)
        val config =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(defaultTtl)
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(serializer)
                        )
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build()
    }
}
