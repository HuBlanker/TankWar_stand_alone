#<center>坦克大战
##项目需求
1. 能够四处移动
2. 能够打击敌人
3. 敌人能够移动
4. 能够模拟爆炸
5. 能够产生障碍
6. 能够增长生命
##项目思路
**1. 产生一个窗口，大小为800\*600**;  
**2. 设置窗口监听，关闭按钮实现**    
匿名类的用法：  
<pre>
      this.addWindowListener(new WindowAdapter() {  
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}  
		});
</pre>
匿名类的使用场合：类短小，不涉及将来的拓展，不涉及重要的业务逻辑。  
**3. 使窗口不能被改变大小**  
 this.setResizable(false);   
**4.画出代表坦克的实心圆形。**  
paint方法的使用，重写，重写后不需要调用。  
**5. 使坦克动起来。**  
将坦克位置设置为变量，类似于电影的处理方法，使用线程（内部类实现），循环调用paint，改变位置变量的值，实现重画.  

**5.1.使用双缓冲解决画面闪烁问题。**  
原因：paint方法没有画完就需要刷新屏幕，导致画面闪烁。  
解决：双缓冲，即在屏幕后虚拟一张画幕，先在虚拟画幕上画完整张图片，再将整幅画幕一次画在屏幕上。
<pre>	
       public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(800,600);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();  //获取虚拟屏幕画笔
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, 800, 600);		//刷新背景
		gOffScreen.setColor(c);
		paint(gOffScreen);		 //调用paint方法活出图形
		g.drawImage(offScreenImage, 0, 0, null);     //将虚拟屏幕的内容画到屏幕里
	}
</pre>
**5.2.将需要经常修改的量设置为常量，比如游戏窗口的宽度和高度**

**6.让坦克受控制**  
添加键盘监听，对应上下左右键位改变坦克的位置X，Y值。  
<pre>private class KeyMonitor extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_LEFT:
				x -= 5;
				break;
			case KeyEvent.VK_UP:
				y -= 5;
				break;
			case KeyEvent.VK_RIGHT:
				x += 5;
				break;
			case KeyEvent.VK_DOWN:
				y += 5;
				break;
			}
		}
       }  
</pre>
**7.代码重构，将坦克单独包装，以便后续使用多个坦克对象**  
新建Tank类，将坦克位置x，y作为属性，新建draw()方法实现坦克将自身画在屏幕上，KeyPressed()方法实现坦克的移动，在TankClient类中，对应的方法只是调用坦克的方法，简化了TankClient的代码。  
**8.让坦克可以向8个方向移动**  
设置4个Boolean值（bL = false,bU = false,bR = false,bD = false），表示某个方向是否被按下。  
设置枚举类型Direction，共有{L,LU,U,RU,R,RD,D,LD,STOP}八个方向。  
设置locateDirection()方法，根据Boolean值确定某个方向是否被按下，以此确定坦克移动的方向。
设置move()方法，根据确定的移动方向使坦克的位置改变。
在keyPressed()方法中，调用move0方法，每次重画时确定当前坦克前进方向。  
**此版本遗留问题：**按下右键，坦克向右移动，抬起右键，坦克已久向右移动，再次按下下键，坦克向右下移动且不再受到操控。*应该是方向的值的刷新问题，即坦克移动后，将方向设置为默认？*  
**8.1.问题处理办法：**添加键盘按键抬起监听，当某个键位抬起之后，将Boolean值设置回false。然后重新获取坦克移动方向。  
**我的问题：坦克需要按一下右键之后一直向后移动，直到按下下一次键位再改变方向吗？还是按一下移动一下就ok？**  
**9..添加子弹**  
添加子弹类，属性：x，y，dir。  
方法：draw()，画出自己。  
move()，移动。  
**10.让坦克可以发射子弹**  
TankClient添加对ctrl键的监听，每当按下ctrl，调用坦克类的fire()方法。
给坦克类添加方法fire()，以坦克的位置计算后new一个子弹，方向为坦克移动方向。  
TankClient类中的属性m为子弹，Tank类中成员tc为客户端TankClient的一个引用，重载Tank构造函数，使得可以在new坦克时将本身的TankClient引用传入，然后在调用fire()时对子弹进行tc.m进行赋值，即tc.m = fire()。  
画出m即为发射的子弹。  
**10.1.让子弹从坦克中心发射出来而不是左上角**  
简单的数学问题  
**10.2.坦克停止移动时发射出的子弹也不动唉**  
添加炮筒方向，画一条直线代表炮筒方向。当坦克移动完后判定，如果坦克移动方向不等于STOP，则让炮筒方向等于移动方向。发射子弹时初始化子弹所用的方向由移动方向改成炮筒方向，即可以在停止移动时发射炮弹。  
**11.可以发射多颗子弹**  
将TankClient中的子弹m改为子弹列表List<Missile> missiles = new ArrayList<Missile>()。
fire()方法中每当发射一颗子弹，将new出来的子弹添加到子弹列表中。  
<pre> 
        public Missile fire() {
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2; 
		Missile m = new Missile(x, y, ptDir);
		tc.missiles.add(m);
		return m;
	}
