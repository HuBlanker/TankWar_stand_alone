package com.huyan;
import java.awt.*;

/**
 * 血块类
 * @author 刘攀帅
 *
 */

public class Blood {
	/**
	 * 血块的宽度和高度
	 */
	public static  int BLOOD_WIDTH = 15,BLOOD_HEIGHT = 15;
	
	int x,y,w,h;
	TankWarClient tc;
	
	/**
	 * 血块是否死亡
	 */
	private boolean live = true;
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	/**
	 * pos是血块运动的轨迹，用pos中的几个点控制
	 */
	private int step = 0;
	private int [][] pos = { {450,285},{440,275},{430,265},{420,255},{410,245},{402,220}
							};
	
	/**
	 * 构造方法
	 */
	public Blood() {
		x = pos[0][0];
		y = pos[0][1];
		w = BLOOD_WIDTH;
		h = BLOOD_HEIGHT;
	}
	
	/**
	 * draw方法，画出自身
	 * @param g
	 */
	public void draw(Graphics g) {
		if (!live) {
			return ;
		}
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
		move();
	}

	/**
	 * 血块移动方法，按照pos中的轨迹移动
	 */
	private void move() {
		step++;
		if(step == pos.length){
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	
	/**
	 * 返回血块外围的矩形
	 * @return Rectangle
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
}
