package iisi.batch.util;

import java.io.File;

public final class Utils {
	
	public final static String OUTPUT_FOLDER = "export";

	public static File getOutputFolder(String dataSetId) {
		File base = new File(getOutputBase(), dataSetId);
		if (!base.exists()) {
			base.mkdirs();
		}
		return base;
	}
	
	public static File getOutputBase() {
		return new File(getBase(), OUTPUT_FOLDER);
	}
	
	public static boolean runJar() {
		String className = Utils.class.getName().replace('.', '/');
		String classJar = Utils.class.getResource("/" + className + ".class").toString();
		return classJar.startsWith("jar:");
	}

	public static File getBase() {
		if (runJar()) {
			String path = new File(Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getPath();
			if (path.startsWith("file:")) {
				path = path.replace("file:", "");
			}
			return new File(path);
		}
		return new File(System.getProperty("user.dir"));
	}
	
	private Utils() {
	}
	
}
