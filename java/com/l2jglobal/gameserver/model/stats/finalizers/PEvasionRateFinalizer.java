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
package com.l2jglobal.gameserver.model.stats.finalizers;

import java.util.Optional;

import com.l2jglobal.Config;
import com.l2jglobal.gameserver.model.actor.L2Character;
import com.l2jglobal.gameserver.model.itemcontainer.Inventory;
import com.l2jglobal.gameserver.model.items.L2Item;
import com.l2jglobal.gameserver.model.items.instance.L2ItemInstance;
import com.l2jglobal.gameserver.model.stats.IStatsFunction;
import com.l2jglobal.gameserver.model.stats.Stats;

/**
 * @author UnAfraid
 */
public class PEvasionRateFinalizer implements IStatsFunction
{
	@Override
	public double calc(L2Character creature, Optional<Double> base, Stats stat)
	{
		throwIfPresent(base);
		
		double baseValue = calcWeaponPlusBaseValue(creature, stat);
		final Inventory inv = creature.getInventory();
		if (inv != null)
		{
			for (L2ItemInstance item : inv.getPaperdollItems(L2ItemInstance::isEquipped))
			{
				baseValue += item.getItem().getStats(stat, 0);
			}
		}
		
		final int level = creature.getLevel();
		if (creature.isPlayer())
		{
			// [Square(DEX)] * 5 + lvl;
			baseValue += (Math.sqrt(creature.getDEX()) * 5) + level;
			if (level > 69)
			{
				baseValue += level - 69;
			}
			if (level > 77)
			{
				baseValue += 1;
			}
			if (level > 80)
			{
				baseValue += 2;
			}
			if (level > 87)
			{
				baseValue += 2;
			}
			if (level > 92)
			{
				baseValue += 1;
			}
			if (level > 97)
			{
				baseValue += 1;
			}
			
			// Enchanted helm bonus
			baseValue += calcEnchantBodyPart(creature, L2Item.SLOT_HEAD);
		}
		else
		{
			// [Square(DEX)] * 5 + lvl;
			baseValue += (Math.sqrt(creature.getDEX()) * 5) + level;
			if (level > 69)
			{
				baseValue += (level - 69) + 2;
			}
		}
		
		return validateValue(creature, Stats.defaultValue(creature, stat, baseValue), Double.NEGATIVE_INFINITY, Config.MAX_EVASION);
	}
	
	@Override
	public double calcEnchantBodyPartBonus(int enchantLevel, boolean isBlessed)
	{
		if (isBlessed)
		{
			return (0.3 * Math.max(enchantLevel - 3, 0)) + (0.3 * Math.max(enchantLevel - 6, 0));
		}
		
		return (0.2 * Math.max(enchantLevel - 3, 0)) + (0.2 * Math.max(enchantLevel - 6, 0));
	}
}
