package me.windyteam.kura.event.events.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EventRenderTooltip extends Event
{
    private ItemStack Item;
    private int X;
    private int Y;

    public EventRenderTooltip(ItemStack p_Stack, int p_X, int p_Y)
    {
        Item = p_Stack;
        X = p_X;
        Y = p_Y;
    }

    public ItemStack getItemStack()
    {
        return Item;
    }

    public int getX()
    {
        return X;
    }

    public int getY()
    {
        return Y;
    }

}
