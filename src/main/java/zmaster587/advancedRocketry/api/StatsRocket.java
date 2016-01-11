package zmaster587.advancedRocketry.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zmaster587.advancedRocketry.api.fuel.FuelRegistry;
import zmaster587.advancedRocketry.api.fuel.FuelRegistry.FuelType;
import zmaster587.libVulpes.util.BlockPosition;
import zmaster587.libVulpes.util.Vector3F;
import net.minecraft.nbt.NBTTagCompound;

public class StatsRocket {

	private int thrust;
	private int weight;
	
	private int fuelLiquid;
	private int fuelNuclear;
	private int fuelIon;
	private int fuelWarp;
	private int fuelImpulse;
	
	private int fuelCapacityLiquid;
	private int fuelCapacityNuclear;
	private int fuelCapacityIon;
	private int fuelCapacityWarp;
	private int fuelCapacityImpulse;
	
	private int fuelRateLiquid;
	private int fuelRateNuclear;
	private int fuelRateIon;
	private int fuelRateWarp;
	private int fuelRateImpulse;
	
	BlockPosition pilotSeatPos;
	private final List<BlockPosition> passengerSeats = new ArrayList<BlockPosition>();
	private List<Vector3F<Float>> engineLoc;

	private static final String TAGNAME = "rocketStats";

	public StatsRocket() {
		thrust = 0;
		weight = 0;
		fuelLiquid = 0;
		pilotSeatPos = new BlockPosition(0,0,0);
		pilotSeatPos.x = -1;
		engineLoc = new ArrayList<Vector3F<Float>>();
	}

	/*public StatsRocket(int thrust, int weight, int fuelRate, int fuel) {
		this.thrust = thrust;
		this.weight = weight;
		this.fuelLiquid = fuel;
		lastSeatX = -1;
		engineLoc = new ArrayList<Vector3F>();
	}*/



	public int getSeatX() { return pilotSeatPos.x; }
	public int getSeatY() { return pilotSeatPos.y; }
	public int getSeatZ() { return pilotSeatPos.z; }
	
	public BlockPosition getPassengerSeat(int index) {
		return passengerSeats.get(index);
	}
	
	public int getNumPassengerSeats() {
		return passengerSeats.size();
	}
	
	public int getThrust() {return thrust;}
	public int getWeight() {return weight;}
	public float getAcceleration() { return (thrust - weight)/10000f; }
	public List<Vector3F<Float>> getEngineLocations() { return engineLoc; }

	public void setThrust(int thrust) { this.thrust = thrust; }
	public void setWeight(int weight) { this.weight = weight; }
	
	public void setSeatLocation(int x, int y, int z) {
		pilotSeatPos.x = x;
		pilotSeatPos.y = y;
		pilotSeatPos.z = z;
	}

	public void addPassengerSeat(int x, int y, int z) {
		passengerSeats.add(new BlockPosition(x, y, z));
	}
	
	/**
	 * Adds an engine location to the given coordinates
	 * the engine location is only currently used to track the location for spawning particle effects
	 * @param x
	 * @param y
	 * @param z
	 */
	public void addEngineLocation(float x, float y, float z) {
		//We want to be in the center of the block
		engineLoc.add(new Vector3F<Float>(x , y, z));
	}

	/**
	 * Removes all engine locations
	 */
	public void clearEngineLocations() {
		engineLoc.clear();
	}

	/**
	 * @return a duplicate of the rocket stats
	 */
	public StatsRocket copy() {
		StatsRocket stat = new StatsRocket();
		
		stat.thrust = this.thrust;
		stat.weight = this.weight;
		
		for(FuelType type : FuelType.values()) {
			stat.setFuelAmount(type, this.getFuelAmount(type));
			stat.setFuelRate(type, this.getFuelRate(type));
			stat.setFuelCapacity(type, this.getFuelCapacity(type));
		}
		
		stat.pilotSeatPos = new BlockPosition(this.pilotSeatPos.x, this.pilotSeatPos.y, this.pilotSeatPos.z);
		stat.engineLoc = new ArrayList<Vector3F<Float>>(engineLoc);
		return stat;
	}

