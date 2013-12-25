package com.cscguru.client.items;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.cscguru.client.enums.ItemType;
import com.cscguru.client.enums.Quality;
import com.cscguru.client.interfaces.IDrawable;
import com.cscguru.client.interfaces.ITipped;
import com.cscguru.client.interfaces.IUpdatable;
import com.cscguru.client.ui.Inventory;
import com.cscguru.client.ui.Settings;

/**Base class for all items.
 * @author Bryan Bennett
 * @date Dec 1, 2013
 */
public abstract class Item extends Rectangle implements IDrawable,ITipped, IUpdatable{
	private static final long serialVersionUID = 1L;
	protected int lvlReq;
	protected int value;
	protected String desc;
	protected int armor;
	protected int max;
	protected int min;
	protected int mod;
	
	private Image img;
	private ItemType type;
	private Quality quality;
	private String name;
	private boolean hasToolTip = true;
	private int tileX;
	private int tileY;
	private boolean isOnGround = false;
	private long despawnTime = 0;
	private long despawnMax = 300000;
	private boolean isStale = false;
	protected boolean hasDropped = false;
	/**Constructs an item at a specific location with the correct item information.
	 * @param info
	 * @param v
	 */
	public Item(ItemInfo info, Vector2f v, int tileX, int tileY) {
		super(v.x,v.y,32,32);
		lvlReq = info.getLvlReq();
		value = info.getValue();
		desc = info.getDesc();
		armor = info.getMod();
		min = info.getMod();
		max = info.getMax();
		mod = info.getMod();
		img = info.getImage();
		name = info.getName();
		type = info.getType();
		quality = info.getQuality();
		this.tileX = tileX;
		this.tileY = tileY;
		isOnGround = true;
	}
	@Override
	public void draw(Graphics g) {
		img.draw(x,y);
	}
	public void draw(int offsetX, int offsetY){
		float x1 = 0;
		float y1 = 0;
		int totalX = tileX - offsetX;
		int totalY = tileY - offsetY;
		if (totalX <= 0 || totalY <= 0 || totalX >= 41 || totalY >= 41){
			return ;
		}
		x1 = Settings.CAM_X + (totalX * 16);
		y1 = Settings.CAM_Y + (totalY * 16);
		setX(x1);
		setY(y1);
		img.draw(x1,y1);
	}
	/**Returns the ItemType of the item.
	 * @return ItemType
	 */
	public ItemType getType(){
		return type;
	}
	/**Returns true if the item has a tool tip or not.  Expected true for all items.
	 * @return boolean
	 */
	public boolean hasToolTip(){
		return hasToolTip;
	}
	/**Returns the image of the item.
	 * @return Image
	 */
	public Image getImage(){
		return img;
	}
	/**Returns the name of the item.
	 * @return String
	 */
	public String getName(){
		return name;
	}
	/**Returns the rarity of the object (Quality.enum)
	 * @return Quality
	 */
	public Quality getQuality(){
		return quality;
	}
	/**Returns the damage of the item.
	 * @return int
	 */
	public int getMaxDamage(){
		return max;
	}
	public int getMinDamage(){
		return min;
	}
	/**Returns the armor of the item.
	 * @return int
	 */
	public int getArmor(){
		return armor;
	}
	/**Returns the mod value;
	 * @return int
	 */
	public int getMod(){
		return mod;
	}
	/**Gets the level requirement of the item.
	 * @return int
	 */
	public int getLvlReq(){
		return lvlReq;
	}
	/**Overridable method to handle right-clicks on items in the inventory.
	 * @param parent
	 */
	public abstract void use(Inventory parent);
	public int getTileX() {
		return tileX;
	}
	public void setTileX(int tileX) {
		this.tileX = tileX;
	}
	public int getTileY() {
		return tileY;
	}
	public void setTileY(int tileY) {
		this.tileY = tileY;
	}
	@Override
	public void update(int delta){
		if (isOnGround){
			despawnTime += delta;
		}
		if (despawnTime >= despawnMax){
			isStale = true;
		}
	}
	public boolean isOnGround() {
		return isOnGround;
	}
	public void setOnGround(boolean isOnGround) {
		this.isOnGround = isOnGround;
	}
	public boolean isStale(){
		return isStale;
	}
	public boolean hasDropped(){
		return hasDropped;
	}
	public void setDropped(boolean val){
		hasDropped = val;
	}
}
