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
package quests.Q10332_ToughRoad;

import com.l2jglobal.gameserver.enums.Movie;
import com.l2jglobal.gameserver.enums.Race;
import com.l2jglobal.gameserver.model.actor.L2Character;
import com.l2jglobal.gameserver.model.actor.L2Npc;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.model.quest.Quest;
import com.l2jglobal.gameserver.model.quest.QuestState;
import com.l2jglobal.gameserver.model.quest.State;
import com.l2jglobal.gameserver.model.zone.L2ZoneType;
import com.l2jglobal.gameserver.network.NpcStringId;
import com.l2jglobal.gameserver.network.serverpackets.ExShowScreenMessage;

import quests.Q10331_StartOfFate.Q10331_StartOfFate;

/**
 * Tough Road (10332)
 * @author St3eT
 */
public final class Q10332_ToughRoad extends Quest
{
	// NPCs
	private static final int KAKAI = 30565;
	private static final int BATHIS = 30332;
	// Misc
	private static final int MIN_LEVEL = 20;
	private static final int MAX_LEVEL = 40;
	private static final int ZONE_ID = 12016;
	private static final String MOVIE_VAR = "Q10332_MOVIE";
	
	public Q10332_ToughRoad()
	{
		super(10332);
		addStartNpc(KAKAI);
		addTalkId(KAKAI, BATHIS);
		addEnterZoneId(ZONE_ID);
		addCondNotRace(Race.ERTHEIA, "30565-05.html");
		addCondLevel(MIN_LEVEL, MAX_LEVEL, "30565-04.html");
		addCondCompletedQuest(Q10331_StartOfFate.class.getSimpleName(), "30565-04.html");
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = getQuestState(player, false);
		
		if (st == null)
		{
			if (event.equals("SCREEN_MSG"))
			{
				showOnScreenMsg(player, NpcStringId.PA_AGRIO_LORD_KAKAI_IS_CALLING_FOR_YOU, ExShowScreenMessage.TOP_CENTER, 10000);
			}
			return null;
		}
		
		String htmltext = null;
		switch (event)
		{
			case "30332-02.html":
			{
				htmltext = event;
				break;
			}
			case "30565-02.html":
			{
				st.startQuest();
				htmltext = event;
				break;
			}
			case "30332-03.html":
			{
				if (st.isCond(1))
				{
					giveAdena(player, 700, true);
					addExpAndSp(player, 90000, 21);
					st.exitQuest(false, true);
					player.getVariables().remove(MOVIE_VAR);
					break;
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		
		if (npc.getId() == KAKAI)
		{
			switch (st.getState())
			{
				case State.CREATED:
				{
					htmltext = "30565-01.htm";
					break;
				}
				case State.STARTED:
				{
					htmltext = "30565-06.html";
					break;
				}
				case State.COMPLETED:
				{
					htmltext = "30565-03.html";
					break;
				}
			}
		}
		else if (npc.getId() == BATHIS)
		{
			if (st.getState() == State.STARTED)
			{
				htmltext = "30332-01.html";
			}
			else if (st.getState() == State.COMPLETED)
			{
				htmltext = "30332-04.html";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onEnterZone(L2Character character, L2ZoneType zone)
	{
		if (character.isPlayer())
		{
			final L2PcInstance player = character.getActingPlayer();
			final QuestState st = getQuestState(player, false);
			final QuestState st10331 = player.getQuestState(Q10331_StartOfFate.class.getSimpleName());
			
			if (((st == null) || st.isCreated()) && (player.getLevel() >= MIN_LEVEL) && (player.getLevel() <= MAX_LEVEL) && (st10331 != null) && st10331.isCompleted() && !player.getVariables().getBoolean(MOVIE_VAR, false))
			{
				player.getVariables().set(MOVIE_VAR, true);
				playMovie(player, Movie.SI_ILLUSION_04_QUE);
				startQuestTimer("SCREEN_MSG", 11000, null, player);
			}
		}
		return super.onEnterZone(character, zone);
	}
}