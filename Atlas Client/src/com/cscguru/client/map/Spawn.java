package com.cscguru.client.map;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import com.cscguru.client.entities.Actor;
import com.cscguru.client.entities.Facing;
import com.cscguru.client.entities.Mob;
import com.cscguru.client.entities.MobStat;
import com.cscguru.client.entities.Sprite;
import com.cscguru.client.interfaces.IUpdatable;
import com.cscguru.client.ui.Log;

/**Handles Spawn map objects.  Class handles the spawning and generation of mobs.
 * @author Bryan Bennett
 * @date Dec 26, 2013
 */
public class Spawn extends Rectangle implements IUpdatable{
	private static final long serialVersionUID = 1L;
	private int tileX;
	private int tileY;
	private Vector2f spawnPoint;
	private boolean hasMob;
	private Actor actor;
	private MobStat ms;
	private Mob mob;
	private int respawnTime;
	private int respawnMax = 10000;
	private AtlasMap tmx;
	private Log log;

	/**Initializes a spawner at a specific location and generates a mob.
	 * @param x
	 * @param y
	 * @param ss
	 * @param ms
	 * @param tmx
	 */
	public Spawn(float x, float y, Sprite ss, MobStat ms, AtlasMap tmx) {
		super(x, y, 16, 16);
		log = tmx.getLog();
		tileX = (int) (x/16);
		tileY = (int) (y/16);
		spawnPoint = new Vector2f (tileX, tileY);
		actor = new Actor(ss);
		this.ms = ms;
		this.tmx = tmx;
		createMob();
	}
	/**Returns the x-coordinate of the spawner in tiles.
	 * @return int
	 */
	public int getTileX() {
		return tileX;
	}
	/**Returns the y-coordinate of the spawner in tiles.
	 * @return int
	 */
	public int getTileY() {
		return tileY;
	}
	/**Returns the distance in tiles between the two vectors.
	 * @param tileX
	 * @param tileY
	 * @return double
	 */
	public double getDistanceInTiles(int tileX, int tileY){
		Vector2f v = new Vector2f(tileX, tileY);
		return v.distance(spawnPoint);
	}
	@Override
	public void update(int delta) {
		if (!hasMob && respawnTime >= respawnMax){
			respawnTime = 0;
			createMob();
		}
		else if (!hasMob){
			respawnTime += delta;
		}
		else if (hasMob){
			mob.update(delta);
			if (mob.isDead()){
				tmx.runLootTable(mob);
				mob = null;
				hasMob = false;
			}
			else if (mob.isDespawned()){
				mob = null;
				hasMob = false;
			}
		}
		
	}
	private void createMob(){
		hasMob = true;
		MobStat temp = new MobStat(this.ms);
		mob = new Mob(getX(), getY(), tileX, tileY, actor, temp, this);
	}
	/**Returns the mob associated with this spawner.
	 * @return Mob
	 */
	public Mob getMob(){
		return mob;
	}
	/**Returns whether or not the spawner currently has a mob.
	 * @return boolean
	 */
	public boolean hasMob(){
		return hasMob;
	}
	/**Returns whether or not the mob can move.  Provides access to the map for mob collision detection.
	 * @return boolean
	 */
	public boolean canMove(){
		return tmx.canMove(mob);
	}
	/**Checks the range of the mob from the spawner.  Mobs must stay within a certain distance
	 * of the spawner when not hooked onto a player.
	 * @param tileX
	 * @param tileY
	 * @param face
	 * @return boolean
	 */
	public boolean checkRange(int tileX, int tileY, Facing face){
		int k = 0;
		if (face == Facing.EAST || face == Facing.WEST){
			k = (int)Math.abs(this.tileX - tileX);
		}
		else{
			k = (int)Math.abs(this.tileY - tileY);
		}
		if (k <= 25){
			return true;
		}
		return false;
	}
	/**Returns an instance of the Log this spawner contains.
	 * @return Log
	 */
	public Log getLog(){
		return log;
	}
}
