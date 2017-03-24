package com.scarabcoder.ereijan.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.scarabcoder.ereijan.Main;
import com.scarabcoder.ereijan.ScarabUtil.ScarabUtil;

import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiSendMessage extends GuiScreen{

	private MessageChannel channel;

	private Channel channel2;

	private boolean isMuted = false;

	private boolean isPrivate = false;

	private GuiTextField text;

	public GuiSendMessage(MessageChannel channel){
		this.channel = channel;
	}



	public GuiSendMessage(PrivateChannel channel){
		this.isPrivate = true;

		this.channel = channel;
	}

	public GuiSendMessage(Channel channel, Boolean check){
		this.channel2 = channel;
		List<TextChannel> channels = channel.getGuild().getTextChannels();
		for(TextChannel chan : channels){
			if(chan.getId().equals(channel.getId())){
				this.channel = (MessageChannel) chan;
			}
		}
		this.isMuted = ScarabUtil.isMuted(channel);
	}

	public void initGui()
	{
		this.text = new GuiTextField(1, this.fontRendererObj, 10, this.height - 30, this.width - 50, 20);
		this.text.setFocused(true);
		this.text.setMaxStringLength(500);
		int x = 0;

	}

	protected void keyTyped(char par1, int par2) throws IOException
	{
		super.keyTyped(par1, par2);
		this.text.textboxKeyTyped(par1, par2);
		if(par1 == '\r'){
			if(text.getText() != ""){
				channel.sendMessage(text.getText());
				text.setText("");
			}
		}
	}

	public void updateScreen()
	{
		super.updateScreen();
		this.text.updateCursorCounter();
	}
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		this.drawDefaultBackground();
		this.text.drawTextBox();
		int x = 0;
		if(Main.messages != null){
			for(Message msg : Main.messages){
				//System.out.println(msg.getAuthor().getUsername() + ": " + msg.getContent());
				if(x < 12){
					String text = "";
					String author = "[Invalid User]: ";
					if(msg.getAuthor() != null){

						author = msg.getAuthor().getUsername() + ": ";
						

					}
					
					
					int color = 0xFFFFFF;
					if(this.channel2 != null){
						List<Role> roles = new ArrayList<Role>(channel2.getGuild().getRolesForUser(msg.getAuthor()));
						if(roles.size() > 0){
							Collections.sort(roles, new Comparator<Role>(){

								@Override
								public int compare(Role arg0, Role arg1) {
									if(arg0.getPosition() > arg1.getPosition()){
										return -1;
									}else{
										return 1;
									}
								}

							});
							color = roles.get(0).getColor();
						}
						if(channel2.getGuild().getNicknameForUser(msg.getAuthor()) != null)
						author = channel2.getGuild().getNicknameForUser(msg.getAuthor()) + ": ";
					}
					text = author + ChatFormatting.WHITE + msg.getContent();
					text = shrinkStringToFit(text);
					if(isToLong(msg.getContent())){
						text = text + "...";
					}
					fontRendererObj.drawString(text,8,this.height - 55 - (x * 15), color);

				}
				x = x + 1;
			}
		}
		this.buttonList.clear();

		this.buttonList.add(new GuiButton(1,this.width - 30, this.height - 30, 20, 20, ">"));

		this.buttonList.add(new GuiButton(2, 0, 0, 75, 20, "<----"));

		if(!this.isPrivate && (this.channel2 != null)){
			String text = "";

			if(this.isMuted){
				text = "Unmute";
			}else{
				text = "Mute";
			}


			this.buttonList.add(new GuiButton(3, this.width - 75, 0, 75, 20, text));

		}

		super.drawScreen(par1, par2, par3);
	}

	private String shrinkStringToFit(String str){
		String ret = str;
		for(int x = str.length(); x != 0; x -= 1){
			if((fontRendererObj.getStringWidth(str.substring(0,x))) < this.width - 60){
				return str.substring(0,x);


			}
		}
		return null;

	}

	private boolean isToLong(String str){
		if((fontRendererObj.getStringWidth(str)) < this.width - 60){
			return false;
		}else{
			return true;
		}
	}

	protected void mouseClicked(int x, int y, int btn) throws IOException {
		super.mouseClicked(x, y, btn);
		this.text.mouseClicked(x, y, btn);
	}


	protected void actionPerformed(GuiButton guibutton) {
		switch(guibutton.id){
		case 1:
			if(text.getText() != ""){
				channel.sendMessage(text.getText());
				text.setText("");
				text.setFocused(true);
			}
			break;
		case 2:
			mc.displayGuiScreen(new GuiMain());
			break;
		case 3:
			this.isMuted = !this.isMuted;
			ScarabUtil.setMuted(this.channel2, !this.isMuted);
			break;

		}
	}
}
