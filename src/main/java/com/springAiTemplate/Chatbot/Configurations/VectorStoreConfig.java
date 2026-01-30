package com.springAiTemplate.Chatbot.Configurations;

import org.springframework.context.annotation.Configuration;

// import redis.clients.jedis.JedisPooled;

@Configuration
public class VectorStoreConfig {

    //  @Bean
    // public OllamaApi ollamaApi(RestClient.Builder restClientBuilder) {
    //     return OllamaApi.builder()
    //             .baseUrl("http://localhost:11434")
    //             .restClientBuilder(restClientBuilder)
    //             .build();
    // }

    // @Bean
    // public EmbeddingModel embeddingModel(OllamaApi ollamaApi) {

    //     return OllamaEmbeddingModel.builder()
    //             .ollamaApi(ollamaApi)
    //             .defaultOptions(
    //                     OllamaEmbeddingOptions.builder()
    //                             .model("nomic-embed-text")
    //                             .build()
    //             )
    //             .build();
    // }

    //  @Bean
    // public JedisPooled jedisPooled() {
    //     return new JedisPooled("localhost", 6379);
    // }

    // @Bean
    // public VectorStore vectorStore(
    //         JedisPooled jedisPooled,
    //         EmbeddingModel embeddingModel) {

    //     return RedisVectorStore.builder(jedisPooled, embeddingModel)
    //             .indexName("rag-index")
    //             .initializeSchema(true)
    //             .build();
    // }
}