	/**
	 * 
	 * @param type type of fuel to check
	 * @return the amount of fuel of the type currently contained in the stat
	 */
	public int getFuelAmount(FuelRegistry.FuelType type) {
		switch(type) {
		case WARP:
			return fuelWarp;
		case IMPULSE:
			return fuelImpulse;
		case ION:
			return fuelIon;
		case LIQUID:
			return fuelLiquid;
		case NUCLEAR:
			return fuelNuclear;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param type
	 * @return the largest amount of fuel of the type that can be stored in the stat
	 */
	public int getFuelCapacity(FuelRegistry.FuelType type) {
		switch(type) {
		case WARP:
			return fuelCapacityWarp;
		case IMPULSE:
			return fuelCapacityImpulse;
		case ION:
			return fuelCapacityIon;
		case LIQUID:
			return fuelCapacityLiquid;
		case NUCLEAR:
			return fuelCapacityNuclear;
		}
		return 0;
	}
	
	/**
	 * @param type
	 * @return the consumption rate of the fuel per tick
	 */
	public int getFuelRate(FuelRegistry.FuelType type) {
		
		if(!Configuration.rocketRequireFuel)
			return 0;
		
		switch(type) {
		case WARP:
			return fuelRateWarp;
		case IMPULSE:
			return fuelRateImpulse;
		case ION:
			return fuelRateIon;
		case LIQUID:
			return fuelRateLiquid;
		case NUCLEAR:
			return fuelRateNuclear;
		}
		return 0;
	}
	
	/**
	 * Sets the amount of a given fuel type in the stat
	 * @param type
	 * @param amt
	 */
	public void setFuelAmount(FuelRegistry.FuelType type, int amt) {
		switch(type) {
		case WARP:
			fuelWarp = amt;
			break;
		case IMPULSE:
			fuelImpulse = amt;
			break;
		case ION:
			fuelIon = amt;
			break;
		case LIQUID:
			fuelLiquid = amt;
			break;
		case NUCLEAR:
			fuelNuclear = amt;
		}
	}
	
	/**
	 * Sets the fuel consumption rate per tick of the stat
	 * @param type
	 * @param amt
	 */
	public void setFuelRate(FuelRegistry.FuelType type, int amt) {
		switch(type) {
		case WARP:
			fuelRateWarp = amt;
			break;
		case IMPULSE:
			fuelRateImpulse = amt;
			break;
		case ION:
			fuelRateIon = amt;
			break;
		case LIQUID:
			fuelRateLiquid = amt;
			break;
		case NUCLEAR:
			fuelRateNuclear = amt;
		}
	}
	
	/**
	 * Sets the fuel capacity of the fuel type in this stat
	 * @param type
	 * @param amt
	 */
	public void setFuelCapacity(FuelRegistry.FuelType type, int amt) {
		switch(type) {
		case WARP:
			fuelCapacityWarp = amt;
			break;
		case IMPULSE:
			fuelCapacityImpulse = amt;
			break;
		case ION:
			fuelCapacityIon = amt;
			break;
		case LIQUID:
			fuelCapacityLiquid = amt;
			break;
		case NUCLEAR:
			fuelCapacityNuclear = amt;
		}
	}
	
	/**
	 * 
	 * @param type type of fuel
	 * @param amt amount of fuel to add
	 * @return amount of fuel added
	 */
	public int addFuelAmount(FuelRegistry.FuelType type, int amt) {
		//TODO: finish other ones
		switch(type) {
		case WARP:
			fuelWarp += amt;
			return fuelWarp;
		case IMPULSE:
			fuelImpulse += amt;
			return fuelImpulse;
		case ION:
			fuelIon += amt;
			return fuelIon;
		case LIQUID:
			int maxAdd = fuelCapacityLiquid - fuelLiquid;
			int amountToAdd = Math.min(amt, maxAdd);
			fuelLiquid += amountToAdd;
			return amountToAdd;
		case NUCLEAR:
			fuelNuclear += amt;
			return fuelNuclear;
		}
		return 0;
	}
	
	/**
	 * @return true if a seat exists on this stat
	 */
	public boolean hasSeat() {
		return pilotSeatPos.x != -1;
	}

	/**
	 * resets all values to default
	 */
	public void reset() {
		thrust = 0;
		weight = 0;
		
		for(FuelType type : FuelType.values()) {
			setFuelAmount(type, 0);
			setFuelRate(type, 0);
			setFuelCapacity(type, 0);
		}
			
		fuelLiquid = 0;
		pilotSeatPos.x = -1;
		clearEngineLocations();
		passengerSeats.clear();
	}

	public static StatsRocket createFromNBT(NBTTagCompound nbt) { 
		if(nbt.hasKey(TAGNAME)) {
			NBTTagCompound stats = nbt.getCompoundTag(TAGNAME);
			StatsRocket statsRocket = new StatsRocket();
			statsRocket.readFromNBT(stats);
			return statsRocket;
		}

		return new StatsRocket();
	}

	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound stats = new NBTTagCompound();

		stats.setInteger("thrust", this.thrust);
		stats.setInteger("weight", this.weight);
		
		stats.setInteger("fuelLiquid", this.fuelLiquid);
		stats.setInteger("fuelImpulse", this.fuelImpulse);
		stats.setInteger("fuelIon", this.fuelIon);
		stats.setInteger("fuelNuclear", this.fuelNuclear);
		stats.setInteger("fuelWarp", this.fuelWarp);
		
		stats.setInteger("fuelCapacityLiquid", this.fuelCapacityLiquid);
		stats.setInteger("fuelCapacityImpulse", this.fuelCapacityImpulse);
		stats.setInteger("fuelCapacityIon", this.fuelCapacityIon);
		stats.setInteger("fuelCapacityNuclear", this.fuelCapacityNuclear);
		stats.setInteger("fuelCapacityWarp", this.fuelCapacityWarp);
		
		stats.setInteger("fuelRateLiquid", this.fuelRateLiquid);
		stats.setInteger("fuelRateImpulse", this.fuelRateImpulse);
		stats.setInteger("fuelRateIon", this.fuelRateIon);
		stats.setInteger("fuelRateNuclear", this.fuelRateNuclear);
		stats.setInteger("fuelRateWarp", this.fuelRateWarp);
		
		stats.setInteger("playerXPos", pilotSeatPos.x);
		stats.setInteger("playerYPos", pilotSeatPos.y);
		stats.setInteger("playerZPos", pilotSeatPos.z);

		if(!engineLoc.isEmpty()) {
			int locs[] = new int[engineLoc.size()*3];
			
			for(int i=0 ; (i/3) < engineLoc.size(); i+=3) {
				Vector3F<Float> vec = engineLoc.get(i/3);
				locs[i] = vec.x.intValue();
				locs[i + 1] = vec.y.intValue();
				locs[i + 2] = vec.z.intValue();
			}
			stats.setIntArray("engineLoc", locs);
		}

		nbt.setTag(TAGNAME, stats);
	}

	public void readFromNBT(NBTTagCompound nbt) {

		if(nbt.hasKey(TAGNAME)) {
			NBTTagCompound stats = nbt.getCompoundTag(TAGNAME);
			this.thrust = stats.getInteger("thrust");
			this.weight = stats.getInteger("weight");
			
			this.fuelLiquid = stats.getInteger("fuelLiquid");
			this.fuelImpulse = stats.getInteger("fuelImpulse");
			this.fuelIon = stats.getInteger("fuelIon");
			this.fuelNuclear = stats.getInteger("fuelNuclear");
			this.fuelWarp = stats.getInteger("fuelWarp");
			
			this.fuelCapacityLiquid = stats.getInteger("fuelCapacityLiquid");
			this.fuelCapacityImpulse = stats.getInteger("fuelCapacityImpulse");
			this.fuelCapacityIon = stats.getInteger("fuelCapacityIon");
			this.fuelCapacityNuclear = stats.getInteger("fuelCapacityNuclear");
			this.fuelCapacityWarp = stats.getInteger("fuelCapacityWarp");
			
			this.fuelRateLiquid = stats.getInteger("fuelRateLiquid");
			this.fuelRateImpulse = stats.getInteger("fuelRateImpulse");
			this.fuelRateIon = stats.getInteger("fuelRateIon");
			this.fuelRateNuclear = stats.getInteger("fuelRateNuclear");
			this.fuelRateWarp = stats.getInteger("fuelRateWarp");

			pilotSeatPos.x = stats.getInteger("playerXPos");
			pilotSeatPos.y = stats.getInteger("playerYPos");
			pilotSeatPos.z = stats.getInteger("playerZPos");
			
			if(stats.hasKey("engineLoc")) {
				int locations[] = stats.getIntArray("engineLoc");
				
				for(int i=0 ; i < locations.length; i+=3) {
					
					this.addEngineLocation(locations[i], locations[i+1], locations[i+2]);
				}
			}
		}
	}
}
