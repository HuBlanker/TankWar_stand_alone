package com.huyan;
import java.awt.*;

/**
 * Ѫ����
 * @author ����˧
 *
 */

public class Blood {
	/**
	 * Ѫ��Ŀ�Ⱥ͸߶�
	 */
	public static  int BLOOD_WIDTH = 15,BLOOD_HEIGHT = 15;
	
	int x,y,w,h;
	TankWarClient tc;
	
	/**
	 * Ѫ���Ƿ�����
	 */
	private boolean live = true;
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	/**
	 * pos��Ѫ���˶��Ĺ켣����pos�еļ��������
	 */
	private int step = 0;
	private int [][] pos = { {450,285},{440,275},{430,265},{420,255},{410,245},{402,220}
							};
	
	/**
	 * ���췽��
	 */
	public Blood() {
		x = pos[0][0];
		y = pos[0][1];
		w = BLOOD_WIDTH;
		h = BLOOD_HEIGHT;
	}
	
	/**
	 * draw��������������
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
	 * Ѫ���ƶ�����������pos�еĹ켣�ƶ�
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
	 * ����Ѫ����Χ�ľ���
	 * @return Rectangle
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
}
