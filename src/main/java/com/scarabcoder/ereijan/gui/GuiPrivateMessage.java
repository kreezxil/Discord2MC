package com.scarabcoder.ereijan.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.scarabcoder.ereijan.Main;

import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.PrivateChannel;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiPrivateMessage extends GuiScreen{
	
	
	List<PrivateChannel> pms;
	
	public GuiPrivateMessage(){
		this.pms = Main.jda.getPrivateChannels();
		
	}
	private GuiTextField text;
	int channelScroll = 0;
	public void initGui()
    {
        this.text = new GuiTextField(1,this.fontRendererObj, 5, 30, 100, 20);
        text.setMaxStringLength(100);
    }
	
	protected void keyTyped(char par1, int par2)
    {
        try {
			super.keyTyped(par1, par2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.text.textboxKeyTyped(par1, par2);
    }
    public void updateScreen()
    {
        super.updateScreen();
        this.text.updateCursorCounter();
    }
    protected void mouseClicked(int x, int y, int btn) {
        try {
			super.mouseClicked(x, y, btn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.text.mouseClicked(x, y, btn);
    }
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawDefaultBackground();
		this.text.drawTextBox();
		
		this.buttonList.clear();
		int scroll = 0;
		int x = 0;
		for(PrivateChannel channel : pms){
			if(channel.getUser().getUsername().toLowerCase().contains(text.getText().toLowerCase())){
				if(scroll >= channelScroll && (scroll < channelScroll + 9)){
					this.buttonList.add(new GuiButton(scroll, this.width / 2 - 75, 30 + (20 * x), 150, 20, channel.getUser().getUsername()));
					x = x + 1;
				}
				scroll = scroll + 1;
			}
		}
		this.buttonList.add(new GuiButton(pms.size() + 1, this.width / 2 - 75, 15, 150, 15, "↑"));
		this.buttonList.add(new GuiButton(pms.size() + 2, this.width / 2 - 75, 30 + (20 * 9), 150, 15, "↓"));
		this.buttonList.add(new GuiButton(200, 0, 0, 75, 20, "<----"));
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	

	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id <= pms.size()){
			PrivateChannel channel = this.pms.get(guibutton.id);
			mc.displayGuiScreen(new GuiSendMessage(channel));
			Main.messages = new MessageHistory(channel).retrieve();
			Main.isPrivateMessage = true;
		}else{
			switch(guibutton.id - pms.size()){
			case 1:
				if(this.channelScroll > 0){
					this.channelScroll -= 1;
				}
				break;
			case 2:
				this.channelScroll += 1;
				break;
			
			}
		}
		if(guibutton.id == 200){
			mc.displayGuiScreen(new GuiMain());
		}
	}
	
}
