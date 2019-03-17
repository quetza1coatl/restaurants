package com.quetzalcoatl.restaurants.web;

import com.quetzalcoatl.restaurants.model.User;
import com.quetzalcoatl.restaurants.web.json.JsonUtil;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class TestUtil {
    private TestUtil(){ }


    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    public static <T> T readFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }
    public static <T> List<T> readListFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValues(getContent(result), clazz);
    }

    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> void assertMatch(String[] fields, T actual, T expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, fields);
    }
    public static <T> void assertMatch(String[] fields, Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields(fields).isEqualTo(expected);
    }
    public static <T> void assertMatch(String[] fields, Iterable<T> actual, T... expected) {
        assertMatch(fields, actual, List.of(expected));
    }
}
