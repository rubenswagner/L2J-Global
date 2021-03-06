/*
 * This file is part of the L2J Global project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jglobal.gameserver.network.serverpackets;

import java.util.List;

import com.l2jglobal.commons.network.PacketWriter;
import com.l2jglobal.gameserver.instancemanager.FortSiegeManager;
import com.l2jglobal.gameserver.model.FortSiegeSpawn;
import com.l2jglobal.gameserver.model.L2Spawn;
import com.l2jglobal.gameserver.model.entity.Fort;
import com.l2jglobal.gameserver.network.client.OutgoingPackets;

/**
 * TODO: Rewrite!!!!!!
 * @author KenM
 */
public class ExShowFortressMapInfo implements IClientOutgoingPacket
{
	private final Fort _fortress;
	
	public ExShowFortressMapInfo(Fort fortress)
	{
		_fortress = fortress;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SHOW_FORTRESS_MAP_INFO.writeId(packet);
		
		packet.writeD(_fortress.getResidenceId());
		packet.writeD(_fortress.getSiege().isInProgress() ? 1 : 0); // fortress siege status
		packet.writeD(_fortress.getFortSize()); // barracks count
		
		final List<FortSiegeSpawn> commanders = FortSiegeManager.getInstance().getCommanderSpawnList(_fortress.getResidenceId());
		if ((commanders != null) && (commanders.size() != 0) && _fortress.getSiege().isInProgress())
		{
			switch (commanders.size())
			{
				case 3:
				{
					for (FortSiegeSpawn spawn : commanders)
					{
						if (isSpawned(spawn.getId()))
						{
							packet.writeD(0);
						}
						else
						{
							packet.writeD(1);
						}
					}
					break;
				}
				case 4: // TODO: change 4 to 5 once control room supported
				{
					int count = 0;
					for (FortSiegeSpawn spawn : commanders)
					{
						count++;
						if (count == 4)
						{
							packet.writeD(1); // TODO: control room emulated
						}
						if (isSpawned(spawn.getId()))
						{
							packet.writeD(0);
						}
						else
						{
							packet.writeD(1);
						}
					}
					break;
				}
			}
		}
		else
		{
			for (int i = 0; i < _fortress.getFortSize(); i++)
			{
				packet.writeD(0);
			}
		}
		return true;
	}
	
	/**
	 * @param npcId
	 * @return
	 */
	private boolean isSpawned(int npcId)
	{
		boolean ret = false;
		for (L2Spawn spawn : _fortress.getSiege().getCommanders())
		{
			if (spawn.getId() == npcId)
			{
				ret = true;
				break;
			}
		}
		return ret;
	}
}
