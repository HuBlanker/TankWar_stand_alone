package com.huyan;

import java.awt.*;
import java.util.List;

/**
 * 子弹类
 * @author 刘攀帅
 *
 */

public class Missile {
	/**
	 * 子弹的移动速度
	 */
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	/**
	 * 子弹的宽度和高度
	 */
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	int x,y;
	Tank.Direction dir;
	private boolean live = true;
	private TankWarClient tc;
	
	/**
	 * 子弹是敌方的还是我方的
	 */
	private boolean good;
	
	public boolean isLive() {
		return live;
	}

	/**
	 * 构造方法
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
	 * 重载的构造方法，可以初始化好坏，及方向
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
	 * draw方法，画出自身
	 * @param g
	 */
	public void draw(Graphics g) {
		/**
		 * 如果子弹死亡，从子弹列表中溢出此子弹
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
	 * 子弹移动方法
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
		 * 子弹出界，使得子弹死亡
		 */
		if(x < 0 || y < 0 || x > TankWarClient.GAME_WIDTH || y > TankWarClient.GAME_HEIGHT) {
			live = false; 
		}
	}
	
	/**
	 * 返回子弹外围的矩形
	 * @return Rectangle
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	/**
	 * 处理子弹击中坦克的方法
	 * @param t 坦克
	 * @return 击中返回true，没有击中返回false。
	 */
	public boolean hitTank(Tank t) {
		/**
		 * 子弹击中坦克，如果是我方坦克，血量减去20，判断是否还活着，
		 * 如果是敌方坦克，使其死亡
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
			 * 击中坦克子弹死亡
			 */
			this.live = false;
			/**
			 * 击中位置产生一个新的爆炸
			 */
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return  false;
	}
	
	/**
	 * 循环判断打击一个列表的坦克，调用hitTank
	 * @param tanks 坦克列表
	 * @return 击中任意一个返回true，没有击中返回false。
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
	 * 处理子弹击中墙壁的方法，如果击中，使得子弹死亡
	 * @param w  墙壁
	 * @return 击中返回true，没有击中返回false。
	 */
	public boolean hitWall(Wall w) {
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true ;
		}
		return false;
	}

}
