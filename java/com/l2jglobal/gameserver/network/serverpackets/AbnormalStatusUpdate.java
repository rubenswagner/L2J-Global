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

import java.util.ArrayList;
import java.util.List;

import com.l2jglobal.commons.network.PacketWriter;
import com.l2jglobal.gameserver.model.skills.BuffInfo;
import com.l2jglobal.gameserver.model.skills.Skill;
import com.l2jglobal.gameserver.network.client.OutgoingPackets;

public class AbnormalStatusUpdate implements IClientOutgoingPacket
{
	private final List<BuffInfo> _effects = new ArrayList<>();
	private final List<Skill> _effects2 = new ArrayList<>();
	
	public void addSkill(BuffInfo info)
	{
		if (!info.getSkill().isHealingPotionSkill())
		{
			_effects.add(info);
		}
	}
	
	public void addSkill(Skill skill)
	{
		if (!skill.isHealingPotionSkill())
		{
			_effects2.add(skill);
		}
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.ABNORMAL_STATUS_UPDATE.writeId(packet);
		
		packet.writeH(_effects.size() + _effects2.size());
		for (BuffInfo info : _effects)
		{
			if ((info != null) && info.isInUse())
			{
				packet.writeD(info.getSkill().getDisplayId());
				packet.writeH(info.getSkill().getDisplayLevel());
				packet.writeH(0x00); // Sub level
				packet.writeD(info.getSkill().getAbnormalType().getClientId());
				writeOptionalD(packet, info.getSkill().isAura() ? -1 : info.getTime());
			}
		}
		for (Skill skill : _effects2)
		{
			if (skill != null)
			{
				packet.writeD(skill.getDisplayId());
				packet.writeH(skill.getDisplayLevel());
				packet.writeH(0x00); // Sub level
				packet.writeD(skill.getAbnormalType().getClientId());
				packet.writeH(-1);
			}
		}
		return true;
	}
}
