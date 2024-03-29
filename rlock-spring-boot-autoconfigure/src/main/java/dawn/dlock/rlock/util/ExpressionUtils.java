package dawn.dlock.rlock.util;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author HEBO
 */
public class ExpressionUtils {
	/**
	 * Spel 解析器
	 */
	private static final ExpressionParser parser = new SpelExpressionParser();

	public static <T> T parseSpel(String expression, String[] paramNames, Object[] params, Class<T> type) {
		EvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < paramNames.length; i++) {
			context.setVariable(paramNames[i], params[i]);
		}
		return parser.parseExpression(expression).getValue(context, type);
	}


}
