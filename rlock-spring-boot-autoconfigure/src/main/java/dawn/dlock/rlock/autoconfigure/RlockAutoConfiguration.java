package dawn.dlock.rlock.autoconfigure;

import dawn.dlock.rlock.aspect.RlockAspect;
import dawn.dlock.rlock.config.RlockConfig;
import dawn.dlock.rlock.core.LockInvoker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * redis lock 自动配置类
 *
 * @author HEBO
 */
@Configuration
@ConditionalOnBean(StringRedisTemplate.class)
@ConditionalOnExpression("${rlock.enabled:true}")
@EnableConfigurationProperties(RlockConfig.class)
@Import(RlockAspect.class)
public class RlockAutoConfiguration {

	@Bean
	public LockInvoker lockHelper(StringRedisTemplate redisTemplate) {
		return new LockInvoker(redisTemplate);
	}


}
