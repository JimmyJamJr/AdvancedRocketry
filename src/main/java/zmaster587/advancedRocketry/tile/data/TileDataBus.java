package zmaster587.advancedRocketry.tile.data;

import io.netty.buffer.ByteBuf;

import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import zmaster587.advancedRocketry.api.DataStorage;
import zmaster587.advancedRocketry.api.DataStorage.DataType;
import zmaster587.advancedRocketry.inventory.modules.IModularInventory;
import zmaster587.advancedRocketry.inventory.modules.ModuleBase;
import zmaster587.advancedRocketry.inventory.modules.ModuleData;
import zmaster587.advancedRocketry.item.ItemData;
import zmaster587.advancedRocketry.network.PacketHandler;
import zmaster587.advancedRocketry.network.PacketMachine;
import zmaster587.advancedRocketry.tile.multiblock.TileInventoryHatch;
import zmaster587.advancedRocketry.util.IDataInventory;
import zmaster587.libVulpes.util.INetworkMachine;

//TODO: allow dataCable connections
public class TileDataBus extends TileInventoryHatch implements IDataInventory, INetworkMachine {

	int maxData;
	DataStorage data;

	public TileDataBus() {
		data = new DataStorage(DataStorage.DataType.UNDEFINED);
		data.setMaxData(2000);
	}

	public TileDataBus(int number) {
		super(number);
		data = new DataStorage(DataStorage.DataType.UNDEFINED);

		data.setMaxData(2000);
	}

	@Override
	public void loadData() {
		
		ItemStack itemStack = inventory.getStackInSlot(0);
		
		if(itemStack != null && itemStack.getItem() instanceof ItemData) {
			ItemData itemData = (ItemData)itemStack.getItem();
			
			itemData.removeData(itemStack, 
					this.data.addData(itemData.getData(itemStack), itemData.getDataType(itemStack), true), DataStorage.DataType.UNDEFINED);

			//this.data.removeData(itemData.removeData(inv[0], this.data.getData(), this.data.getDataType()));
		}

		if(worldObj.isRemote) {
			PacketHandler.sendToServer(new PacketMachine(this, (byte)-2));
		}
	}

	@Override
	public String getModularInventoryName() {
		return "tile.hatch.2.name";
	}

	@Override
	public void storeData() {
		ItemStack itemStack = inventory.getStackInSlot(0);
		
		if(itemStack != null && itemStack.getItem() instanceof ItemData && itemStack.stackSize == 1) {
			ItemData itemData = (ItemData)itemStack.getItem();

			this.data.removeData(itemData.addData(itemStack, this.data.getData(), this.data.getDataType()), true);
		}

		if(worldObj.isRemote) {
			PacketHandler.sendToServer(new PacketMachine(this, (byte)-1));
		}
	}

	public void setData(int data, DataStorage.DataType dataType) {
		this.data.setData(data, dataType);
	}

	@Override
	public int addData(int data, DataStorage.DataType dataType, ForgeDirection dir, boolean commit) {
		return this.data.addData(data, dataType, commit);
	}

	public int getData() {
		return data.getData();
	}

	public final DataStorage getDataObject() {
		return data;
	}

	public int setMaxData() {
		return data.getMaxData();
	}

	public void setMaxData(int maxData) {
		data.setMaxData(maxData);
	}
	
	/**
	 * @param type the datatype to lock the tile to or null to unlock
	 * @see DataStorage
	 */
	public void lockData(DataStorage.DataType type) {
		data.lockDataType(type);
	}

	@Override
	public List<ModuleBase> getModules(int ID, EntityPlayer player) {
		LinkedList<ModuleBase> modules = new LinkedList<ModuleBase>();
		modules.add(new ModuleData(40, 20, 0, this, data));
		return modules;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		data.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		data.readFromNBT(nbt);
	}

	@Override
	public void writeDataToNetwork(ByteBuf out, byte id) {

	}

	@Override
	public void readDataFromNetwork(ByteBuf in, byte packetId,
			NBTTagCompound nbt) {

	}

	@Override
	public void useNetworkData(EntityPlayer player, Side side, byte id,
			NBTTagCompound nbt) {

		if(id == -1) {
			storeData();
		}
		else if(id == -2)
			loadData();
	}

	@Override
	public int extractData(int maxAmount, DataType type, ForgeDirection dir, boolean commit) {
		if(type == DataStorage.DataType.UNDEFINED || this.data.getDataType() == type)
			return this.data.removeData(maxAmount, commit);
		return 0;
	}
}
