package com.huyan;
import java.awt.*;

/**
 * ǽ����
 * @author ����˧
 *
 */

public class Wall {
	
	/**
	 * ǽ�ڵ�λ�ü���ȣ��߶�
	 */
	int x,y,w,h;
	TankWarClient tc;
	
	/**
	 * ���췽��
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
	 * draw��������������
	 * @param g
	 */
	public void  draw(Graphics g) {
		g.fillRect(x, y, w, h);
		
	}

	/**
	 * ����ǽ����Χ�ľ���
	 * @return
	 */
	public Rectangle getRect() {
		return new Rectangle(x,y,w,h);
	}



}
