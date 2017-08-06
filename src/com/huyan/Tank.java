package com.huyan;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.*;
import java.util.*;

/**
 * ̹����
 */

public class Tank {
	
	/**
	 * ̹���ƶ��ٶ�
	 */
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	/**
	 * ̹�˵Ŀ�Ⱥ͸߶�
	 */
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	/**
	 * ̹�˵�����ֵ
	 */
	public static final int LIFE = 100;
	
	
	
	TankWarClient tc;
	
	private static Random r = new Random();
	/**
	 * �Ƿ����
	 */
	private boolean  live = true;
	/**
	 * ���ҷ�̹�˻��ǵз�̹��
	 */
	private boolean good;
	/**
	 * ��ǰ����ֵ
	 */
	private int life = LIFE;
	
	private int x,y;
	private int oldx,oldy;
	private boolean bL = false,bU = false,bR = false,bD = false;
	
	/**
	 * ̹���ƶ����򣬹�9������stop
	 * @author ����˧
	 *
	 */
	enum Direction{L,LU,U,RU,R,RD,D,LD,STOP};
	
	/**
	 * ̹�˷������Ͳ����
	 */
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;
	
	/**
	 * Ѫ��
	 */
	private BloodBar bb = new BloodBar();
	
	/**
	 * ̹�����ǰ���ľ���
	 */
	private int step = r.nextInt(15) + 3;
	
	/**
	 * ���췽������ʼ��x��y��good
	 */
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.oldx = x;
		this.oldy = y;
		this.good = good;
	}
	
	/**
	 * ���췽��
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
	 * ̹�˵�draw��������������
	 * @param g
	 */
	public void draw(Graphics g) {
		 /**
		  * ���̹���������ǵз�̹�ˣ���̹���б����Ƴ���̹��
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
		 * ������ҷ�̹�˻���Ѫ��
		 */
		if (good) {
			bb.draw(g);
		}
		ptDraw(g);
		
		move();
	}

	/**
	 * ������Ͳ
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
	 * ̹�˵��ƶ�������
	 */
	private void move() {
		/**
		 * �ƶ�ǰ��¼��һ�ε�λ��
		 */
		this.oldx = x;
		this.oldy = y;
		
		/**
		 * �жϷ����ƶ�
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
		 * ���̹���ƶ�������ֹͣ������Ͳ��������ƶ�����
		 */
		if (dir != Direction.STOP) {
			this.ptDir = dir;
		}
		
		/**
		 * ���̹�˳���(�߳���Ϸ�߽�)����
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
		 * ����ǵз�̹�ˣ���������ƶ��������
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
			 * �з�̹����������ӵ�
			 */
			if (r.nextInt(40) > 36) {
				this.fire();
			}
		}
	}
	
	/**
	 * ���������������������ң�ctrl������A����˸��������ڵ���F2��������̹�ˣ�F3ˢ��5���з�̹�ˡ�
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
	 * ����̧�������̧��crtlʱ��Ӧ�������������ܼ����⡣
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
	 * �жϰ�����ǰ������Ĺ�ϵ
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
	 * ���𷽷����Ե�ǰ̹�˵�λ�ü����ó��ӵ��ĳ�ʼλ�ã��ӵ�����Ϊ��Ͳ�����ҽ��ӵ������ӵ��б�
	 * @return ����ֵΪһ���ӵ�
	 */
	public Missile fire() {
		/**
		 * ���̹������������
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
	 * ����fire��������������ķ�����һ���ӵ�
	 * @param dir ̹���ƶ�����
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
	 * ����Rectangle
	 * @return ̹����Χ�ľ���
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	/**
	 * ����̹��ײǽ���⣬��̹��ײǽ��ʹ��ص���һ��λ�������������ͷ���
	 * @param w ǽ��
	 * @return ײǽ����true��û��ײ������false��
	 */
	public boolean collidesWithWall (Wall w) {
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.stay();
			return true ;
		}
		return false;
	}
	
	/**
	 * ����з�̹�˻���ײ�������⣬��̹�˻���ײ��ʱ���ص���һ��λ�ã����������
	 * @param tanks ̹���б�
	 * @return	ײ������true��û��ײ������false
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
	 * ̹��ֹͣ����������̹�˻ص���һ��λ��
	 */
	private void stay() {
		x = oldx;
		y = oldy;
	}
	
	/**
	 * A�����𣬵���dire��dir����������˸����������һ���ӵ�
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
	 * ������ս̹�˳ٵ�Ѫ��ķ�������̹�˺�Ѫ����ײ����̹�˻ظ���Ѫ��Ѫ������
	 * @param b Ѫ��
	 * @return �ٵ�����true��û�гٵ�����false
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
	 * Ѫ���ڲ��࣬��̹���Ϸ�����̹�˵�Ѫ��
	 * @author ����˧
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
