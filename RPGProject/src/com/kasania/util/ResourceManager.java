package com.kasania.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ResourceManager {
	
	public static String loadResource(String path) throws ClassNotFoundException, IOException {
		String result;
		try(InputStream in = Class.forName(ResourceManager.class.getName()).getResourceAsStream(path);
				Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
		}
		
		return result;
		
	}

}
