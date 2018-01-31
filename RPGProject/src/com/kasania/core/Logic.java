package com.kasania.core;

import com.kasania.graphics.Window;

public interface Logic {
	
	void init() throws Exception;
	void input(Window window);
	void update(float interval);
	void render(Window window);
	void cleanUp();
}
