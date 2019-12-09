package dawn.dlock.rlock.core.lock.name;

import org.apache.commons.lang3.StringUtils;

import java.util.StringJoiner;

/**
 * @author HEBO
 */
public class DefaultNameBuilder implements LockNameBuilder {

	@Override
	public String build(LockNameContext context) throws Throwable {
		StringJoiner joiner = new StringJoiner(DELIMITER);
		joiner.add(context.getPrefix());
		joiner.add(context.getProjectName());
		joiner.add(context.getFullClassName());
		joiner.add(context.getMethodName());
		if (StringUtils.isNotEmpty(context.getExpressionValue())) {
			joiner.add(context.getExpressionValue());
		}
		return joiner.toString();
	}
}
