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
package ai.areas.DragonValley;

import com.l2jglobal.gameserver.enums.ChatType;
import com.l2jglobal.gameserver.model.actor.L2Character;
import com.l2jglobal.gameserver.model.actor.L2Npc;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.network.NpcStringId;
import com.l2jglobal.gameserver.network.serverpackets.NpcSay;

import ai.AbstractNpcAI;

/**
 * Mercenary Captain AI.
 * @author Global
 */
public final class MercenaryCaptain extends AbstractNpcAI
{
	// NPC
	private static final int MERCENARY_CAPTAIN = 33970;
	
	private MercenaryCaptain()
	{
		addSeeCreatureId(MERCENARY_CAPTAIN);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equals("BROADCAST_TEXT") && (npc != null))
		{
			npc.broadcastPacket(new NpcSay(npc.getObjectId(), ChatType.NPC_GENERAL, npc.getId(), NpcStringId.THE_SOUTHERN_PART_OF_DRAGON_VALLEY_IS_MUCH_MORE_DANGEROUS_THAN_THE_NORTH_BE_CAREFUL));
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSeeCreature(L2Npc npc, L2Character creature, boolean isSummon)
	{
		if (creature.isPlayer())
		{
			startQuestTimer("BROADCAST_TEXT", 3000, npc, null, true);
		}
		return super.onSeeCreature(npc, creature, isSummon);
	}
	
	public static void main(String[] args)
	{
		new MercenaryCaptain();
	}
}