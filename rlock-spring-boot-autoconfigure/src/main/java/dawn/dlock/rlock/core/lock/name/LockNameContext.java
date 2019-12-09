package dawn.dlock.rlock.core.lock.name;

import dawn.dlock.rlock.config.RlockConfig;
import dawn.dlock.rlock.constant.RlockConstant;
import dawn.dlock.rlock.util.ExpressionUtils;
import dawn.dlock.rlock.util.VariableUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * 锁名称相关上下文
 *
 * @author HEBO
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LockNameContext {

	/**
	 * 指定的key
	 * 例:  my:lock
	 */
	private String specialValue;

	/**
	 * redis lock前缀
	 *
	 * @see RlockConfig#getPrefix()
	 */
	private String prefix;

	/**
	 * 项目名
	 *
	 * @see RlockConfig#getProjectName()
	 */
	private String projectName;

	/**
	 * 类全路径
	 * 例: com.github.dawn9117.service.UserService
	 */
	private String fullClassName;

	/**
	 * 类名
	 * 例: UserService
	 */
	private String simpleClassName;

	/**
	 * 方法名
	 * 例: addUser
	 */
	private String methodName;

	/**
	 * expression解析的值
	 * 例: li san
	 */
	private String expressionValue;

	public static Builder builder() {
		return new Builder();
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Builder {
		private Method method;

		private Object[] params;

		private RlockConfig config;

		private String key;

		public Builder config(RlockConfig config) {
			this.config = config;
			return this;
		}

		public Builder method(Method method) {
			this.method = method;
			return this;
		}

		public Builder params(Object[] params) {
			this.params = params;
			return this;
		}

		public Builder key(String key) {
			this.key = key;
			return this;
		}


		public LockNameContext build() {
			LockNameContext context = new LockNameContext();
			context.setPrefix(config.getPrefix());
			context.setFullClassName(method.getDeclaringClass().getName());
			context.setSimpleClassName(method.getDeclaringClass().getSimpleName());
			context.setMethodName(method.getName());
			context.setProjectName(config.getProjectName());
			if (StringUtils.startsWith(key, RlockConstant.EL_PREFIX)) {
				String expressionValue = ExpressionUtils.parseSpel(key, VariableUtils.getParameterNames(method), params, String.class);
				context.setExpressionValue(expressionValue);
			}
			return context;
		}
	}
}
