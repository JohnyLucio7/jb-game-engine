package com.jb.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jb.main.Game;
import com.jb.world.Camera;
import com.jb.world.Direction;
import com.jb.world.World;

public class Player extends Entity {

	/** player attributes */

	private double life = 100;
	private int maxLife = 100;
	private int speed = 1;
	private Direction direction = Direction.RIGHT;

	/** mouse */
	private int mouseX;
	private int mouseY;

	/** animation variables */

	private int playerAnimFrames = 0;
	private int playerAnimeMaxFrames = 5;
	private int playerAnimSpriteIndex = 0;
	private int playerAnimeMaxSpriteIndex = 4;
	private int playerAnimIsDamagedFrames = 0;
	private int playerAnimMaxIsDamagedFrames = 8;

	/** control booleans */

	private boolean up;
	private boolean down;
	private boolean right;
	private boolean left;
	private boolean isMoved = false;
	private boolean isDamaged = false;
	private boolean isShoot = false;
	private boolean isMouseShoot = false;
	private boolean hasGun = false;
	private boolean enableRectCollisionMask = false;
	private boolean enableRectBorderCollisionMask = false;

	/** image vectors */

	private BufferedImage[] playerLeft;
	private BufferedImage[] playerRight;
	private BufferedImage[] playerLeftDamage;
	private BufferedImage[] playerRightDamage;
	private BufferedImage[] gunLeft;
	private BufferedImage[] gunRight;
	private BufferedImage[] gunLeftDamage;
	private BufferedImage[] gunRightDamage;

	/** weapons configuration */
	private Weapon weapon;
	private int ammo = 0;
	private int maxAmmo = 96;
	private int weaponXOffset = 0;
	private int weaponYOffset = 0;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		playerLeft = new BufferedImage[4];
		playerRight = new BufferedImage[4];
		playerLeftDamage = new BufferedImage[4];
		playerRightDamage = new BufferedImage[4];

		gunLeft = new BufferedImage[4];
		gunRight = new BufferedImage[4];
		gunLeftDamage = new BufferedImage[4];
		gunRightDamage = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			playerRight[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, width, height);
			playerLeft[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, width, height);
			playerRightDamage[i] = Game.spritesheet.getSprite(32 + (i * 16), 32, width, height);
			playerLeftDamage[i] = Game.spritesheet.getSprite(32 + (i * 16), 48, width, height);

