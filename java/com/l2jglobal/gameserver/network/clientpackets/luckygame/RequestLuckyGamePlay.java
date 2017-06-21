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
package com.l2jglobal.gameserver.network.clientpackets.luckygame;

import com.l2jglobal.commons.network.PacketReader;
import com.l2jglobal.gameserver.network.client.L2GameClient;
import com.l2jglobal.gameserver.network.clientpackets.IClientIncomingPacket;
import com.l2jglobal.gameserver.network.serverpackets.luckygame.ExBettingLuckyGameResult;

/**
 * @author Global
 */
public class RequestLuckyGamePlay implements IClientIncomingPacket
{
	private int _type;
	private int _count;
	
	@Override
	public boolean read(L2GameClient client, PacketReader packet)
	{
		_type = packet.readD(); // luxury = 2, normal = 1
		_count = packet.readD(); // count
		return true;
	}
	
	@Override
	public void run(L2GameClient client)
	{
		client.getActiveChar().sendPacket(new ExBettingLuckyGameResult(client.getActiveChar(), _type, _count));
	}
}
