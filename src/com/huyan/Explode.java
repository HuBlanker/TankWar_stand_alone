package com.huyan;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 爆炸类
 * @author 刘攀帅
 *
 */

public class Explode {

	int x,y;
	/**
	 * 爆炸是否死亡
	 */
	private boolean live = true;
	
	/**
	 * 爆炸的圆的直径
	 */
	int [] diameter = {4,7,9,16,26,35,49,30,15,5};
	/**
	 * 当前显示的圆形是第几步
	 */
	int step = 0;
	
	TankWarClient tc;
	
	/**
	 * 构造方法
	 * @param x
	 * @param y
	 * @param tc
	 */
	public Explode(int x,int y,TankWarClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	/**
	 * draw方法，画出爆炸自身
	 * @param g
	 */
	public void draw(Graphics g) {
		/**
		 * 如果爆炸死亡，从爆炸列表中移除此爆炸
		 */
		if (!live) {
			tc.explodes.remove(this);
			return ;
		}
		
		/**
		 * 如果爆炸完毕，让爆炸死亡
		 */
		if (step == diameter.length) {
			live  = false;
			step = 0;
			return ;
		}
		
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step ++;
	}

}
