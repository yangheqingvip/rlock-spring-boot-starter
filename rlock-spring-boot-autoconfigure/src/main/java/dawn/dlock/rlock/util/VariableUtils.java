package dawn.dlock.rlock.util;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * @author HEBO
 */
public class VariableUtils {
	/**
	 * 本地变量名工具
	 */
	private static final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

	/**
	 * 获取方法上的参数名称列表
	 *
	 * @param method
	 * @return
	 */
	public static String[] getParameterNames(Method method) {
		return discoverer.getParameterNames(method);
	}

}
