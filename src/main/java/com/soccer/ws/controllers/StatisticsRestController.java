package com.soccer.ws.controllers;

import com.soccer.ws.dto.AccountStatisticDTO;
import com.soccer.ws.service.CacheAdapter;
import com.soccer.ws.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by u0090265 on 16/09/16.
 */
@RestController
@Api(value = "Statictics REST api", description = "Statictics REST operations")
public class StatisticsRestController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsRestController.class);
    private final CacheAdapter cacheAdapter;

    public StatisticsRestController(SecurityUtils securityUtils, MessageSource messageSource, CacheAdapter cacheAdapter) {
        super(securityUtils, messageSource);
        this.cacheAdapter = cacheAdapter;
    }

    @RequestMapping(value = "/statistics/season/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get statictics", nickname = "getStatictics")
    public ResponseEntity<List<AccountStatisticDTO>> getStatisticsForSeason(@PathVariable long id) {
        return new ResponseEntity<>(cacheAdapter.getStatisticsForSeason(id, isLoggedIn()),
                HttpStatus.OK);
    }
}