</pre>
在TankClient的paint方法中，每次重画时遍历子弹列表全部重新画一遍。
<pre>
               for(int i = 0; i < missiles.size();i ++) {
			Missile m = missiles.get(i);
			m.draw(g);
		}</pre>    
在TankClient的paint()方法中，画出字符串“Missiles count:”，用来测试，并且获取子弹数目。    
**12.问题：子弹一直添加，即使子弹飞出屏幕仍然存在，内存使用过多。**  
**解决：添加子弹死亡**  
子弹类里添加Booleen值live，默认值为true。  
<pre>if(x < 0 || y < 0 || x > TankWarClient.GAME_WIDTH || y > TankWarClient.GAME_HEIGHT) {
			live = false;
</pre>  
即当子弹走出程序框时判定子弹死亡，然后在子弹的move()方法中，每移动一次判断一次子弹是否死亡，如果死亡，将此子弹从子弹列表中删除。  
实现方法：让子弹持有一个Tank War Client的引用，以方便操作子弹列表。  
**13.解决坦克出界**  
**解决：**在坦克的move()方法中添加判断，如果坦克的位置走出了程序框，则让坦克的位置参数等于边界值。
<pre>
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
</pre>  
**14.画出一辆敌人的坦克**  
给坦克类中添加私有成员变量Boolean good；当good为true，表示为自己的坦克，当值为false时，为敌方坦克。修改相应的构造方法，以及将坦克敌人坦克画出来。  
在坦克自身的draw()方法中，添加判断语句，将自己的坦克画成红色，敌人的坦克画成蓝色。  
**15.可以将敌人坦克击毙**  
**思路：一颗子弹击中坦克，子弹死亡，坦克死亡**    
为坦克和子弹都加入getRect()方法，获取其外面的矩形框，然后用Rectangle类的方法判断是否碰撞。  
击中后，子弹死亡，坦克死亡。  
<pre>getrect()方法：
        public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
</pre>

Missile中添加hitTank()方法，判断是否击中坦克（）。
<pre>
	public Boolean hitTank(Tank t) {
		if (this.getRect().intersects(t.getRect()) && t.isLive()) {
			t.setLive(false);
			this.live = false;
			return true;
		}
		return  false;
	}
</pre>    
即当坦克外边的矩形和子弹的矩形相交且坦克为活着的，则将坦克和子弹的live都设置为false，死去。  
坦克和子弹的draw()方法中开始添加判断，如果live值为false，则return，不再画出。  
**16.加入爆炸类**  
添加爆炸类，属性有位置，是否活着，tankwarclient的引用。  
方法：draw()画出自己。  
用一个个圆来模拟爆炸，所以会有一个圆的直径的数组。  
step：用来控制画到了哪一个圆。
<pre>
	int x,y;
	private boolean live = true;
	
	int [] diameter = {4,7,9,16,26,35,49,30,15,5};
	int step = 0;
	
	TankWarClient tc;
</pre>
<pre>draw()方法：
	public void draw(Graphics g) {
		if (!live) {
			return ;
		}
		
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
</pre>  
**17.子弹击中敌人坦克时爆炸**  
当子弹打击中敌人坦克时，产生一个爆炸，存放在ArrayList中，在Tank War Client的paint()中像子弹一样的挨个画出来。*注意当爆炸死亡时，将其从列表中删除，否则会出现占用内存过多的问题。*  
**18.添加多辆坦克**  
用容器来装坦克，在launchframe里new出多个坦克装进列表里。  
添加方法hitTanks()，每发子弹打一系列坦克，循环调用hitTank()，如果击中某一个坦克返回true，如果循环结束仍然没有击中任何一个坦克，返回false。  
<pre>
	public boolean hitTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			if (hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}
</pre>  
然后在TankWarClient中对每一发子弹调用hitTanks()，画出所有子弹，坦克，爆炸。ok。  
**问题：解决坦克不消亡问题**  
**解决：**在坦克的draw()方法中添加判断，如果坦克死了且是敌方坦克，从坦克列表中删除此坦克。  
**19.地方坦克运动及发射子弹**  
**19.1让敌军可以运动**  
坦克的构造方法中添加方向，修改对应调用位置。  
在坦克的move()方法中，每移动一次进行一次判定，如果是敌方坦克，将其移动方向设为随机方向，实现方法是随机数Random。  华友enum的values()方法的使用。
<pre>
		if (!good) {
			Direction [] dirs = Direction.values();
			int rn = r.nextInt(dirs.length);
			dir = dirs[rn];
		}
</pre>  
**19.2让地方坦克运动更加智能**  
设置一个随机数step，坦克随机一个方向后沿着此方向移动step步之后再次随机方向，再次随机移动距离。！！只有当step递减为0时，重新随机方向及随机步数。  
**19.3让地方坦克可以发射子弹**  
在移动后，即上一步的！good之后添加fire()，为了不让敌人炮火太猛添加限制。  
                    if (r.nextInt(40) > 36) {this.fire();}    
子弹添加好坏属性good，修改构造方法，在fire()方法中发射子弹时，发射与当前坦克同一阵营的子弹。  
client中添加打击自身坦克的m.hitTank(myTank)。即可解决自身无敌的问题。  
**20.添加墙壁**  
**20.1添加Wall类**  
初始化：具有属性位置，高度，宽度。具有方法：draw()，getRect()。  
**20.2子弹不能穿过墙壁**  
子弹内添加hitWall()方法，当子弹打击到墙壁时，子弹死亡。  
<pre>
	public boolean hitWall(Wall w) {
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true ;
		}
		return false;
	}
</pre>  
**20.3坦克不能穿过墙壁，碰到墙壁回到上一个位置重新随机方向及距离**    
添加新的成员变量oldx，oldy。来记录子弹上一步的位置，修改构造方法，在move()方法中记录，即每一次移动之前首先记录当前位置。stay()方法式坦克回到上一步骤的位置。
<pre>
	public boolean collidesWithWall (Wall w) {
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.stay();
			return true ;
		}
		return false;
	}
	private void stay() {
		x = oldx;
		y = oldy;
	}
