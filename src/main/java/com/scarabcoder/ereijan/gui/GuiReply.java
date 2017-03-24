package com.scarabcoder.ereijan.gui;

import java.io.IOException;

import com.scarabcoder.ereijan.Main;

import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.Message;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiReply extends GuiScreen {
	
	private Message msg;
	
	private String author;
	
	private String server;
	
	private String channel;
	
	private GuiTextField text;
	
	public GuiReply(Message msg){
		this.msg = msg;
		
		this.author = msg.getAuthor().getUsername(); 
		
		this.channel = Main.lastChannel.getName();
		
		this.server = Main.lastChannel.getGuild().getName();
		
		
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawDefaultBackground();
		
		this.drawCenteredString(fontRendererObj, "Reply on " + server + " (#" + channel + ")", this.width / 2, this.height / 2 -  30, 0xFFFFFF);
		
        this.text.drawTextBox();
		
        
	}
	
	public void initGui()
    {
        this.text = new GuiTextField(1, this.fontRendererObj, 50, this.height/2, this.width - 100, 20);
        text.setMaxStringLength(150);
        text.setText("");
        this.text.setFocused(true);
    }
	
	protected void keyTyped(char par1, int par2)
    {
        try {
			super.keyTyped(par1, par2);
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.text.textboxKeyTyped(par1, par2);
        if(par1 == '\r'){
        	if(this.text.getText() != ""){
        		this.msg.getChannel().sendMessage(this.text.getText());
        		mc.thePlayer.closeScreen();
        	}
        }
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

}
