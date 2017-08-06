package com.huyan;


import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

/**这个类的作用是坦克大战游戏的主窗口
 * author huyan
 */

public class TankWarClient extends Frame{
	
	/**
	 * 这个静态变量是主窗口的宽度和高度
	 */
	public static final int  GAME_WIDTH = 800;
	public static final int  GAME_HEIGHT = 600;
	
	/**
	 * 这个静态变量是第一次生成敌方坦克时敌方坦克的数量
	 */
	public static final int TANK_NUMBER = 10;
	
	/**
	 * 游戏中的两堵墙
	 */
	Wall w1 = new Wall(300, 200, 20, 150, this), w2 = new Wall(500, 400, 150, 20, this);
	
	/**
	 * new出主战坦克即我方坦克
	 */
	Tank myTank = new Tank(50, 50,true,Tank.Direction.STOP,this);
	
	/**
	 * 血块
	 */
	Blood b = new Blood();
	
	/**
	 * 分别为爆炸列表，子弹列表，敌方坦克列表。
	 */
	List<Explode> explodes = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<Tank> tanks = new ArrayList<Tank>();
	
	Image offScreenImage = null;
	

	
	/**
	 * paint方法，画出游戏窗口及游戏内的内容
	 */
	public void paint(Graphics g) {
		
		/**
		 * 在左上角显示当前子弹数量，当前爆炸数量，当前敌方坦克数量，
		 * 我方坦克剩余生命值，我方坦克目前所处的x，y位置
		 */
		g.drawString("Missiles count:" + missiles.size(), 10, 50);
		g.drawString("Explodes  count:" + explodes.size(), 10, 70);
		g.drawString("Tanks  count:" + tanks.size(), 10, 90);
		g.drawString("Tanks  life :" + myTank.getLife(), 10, 110);
		g.drawString("x = " + myTank.getx(), 10, 130);
		g.drawString("y = " + myTank.gety(), 50, 130);
		
		/**
		 * 画出坦克列表中的坦克，且执行坦克撞击墙壁，撞击其他坦克时的操作，
		 */
		for(int i = 0;i < tanks.size();i ++) {
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}
		
		/**
		 * 画出子弹列表中的子弹，并且处理子弹打击坦克，打击墙壁等操作
		 */
		for(int i = 0; i < missiles.size();i ++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		
		/**
		 * 画出爆炸列表中的爆炸
		 */
		for(int i = 0;i < explodes.size();i ++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		/**
		 * 画出墙壁，我方坦克，血块
		 */
		w1.draw(g);
		w2.draw(g);
		myTank.draw(g);
		myTank.eat(b);
		b.draw(g);
	}
	
	/**
	 * 用来处理画面闪烁的问题的方法
	 */
	
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();  //获取虚拟屏幕画笔
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);		//刷新背景
		gOffScreen.setColor(c);
		paint(gOffScreen);					//调用paint方法活出图形
		g.drawImage(offScreenImage, 0, 0, null);     //将虚拟屏幕的内容画到屏幕里
	}

	/**
	 * 显示主窗口
	 */
	public void launchFrame() {
		
		/**
		 * 初始化10只敌方坦克
		 */
		for (int i = 0; i < TANK_NUMBER; i++) {
			tanks.add(new Tank(50 + 40*(i + 2),50,false,Tank.Direction.D,this));
		}
		/**
		 * 设置主窗口的标题，大小等属性
		 */
		this.setLocation(200,100);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		
		this.addKeyListener(new KeyMonitor());
		this.setVisible(true);
		
		/**
		 * 线程开始
		 */
		new Thread(new PaintThread()).start(); 
	}
	
	/**
	 * 主方法
	 */
	public static void main(String[] args) {
		TankWarClient twc = new TankWarClient();
		twc.launchFrame();

	}
	
	/**
	 * 线程内部类
	 * @author 刘攀帅
	 *
	 */
	private class PaintThread implements Runnable{

		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}

	/**
	 * 按键监听
	 */
	private class KeyMonitor extends KeyAdapter{

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
		  myTank.keyPressed(e);
		}
	
		
	}
}
