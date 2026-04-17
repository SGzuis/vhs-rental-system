package com.locadora.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "omdbClient", url = "${omdb.api.url}")
public interface OmdbClient {
    
    @GetMapping("/")
    OmdbResponse getMovieByTitle(@RequestParam("t") String title, 
                                  @RequestParam("apikey") String apiKey);
    
    @GetMapping("/")
    OmdbResponse getMovieByImdbId(@RequestParam("i") String imdbId, 
                                   @RequestParam("apikey") String apiKey);
}