</pre>
**21.使得坦克之间不能互相穿越**  
在坦克中添加方法collidesWithTanks()，检测坦克与列表中的每一辆坦克相撞，如果相撞，两辆坦克都stay().  
<pre>
	public boolean collidesWithTans(java.util.List<Tank> tanks) {
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
</pre>  
**22.发射超级炮弹**  
1.重载fire()方法，使得fire可以向传入的参数方向发射一颗炮弹。  
<pre>
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
</pre>
2.添加superfire()方法，遍历8个方向，各自调用一次fire(dir);向8个方向同时发射一颗炮弹。  
<pre>
	public void superFire() {
		Direction [] dirs = Direction.values();
		for (int i = 0; i < 8; i++) {
			fire(dirs[i]);
		}
	}
</pre>  
**23.添加主角生命值**  
tank加入变量life=100；  
在击中坦克时，如果是我方坦克，生命值减去20，然后判断生命值是否小于等于0，如果是，让它死去。
如果是地方坦克，直接死去。
<pre>
这一段是最新的hitTank方法中的判定，由于原方法太长不贴了。
	               if(t.isGood()) {
				t.setLife(t.getLife()-20);
				if (t.getLife() <= 0) { 
					t.setLive(false);
				}
			} else {
				t.setLive(false);
			
</pre>  
**24.图形化主站坦克生命值**  
坦克中添加内部类BloodBar，画在坦克上方，先画一个空心的长方形，然后根据当前life值画出实心的长方体。  
<pre>
	private class BloodBar{
		public  void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-5, WIDTH,5 );
			int w = WIDTH * life /100;
			g.fillRect(x, y-5, w, 5);
			g.setColor(c);
		}
	}
</pre> 
**25,增加血块，吃了可以回血**  
新增血块Blood类，在某几个点之间来回移动，当坦克碰到血块时，坦克血量回复满(即life= 100)，而血块死亡，死亡后不再画出血块。  
<pre>坦克中新添加的eat()方法。
	public boolean eat(Blood b) {
		if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			this.life = LIFE;
			b.setLive(false);
			return true ;
		}
		return false;
	}
</pre>  
**26.我的坦克死亡按F2重生，敌军全部死亡按F3重新出生5个**  
在keypress中 添加按键，当按下F2，如果我的坦克死亡，则让live=true，life=100；    
按下F3，如果敌方坦克全部死亡，则重新出生5个。  
<pre>
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
</pre>