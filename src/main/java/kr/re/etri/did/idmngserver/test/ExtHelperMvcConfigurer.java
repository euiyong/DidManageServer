package kr.re.etri.did.idmngserver.test;

import kr.re.etri.did.utility.ext.ExtHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ExtHelperMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Remove the default MappingJackson2HttpMessageConverter
        // 파라미터 : Predicate 함수형 인터페이스
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);

        // Add the custom converter to the list of converters
        //converters.add(new CustomConverter<>());
        converters.add(gsonHttpMessageConverter());
    }

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(ExtHelper.getDidGson());
        return converter;
    }
}

