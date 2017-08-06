package com.huyan;
import java.awt.*;

/**
 * 墙壁类
 * @author 刘攀帅
 *
 */

public class Wall {
	
	/**
	 * 墙壁的位置及宽度，高度
	 */
	int x,y,w,h;
	TankWarClient tc;
	
	/**
	 * 构造方法
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param tc
	 */
	public Wall(int x, int y, int w, int h, TankWarClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	
	/**
	 * draw方法，画出自身
	 * @param g
	 */
	public void  draw(Graphics g) {
		g.fillRect(x, y, w, h);
		
	}

	/**
	 * 返回墙壁外围的矩形
	 * @return
	 */
	public Rectangle getRect() {
		return new Rectangle(x,y,w,h);
	}



}