			gunRight[i] = Game.spritesheet.getSprite(96 + (i * 16), 0, width, height);
			gunLeft[i] = Game.spritesheet.getSprite(96 + (i * 16), 16, width, height);
			gunRightDamage[i] = Game.spritesheet.getSprite(96 + (i * 16), 32, width, height);
			gunLeftDamage[i] = Game.spritesheet.getSprite(96 + (i * 16), 48, width, height);

		}
	}

	public void tick() {

		movement();

		animWalk();
		animDamage();

		checkCollisionWithLifepack();
		checkCollisionWithBullet();
		checkCollisionWithWeapon();

		shotWithKeyboard();
		shotWithMouse();

		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);

	}

	public void render(Graphics g) {

		if (!isDamaged) {
			if (direction == Direction.RIGHT) {
				g.drawImage(playerRight[playerAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					this.setWeaponXOffset(9);
					g.drawImage(gunRight[playerAnimSpriteIndex], this.getX() + getWeaponXOffset() - Camera.x,
							this.getY() + getWeaponYOffset() - Camera.y, null);
				}
			} else if (direction == Direction.LEFT) {
				g.drawImage(playerLeft[playerAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					this.setWeaponXOffset(-9);
					g.drawImage(gunLeft[playerAnimSpriteIndex], this.getX() + getWeaponXOffset() - Camera.x,
							this.getY() + getWeaponYOffset() - Camera.y, null);
				}
			}
		} else {
			if (direction == Direction.RIGHT) {
				g.drawImage(playerRightDamage[playerAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y,
						null);
				if (hasGun) {
					this.setWeaponXOffset(9);
					g.drawImage(gunRightDamage[playerAnimSpriteIndex], this.getX() + getWeaponXOffset() - Camera.x,
							this.getY() + getWeaponYOffset() - Camera.y, null);
				}
			} else if (direction == Direction.LEFT) {
				g.drawImage(playerLeftDamage[playerAnimSpriteIndex], this.getX() - Camera.x, this.getY() - Camera.y,
						null);
				if (hasGun) {
					this.setWeaponXOffset(-9);
					g.drawImage(gunLeftDamage[playerAnimSpriteIndex], this.getX() + getWeaponXOffset() - Camera.x,
							this.getY() + getWeaponYOffset() - Camera.y, null);
				}
			}
		}

		if (enableRectCollisionMask) {
			showRectCollisionMask(g);
		}

		if (enableRectBorderCollisionMask) {
			showRectBorderCollisionMask(g);
		}

		//showReloadFeedback(g);

	}

	private void movement() {
		isMoved = false;

		if (right && World.isFree(this.getX() + speed, this.getY())) {
			isMoved = true;
			direction = Direction.RIGHT;
			setX(getX() + speed);
		} else if (left && World.isFree(this.getX() - speed, this.getY())) {
			isMoved = true;
			direction = Direction.LEFT;
			setX(getX() - speed);
		}

		if (up && World.isFree(this.getX(), this.getY() - speed)) {
			isMoved = true;
			setY(getY() - speed);
		} else if (down && World.isFree(this.getX(), this.getY() + speed)) {
			isMoved = true;
			setY(getY() + speed);
		}
	}

	private void animWalk() {
		if (isMoved) {
			if (playerAnimFrames >= playerAnimeMaxFrames) {
				playerAnimSpriteIndex++;
				playerAnimSpriteIndex %= playerAnimeMaxSpriteIndex;
			}
			playerAnimFrames %= playerAnimeMaxFrames;
			playerAnimFrames++;
		} else {
			playerAnimSpriteIndex = 0;
		}
	}

	private void animDamage() {
		if (getIsDamaged()) {
			playerAnimIsDamagedFrames++;
			playerAnimIsDamagedFrames %= playerAnimMaxIsDamagedFrames;
			if (playerAnimIsDamagedFrames == 0) {
				setIsDamaged(false);
			}
		}
	}

	private void shotWithMouse() {
		if (isMouseShoot && hasGun && weapon.getAmmoInClip() > 0) {
			weapon.setAmmoInClip(weapon.getAmmoInClip() - 1);
			setIsMouseShoot(false);

			double angle = Math.atan2(this.getMouseY() - (this.getY() + 8 - Camera.y),
					this.getMouseX() - (this.getX() + 8 - Camera.x));

			System.out.println(Math.toDegrees(angle));

			direction = ((Math.toDegrees(angle) <= 90.0) && (Math.toDegrees(angle) >= -90.0)) ? Direction.RIGHT
					: Direction.LEFT;

			double dx = Math.cos(angle);
			double dy = Math.sin(angle);

			int offSetX = (direction == Direction.RIGHT) ? 22 : -10;
			int offSetY = 5;

			Bulletshoot bullet = new Bulletshoot(getX() + offSetX, getY() + offSetY, 3, 3, null, dx, dy);
			Game.bulletshoot.add(bullet);

		}
	}

	private void shotWithKeyboard() {
		if (isShoot && hasGun && weapon.getAmmoInClip() > 0) {

			weapon.setAmmoInClip(weapon.getAmmoInClip() - 1);
			setIsShoot(false);

			int dx = (direction == Direction.RIGHT) ? 1 : -1;
			int offSetX = (direction == Direction.RIGHT) ? 22 : -10;
			int offSetY = 5;

			Bulletshoot bullet = new Bulletshoot(getX() + offSetX, getY() + offSetY, 3, 3, null, dx, 0);
			Game.bulletshoot.add(bullet);
		}
	}

	private void showRectCollisionMask(Graphics g) {
		g.setColor(new Color(0, 0, 255, 100));
		g.fillRect(getX() - Camera.x, getY() - Camera.y, 16, 16);
	}

	private void showRectBorderCollisionMask(Graphics g) {
		g.setColor(new Color(255, 255, 0));

		int[] dx = new int[] { this.getX() + this.getMaskX() - Camera.x,
				this.getX() + this.getMaskX() + this.getMaskW() - Camera.x,
				this.getX() + this.getMaskX() + this.getMaskW() - Camera.x, this.getX() + this.getMaskX() - Camera.x };

		int[] dy = new int[] { this.getY() + this.getMaskY() - Camera.y, this.getY() + this.getMaskY() - Camera.y,
				this.getY() + this.getMaskY() + this.getMaskH() - Camera.y,
				this.getY() + this.getMaskY() + this.getMaskH() - Camera.y };
		g.drawPolygon(dx, dy, 4);
	}

	public void showReloadFeedback(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 7));
		g.drawString("R", getX() + 18 - Camera.x, getY() + 18 - Camera.y);
	}

	public void checkCollisionWithWeapon() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Weapon) {
				if (Entity.isCollinding(this, current)) {
					weapon = (Weapon) current;
					hasGun = true;
					Game.entities.remove(current);
				}
			}
		}
	}

	public void checkCollisionWithBullet() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Bullet) {
				if (Entity.isCollinding(this, current)) {
					this.setAmmo(getAmmo() + ((Bullet) current).getAmmo());
					Game.entities.remove(current);
				}
			}
		}
	}

	public void checkCollisionWithLifepack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Lifepack) {
				if (Entity.isCollinding(this, current)) {
					this.setLife(this.getLife() + ((Lifepack) current).getLifePoints());
					Game.entities.remove(current);
				}
			}
		}
	}

	/** getters and setters */

	public int getMouseX() {
		return mouseX;
	}

	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}

	public boolean getIsMouseShoot() {
		return this.isMouseShoot;
	}

	public void setIsMouseShoot(boolean b) {
		this.isMouseShoot = b;
	}

	public Weapon getWeapon() {
		return this.weapon;
	}

	public boolean getIsShoot() {
		return this.isShoot;
	}

	public void setIsShoot(boolean b) {
		this.isShoot = b;
	}

	public int getWeaponAmmoInClip() {
		if (weapon == null)
			return 0;
		else
			return this.weapon.getAmmoInClip();
	}

	public boolean getHasGun() {
		return this.hasGun;
	}

	public int getWeaponXOffset() {
		return this.weaponXOffset;
	}

	public void setWeaponXOffset(int offset) {
		this.weaponXOffset = offset;
	}

	public int getWeaponYOffset() {
		return this.weaponYOffset;
	}

	public void setWeaponYOffset(int offset) {
		this.weaponYOffset = offset;
	}

	public boolean getIsDamaged() {
		return this.isDamaged;
	}

	public void setIsDamaged(boolean b) {
		this.isDamaged = b;
	}

	public int getAmmo() {
		return this.ammo;
	}

	public void setAmmo(int a) {
		if (a < 0) {
			this.ammo = 0;
		} else if (a >= maxAmmo) {
			this.ammo = maxAmmo;
		} else {
			this.ammo = a;
		}
	}

	public int getMaxLife() {
		return this.maxLife;
	}

	public double getLife() {
		return this.life;
	}

	public void setLife(double nl) {

		if (nl <= 0)
			this.life = 0;
		else if (nl >= this.maxLife)
			this.life = maxLife;
		else
			this.life = nl;

	}

	public boolean getRight() {
		return this.right;
	}

	public void setRight(boolean b) {
		this.right = b;
	}

	public boolean getLeft() {
		return this.left;
	}

	public void setLeft(boolean b) {
		this.left = b;
	}

	public void setUp(boolean b) {
		this.up = b;
	}

	public void setDown(boolean b) {
		this.down = b;
	}

}
