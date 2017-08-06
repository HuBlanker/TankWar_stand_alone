package com.huyan;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.*;
import java.util.*;

/**
 * 坦克类
 */

public class Tank {
	
	/**
	 * 坦克移动速度
	 */
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	/**
	 * 坦克的宽度和高度
	 */
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	/**
	 * 坦克的生命值
	 */
	public static final int LIFE = 100;
	
	
	
	TankWarClient tc;
	
	private static Random r = new Random();
	/**
	 * 是否活着
	 */
	private boolean  live = true;
	/**
	 * 是我方坦克还是敌方坦克
	 */
	private boolean good;
	/**
	 * 当前生命值
	 */
	private int life = LIFE;
	
	private int x,y;
	private int oldx,oldy;
	private boolean bL = false,bU = false,bR = false,bD = false;
	
	/**
	 * 坦克移动方向，共9个包含stop
	 * @author 刘攀帅
	 *
	 */
	enum Direction{L,LU,U,RU,R,RD,D,LD,STOP};
	
	/**
	 * 坦克方向和炮筒方向
	 */
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;
	
	/**
	 * 血条
	 */
	private BloodBar bb = new BloodBar();
	
	/**
	 * 坦克随机前进的距离
	 */
	private int step = r.nextInt(15) + 3;
	
