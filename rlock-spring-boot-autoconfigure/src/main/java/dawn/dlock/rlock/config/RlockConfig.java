package dawn.dlock.rlock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis lock配置类
 * <p>
 * 该配置类为通用配置
 *
 * @author HEBO
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "rlock")
public class RlockConfig {

	/**
	 * 注解开关(默认开启)
	 */
	private Boolean enabled = Boolean.TRUE;

	/**
	 * 锁名称前缀(所有的锁名称都会以prefix开头)
	 */
	private String prefix = "rlock";

	/**
	 * 项目名称
	 */
	private String projectName;

}
