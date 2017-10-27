package com.onlyou.jmeter.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Entity/vo/pojo 等 bean对象 转 JSON 字符
     *
     * @param object 实体/VO/POJO 等bean对象
     * @return JSON 字符
     */
    public static String objectToString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
