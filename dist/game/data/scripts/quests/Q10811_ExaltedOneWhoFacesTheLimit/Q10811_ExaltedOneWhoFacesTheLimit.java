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
package quests.Q10811_ExaltedOneWhoFacesTheLimit;

import com.l2jglobal.Config;
import com.l2jglobal.gameserver.enums.Movie;
import com.l2jglobal.gameserver.model.actor.L2Npc;
import com.l2jglobal.gameserver.model.actor.instance.L2PcInstance;
import com.l2jglobal.gameserver.model.events.EventType;
import com.l2jglobal.gameserver.model.events.ListenerRegisterType;
import com.l2jglobal.gameserver.model.events.annotations.RegisterEvent;
import com.l2jglobal.gameserver.model.events.annotations.RegisterType;
import com.l2jglobal.gameserver.model.events.impl.character.player.OnPlayerAbilityPointsChanged;
import com.l2jglobal.gameserver.model.quest.Quest;
import com.l2jglobal.gameserver.model.quest.QuestState;
import com.l2jglobal.gameserver.model.quest.State;

/**
 * Exalted, One Who Faces the Limit (10811)
 * @author Gladicek
 */

public final class Q10811_ExaltedOneWhoFacesTheLimit extends Quest
{
	// Npc
	private static final int LIONEL = 33907;
	// Items
	private static final int LIONEL_HUNTER_MISSING_LIST = 45627;
	private static final int ELIKIA_CERTIFICATE = 45623;
	private static final int MYSTERIOUS_BUTLER_CERTIFICATE = 45624;
	private static final int SIR_ERIC_RODEMAI_CERTIFICATE = 45626;
	private static final int GALLADUCI_RODEMAI_CERTIFICATE = 45625;
	private static final int SPELLBOOK_DIGNITY_OF_THE_EXALTED = 45922;
	// Misc
	private static final int MIN_LEVEL = 99;
	
	public Q10811_ExaltedOneWhoFacesTheLimit()
	{
		super(10811);
		addStartNpc(LIONEL);
		addTalkId(LIONEL);
		registerQuestItems(LIONEL_HUNTER_MISSING_LIST, ELIKIA_CERTIFICATE, MYSTERIOUS_BUTLER_CERTIFICATE, SIR_ERIC_RODEMAI_CERTIFICATE, GALLADUCI_RODEMAI_CERTIFICATE);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState qs = getQuestState(player, false);
		
		if (qs == null)
		{
			return null;
		}
		
		String htmltext = null;
		
		switch (event)
		{
			case "33383-03.html":
			case "33383-04.html":
			{
				htmltext = event;
				break;
			}
			case "movieStart":
			{
				qs.startQuest();
				playMovie(player, Movie.SC_HONORS);
				break;
			}
			case "33383-05.html":
			{
				qs.setCond(2);
				giveItems(player, LIONEL_HUNTER_MISSING_LIST, 1);
				htmltext = event;
				break;
			}
			case "33383-09.html":
			{
				if (qs.isCond(3))
				{
					giveItems(player, SPELLBOOK_DIGNITY_OF_THE_EXALTED, 1);
					qs.exitQuest(false, true);
					htmltext = event;
				}
				break;
			}
			case "SUBQUEST_FINISHED_NOTIFY":
			{
				if (hasQuestItems(player, ELIKIA_CERTIFICATE, MYSTERIOUS_BUTLER_CERTIFICATE, SIR_ERIC_RODEMAI_CERTIFICATE, GALLADUCI_RODEMAI_CERTIFICATE) && (player.getAbilityPointsUsed() >= Config.ABILITY_MAX_POINTS))
				{
					qs.setCond(3, true);
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		
		switch (qs.getState())
		{
			case State.CREATED:
			{
				htmltext = (player.getLevel() >= MIN_LEVEL) && player.isNoble() ? "33383-01.htm" : "33383-07.htm";
				break;
			}
			case State.STARTED:
			{
				if (qs.isCond(1))
				{
					htmltext = "33383-02.html";
				}
				else if (qs.isCond(2))
				{
					htmltext = "33383-06.html";
				}
				else if (qs.isCond(3))
				{
					htmltext = "33383-08.html";
				}
				break;
			}
			case State.COMPLETED:
			{
				htmltext = getAlreadyCompletedMsg(player);
				break;
			}
		}
		return htmltext;
	}
	
	@RegisterEvent(EventType.ON_PLAYER_ABILITY_POINTS_CHANGED)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	private void OnPlayerAbilityPointsChanged(OnPlayerAbilityPointsChanged event)
	{
		notifyEvent("SUBQUEST_FINISHED_NOTIFY", null, event.getActiveChar());
	}
}