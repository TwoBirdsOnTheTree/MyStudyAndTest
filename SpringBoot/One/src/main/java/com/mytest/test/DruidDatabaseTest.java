package com.mytest.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
@RestController
public class DruidDatabaseTest {

    @Autowired
    private JdbcTemplate  template;

    @RequestMapping("/druidTest")
    public String test() throws JsonProcessingException {
        List<Map<String, Object>> maps = template.queryForList("select * from temp");
        return new ObjectMapper().writeValueAsString(maps);
    }
}
