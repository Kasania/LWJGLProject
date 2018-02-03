package com.kasania.core;

import com.kasania.graphics.Window;

public interface Logic {
	
	void init(Window window) throws Exception;
	void input(Window window, MouseInput mouseInput);
	void update(float interval, MouseInput mouseInput);
	void render(Window window);
	void cleanUp();
}
