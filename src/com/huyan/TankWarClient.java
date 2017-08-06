package com.huyan;


import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

/**������������̹�˴�ս��Ϸ��������
 * author huyan
 */

public class TankWarClient extends Frame{
	
	/**
	 * �����̬�����������ڵĿ�Ⱥ͸߶�
	 */
	public static final int  GAME_WIDTH = 800;
	public static final int  GAME_HEIGHT = 600;
	
	/**
	 * �����̬�����ǵ�һ�����ɵз�̹��ʱ�з�̹�˵�����
	 */
	public static final int TANK_NUMBER = 10;
	
	/**
	 * ��Ϸ�е�����ǽ
	 */
	Wall w1 = new Wall(300, 200, 20, 150, this), w2 = new Wall(500, 400, 150, 20, this);
	
	/**
	 * new����ս̹�˼��ҷ�̹��
	 */
	Tank myTank = new Tank(50, 50,true,Tank.Direction.STOP,this);
	
	/**
	 * Ѫ��
	 */
	Blood b = new Blood();
	
	/**
	 * �ֱ�Ϊ��ը�б��ӵ��б��з�̹���б�
	 */
	List<Explode> explodes = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<Tank> tanks = new ArrayList<Tank>();
	
	Image offScreenImage = null;
	

	
	/**
	 * paint������������Ϸ���ڼ���Ϸ�ڵ�����
	 */
	public void paint(Graphics g) {
		
		/**
		 * �����Ͻ���ʾ��ǰ�ӵ���������ǰ��ը��������ǰ�з�̹��������
		 * �ҷ�̹��ʣ������ֵ���ҷ�̹��Ŀǰ������x��yλ��
		 */
		g.drawString("Missiles count:" + missiles.size(), 10, 50);
		g.drawString("Explodes  count:" + explodes.size(), 10, 70);
		g.drawString("Tanks  count:" + tanks.size(), 10, 90);
		g.drawString("Tanks  life :" + myTank.getLife(), 10, 110);
		g.drawString("x = " + myTank.getx(), 10, 130);
		g.drawString("y = " + myTank.gety(), 50, 130);
		
		/**
		 * ����̹���б��е�̹�ˣ���ִ��̹��ײ��ǽ�ڣ�ײ������̹��ʱ�Ĳ�����
		 */
		for(int i = 0;i < tanks.size();i ++) {
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}
		
		/**
		 * �����ӵ��б��е��ӵ������Ҵ����ӵ����̹�ˣ����ǽ�ڵȲ���
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
		 * ������ը�б��еı�ը
		 */
		for(int i = 0;i < explodes.size();i ++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		/**
		 * ����ǽ�ڣ��ҷ�̹�ˣ�Ѫ��
		 */
		w1.draw(g);
		w2.draw(g);
		myTank.draw(g);
		myTank.eat(b);
		b.draw(g);
	}
	
	/**
	 * ������������˸������ķ���
	 */
	
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();  //��ȡ������Ļ����
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);		//ˢ�±���
		gOffScreen.setColor(c);
		paint(gOffScreen);					//����paint�������ͼ��
		g.drawImage(offScreenImage, 0, 0, null);     //��������Ļ�����ݻ�����Ļ��
	}

	/**
	 * ��ʾ������
	 */
	public void launchFrame() {
		
		/**
		 * ��ʼ��10ֻ�з�̹��
		 */
		for (int i = 0; i < TANK_NUMBER; i++) {
			tanks.add(new Tank(50 + 40*(i + 2),50,false,Tank.Direction.D,this));
		}
		/**
		 * ���������ڵı��⣬��С������
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
		 * �߳̿�ʼ
		 */
		new Thread(new PaintThread()).start(); 
	}
	
	/**
	 * ������
	 */
	public static void main(String[] args) {
		TankWarClient twc = new TankWarClient();
		twc.launchFrame();

	}
	
	/**
	 * �߳��ڲ���
	 * @author ����˧
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
	 * ��������
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
