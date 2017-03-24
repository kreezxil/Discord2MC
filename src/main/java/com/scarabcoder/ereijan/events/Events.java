package com.scarabcoder.ereijan.events;

import com.scarabcoder.ereijan.Keybinds;
import com.scarabcoder.ereijan.Main;
import com.scarabcoder.ereijan.gui.GuiLogin;
import com.scarabcoder.ereijan.gui.GuiMain;
import com.scarabcoder.ereijan.gui.GuiReply;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class Events {
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	
	@SubscribeEvent
	public void loadWorld(PlayerLoggedInEvent e){
		
		
	}
	
	
	@SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
		if(Keybinds.openGui.isPressed()){
			if(Main.isAuth){
				mc.displayGuiScreen(new GuiMain());
			}else{
				mc.displayGuiScreen(new GuiLogin());
			}
		}else if(Keybinds.quickReply.isPressed()){
			if(Main.last != null){
				mc.displayGuiScreen(new GuiReply(Main.last));
			}
		}
        
        		
        	
    }
	
	@SubscribeEvent
	public void playerAttackEntity(AttackEntityEvent e){
		
	}
	
	
	
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent e){
	
	}
	
}
