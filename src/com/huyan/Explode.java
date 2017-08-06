package com.huyan;

import java.awt.Color;
import java.awt.Graphics;

/**
 * ��ը��
 * @author ����˧
 *
 */

public class Explode {

	int x,y;
	/**
	 * ��ը�Ƿ�����
	 */
	private boolean live = true;
	
	/**
	 * ��ը��Բ��ֱ��
	 */
	int [] diameter = {4,7,9,16,26,35,49,30,15,5};
	/**
	 * ��ǰ��ʾ��Բ���ǵڼ���
	 */
	int step = 0;
	
	TankWarClient tc;
	
	/**
	 * ���췽��
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
	 * draw������������ը����
	 * @param g
	 */
	public void draw(Graphics g) {
		/**
		 * �����ը�������ӱ�ը�б����Ƴ��˱�ը
		 */
		if (!live) {
			tc.explodes.remove(this);
			return ;
		}
		
		/**
		 * �����ը��ϣ��ñ�ը����
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
