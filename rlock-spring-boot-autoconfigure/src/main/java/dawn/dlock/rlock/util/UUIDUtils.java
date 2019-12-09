package dawn.dlock.rlock.util;

import java.util.UUID;

/**
 * @author HEBO
 */
public class UUIDUtils {

	public static String random() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String random(String suffix) {
		return random().concat(suffix);
	}


}
