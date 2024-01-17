package xin.common.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 图片绝对地址与虚拟地址映射
 */

@Configuration
public class FileUploadingConfig extends WebMvcConfigurerAdapter {

    @Value("${realityFileDepositPath}")
    private String realityFileDepositPath;

    @Value("${virtualFileDepositPath}")
    private String virtualFileDepositPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(virtualFileDepositPath)
                .addResourceLocations("file:" + realityFileDepositPath);
    }
}