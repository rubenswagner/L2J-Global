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
import com.l2jglobal.gameserver.enums.Race;
import com.l2jglobal.gameserver.model.actor.L2Npc;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * AI for Gust Spiral (23447)
 * @author Gigi, Global
 */
public final class GustSpiral extends AbstractNpcAI
{
	// NPC
	private static final int GUST_SPIRAL = 23447;
	
	private GustSpiral()
	{
		addAttackId(GUST_SPIRAL);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon)
	{
		if (attacker.getRace() == Race.ERTHEIA)
		{
			if (getRandom(100) < 30)
			{
				npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.MY_WIND_BARRIER_HOW_ONLY_THE_ERTHEIA_CAN_WAIT_UNLESS_YOU_ARE);
			}
			npc.setIsInvul(false);
		}
		else
		{
			npc.setIsInvul(true);
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	public static void main(String[] args)
	{
		new GustSpiral();
	}
}