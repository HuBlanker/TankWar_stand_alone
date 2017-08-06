package com.huyan;

import java.awt.*;
import java.util.List;

/**
 * �ӵ���
 * @author ����˧
 *
 */

public class Missile {
	/**
	 * �ӵ����ƶ��ٶ�
	 */
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	/**
	 * �ӵ��Ŀ�Ⱥ͸߶�
	 */
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	int x,y;
	Tank.Direction dir;
	private boolean live = true;
	private TankWarClient tc;
	
	/**
	 * �ӵ��ǵз��Ļ����ҷ���
	 */
	private boolean good;
	
	public boolean isLive() {
		return live;
	}

	/**
	 * ���췽��
	 * @param x
	 * @param y
	 * @param dir
	 */
	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	/**
	 * ���صĹ��췽�������Գ�ʼ���û���������
	 * @param x
	 * @param y
	 * @param good
	 * @param dir
	 * @param tc
	 */
	public Missile(int x,int y,boolean good,Tank.Direction dir,TankWarClient tc){
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
		
	}
	
	/**
	 * draw��������������
	 * @param g
	 */
	public void draw(Graphics g) {
		/**
		 * ����ӵ����������ӵ��б���������ӵ�
		 */
		if (!live) {
			tc.missiles.remove(this);
			return ;
		}
		Color c = g.getColor();
		if (good) {
			g.setColor(Color.BLACK);
		}
		else {
			g.setColor(Color.BLUE);
		}
		
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
		
		
	}

	/**
	 * �ӵ��ƶ�����
	 */
	private void move() {
		switch (dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		default:
			break;
		}
		/**
		 * �ӵ����磬ʹ���ӵ�����
		 */
		if(x < 0 || y < 0 || x > TankWarClient.GAME_WIDTH || y > TankWarClient.GAME_HEIGHT) {
			live = false; 
		}
	}
	
	/**
	 * �����ӵ���Χ�ľ���
	 * @return Rectangle
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	/**
	 * �����ӵ�����̹�˵ķ���
	 * @param t ̹��
	 * @return ���з���true��û�л��з���false��
	 */
	public boolean hitTank(Tank t) {
		/**
		 * �ӵ�����̹�ˣ�������ҷ�̹�ˣ�Ѫ����ȥ20���ж��Ƿ񻹻��ţ�
		 * ����ǵз�̹�ˣ�ʹ������
		 */
		if (this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			if(t.isGood()) {
				t.setLife(t.getLife()-20);
				if (t.getLife() <= 0) { 
					t.setLive(false);
				}
			} else {
				t.setLive(false);
			}
			/**
			 * ����̹���ӵ�����
			 */
			this.live = false;
			/**
			 * ����λ�ò���һ���µı�ը
			 */
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return  false;
	}
	
	/**
	 * ѭ���жϴ��һ���б��̹�ˣ�����hitTank
	 * @param tanks ̹���б�
	 * @return ��������һ������true��û�л��з���false��
	 */
	public boolean hitTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			if (hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �����ӵ�����ǽ�ڵķ�����������У�ʹ���ӵ�����
	 * @param w  ǽ��
	 * @return ���з���true��û�л��з���false��
	 */
	public boolean hitWall(Wall w) {
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true ;
		}
		return false;
	}

}
