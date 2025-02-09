package com.yellobook.config;

////@Configuration
////@EnableRedisRepositories
//public class RedisConfig {
//
////    private final RedisProperties redisProperties;
////
////    public RedisConfig(RedisProperties redisProperties) {
////        this.redisProperties = redisProperties;
////    }
////
////    @Bean
////    public RedisConnectionFactory redisConnectionFactory() {
////        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
////        redisConfig.setHostName(redisProperties.getHost());
////        redisConfig.setPort(redisProperties.getPort());
////        redisConfig.setPassword(redisProperties.getPassword());
////        return new LettuceConnectionFactory(redisConfig);
////    }
////
////    @Bean
////    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
////        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
////        redisTemplate.setKeySerializer(new StringRedisSerializer());
////        redisTemplate.setValueSerializer(new StringRedisSerializer());
////        redisTemplate.setConnectionFactory(redisConnectionFactory);
////        return redisTemplate;
////    }
//}