	/**
	 * 构造方法，初始化x，y，good
	 */
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.oldx = x;
		this.oldy = y;
		this.good = good;
	}
	
	/**
	 * 构造方法
	 * @param x
	 * @param y
	 * @param good
	 * @param dir
	 * @param tc
	 */
	public  Tank(int x,int y,boolean good,Direction dir,TankWarClient tc) {
		this(x, y,good);
		this.dir = dir;
		this.tc = tc;
		
	}
	
	public int getx() {
		return x;
	}
	
	public int gety() {
		return y;
	}
	
	/**
	 * 坦克的draw方法，画出自身
	 * @param g
	 */
	public void draw(Graphics g) {
		 /**
		  * 如果坦克死了且是敌方坦克，从坦克列表中移除此坦克
		  */
		if (!live) {
			if (!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		Color c = g.getColor();
		if (good) {
			g.setColor(Color.RED);
		}
		else {
			g.setColor(Color.BLUE);
		}
		
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		/**
		 * 如果是我方坦克画出血条
		 */
		if (good) {
			bb.draw(g);
		}
		ptDraw(g);
		
		move();
	}

	/**
	 * 画出炮筒
	 * @param g
	 */
	public void ptDraw(Graphics g) {
		switch (ptDir) {
		case L:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
			break;
		default:
			break;
		}
	}

	/**
	 * 坦克的移动方法，
	 */
	private void move() {
		/**
		 * 移动前记录上一次的位置
		 */
		this.oldx = x;
		this.oldy = y;
		
		/**
		 * 判断方向，移动
		 */
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
		case STOP:
		}
		/**
		 * 如果坦克移动方向不是停止，让炮筒方向等于移动方向
		 */
		if (dir != Direction.STOP) {
			this.ptDir = dir;
		}
		
		/**
		 * 解决坦克出界(走出游戏边界)问题
		 */
		if (x < 0) {
			x = 0;
		}
		if (y < 30) {
			y = 30;
		}
		if (x + Tank.WIDTH > TankWarClient.GAME_WIDTH) {
			x = TankWarClient.GAME_WIDTH - Tank.WIDTH;
		}
		if (y + Tank.HEIGHT > TankWarClient.GAME_HEIGHT) {
			y = TankWarClient.GAME_HEIGHT - Tank.HEIGHT;
		}
		
		/**
		 * 如果是敌方坦克，随机方向移动随机距离
		 */
		if (!good) {
			Direction [] dirs = Direction.values();
			if (step == 0) {
				step = r.nextInt(15) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step -- ;
			/**
			 * 敌方坦克随机发射子弹
			 */
			if (r.nextInt(40) > 36) {
				this.fire();
			}
		}
	}
	
	/**
	 * 按键监听，监听上下左右，ctrl键开火，A键向八个方向发射炮弹，F2重生己方坦克，F3刷新5个敌方坦克。
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_F2:
			if (!this.isLive()) {
				this.live = true;
				this.life = LIFE;
			}
			break;
		case KeyEvent.VK_F3:
			if (tc.tanks.size() <= 0) {
				for (int i = 0; i < TankWarClient.TANK_NUMBER/2; i++) {
					tc.tanks.add(new Tank(50 + 40*(i + 2),50,false,Tank.Direction.D,tc));
				}
			}
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		case KeyEvent.VK_A:
			superFire();
			break;
		}
		
		locateDirection();
		
		

	}

	/**
	 * 按键抬起监听，抬起crtl时反应，解决开火过于密集问题。
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_CONTROL :
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		}
		
		locateDirection();
	}
	
	/**
	 * 判断按键与前进方向的关系
	 */
	private void locateDirection() {
		if (bL && !bU && !bR && !bD) dir = Direction.L;
		if (bL && bU && !bR && !bD) dir = Direction.LU;
		if (!bL && bU && !bR && !bD) dir = Direction.U;
		if (!bL && bU && bR && !bD) dir = Direction.RU;
		if (!bL && !bU && bR && !bD) dir = Direction.R;
		if (!bL && !bU && bR && bD) dir = Direction.RD;
		if (!bL && !bU && !bR && bD) dir = Direction.D;
		if (bL && !bU && !bR && bD) dir = Direction.LD;
		if (!bL && !bU && !bR && !bD) dir = Direction.STOP;
		
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	/**
	 * 开火方法，以当前坦克的位置计算后得出子弹的初始位置，子弹方向为炮筒方向，且将子弹加入子弹列表
	 * @return 返回值为一颗子弹
	 */
	public Missile fire() {
		/**
		 * 如果坦克死亡，结束
		 */
		if (!live) {
			return null;
		}
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2; 
		Missile m = new Missile(x, y,good, ptDir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	/**
	 * 重载fire方法，向传入参数的方向发射一颗子弹
	 * @param dir 坦克移动方向
	 * @return
	 */
	public Missile fire(Direction dir) {
		if (!live) {
			return null;
		}
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2; 
		Missile m = new Missile(x, y,good, dir,this.tc);
		tc.missiles.add(m);
		return m;
	}	
	
	/**
	 * 返回Rectangle
	 * @return 坦克周围的矩形
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	/**
	 * 处理坦克撞墙问题，当坦克撞墙，使其回到上一个位置重新随机距离和方向。
	 * @param w 墙壁
	 * @return 撞墙返回true，没有撞到返回false；
	 */
	public boolean collidesWithWall (Wall w) {
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.stay();
			return true ;
		}
		return false;
	}
	
	/**
	 * 处理敌方坦克互相撞到的问题，当坦克互相撞到时，回到上一个位置，重新随机。
	 * @param tanks 坦克列表
	 * @return	撞到返回true，没有撞到返回false
	 */
	public boolean collidesWithTanks(java.util.List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if (this != t) {
				if (this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
					this.stay();
					t.stay();
					return true ;
				}
			}
		}
		return false;
	}

	/**
	 * 坦克停止方法，即让坦克回到上一个位置
	 */
	private void stay() {
		x = oldx;
		y = oldy;
	}
	
	/**
	 * A键开火，调用dire（dir）方法，向八个方向各发射一颗子弹
	 */
	public void superFire() {
		Direction [] dirs = Direction.values();
		for (int i = 0; i < 8; i++) {
			fire(dirs[i]);
		}
	}
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean isGood() {
		return good;
	}
	
	/**
	 * 处理主战坦克迟到血块的方法。当坦克和血块相撞，让坦克回复满血，血块死掉
	 * @param b 血块
	 * @return 迟到返回true，没有迟到返回false
	 */
	public boolean eat(Blood b) {
		if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			this.life = LIFE;
			b.setLive(false);
			return true ;
		}
		return false;
	}
	
	/**
	 * 血条内部类，在坦克上方画出坦克的血条
	 * @author 刘攀帅
	 *
	 */
	private class BloodBar{
		public  void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-5, WIDTH,5 );
			int w = WIDTH * life /LIFE;
			g.fillRect(x, y-5, w, 5);
			g.setColor(c);
		}
	}

	
}
