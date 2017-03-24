package com.scarabcoder.ereijan.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.scarabcoder.ereijan.Main;
import com.scarabcoder.ereijan.ScarabUtil.ScarabUtil;
import com.scarabcoder.ereijan.ScarabUtil.Setting;

import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.utils.PermissionUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiMain extends GuiScreen{


	TextChannel channel;
	
	private boolean discordPrefix;
	
	private boolean serverPrefix;
	
	private boolean channelPrefix;

	Guild guild;

	List<Guild> guilds;

	public GuiMain(){
		this.guilds = Main.jda.getGuilds();
		this.username = Main.jda.getSelfInfo().getUsername();
		List<String> options = new ArrayList<String>(Arrays.asList(ScarabUtil.readFromFile("settings").split(":")));
		this.discordPrefix = Boolean.valueOf(options.get(0));
		this.serverPrefix = Boolean.valueOf(options.get(1));
		this.channelPrefix = Boolean.valueOf(options.get(2));
	}

	int channelScroll = 0;

	int guildScroll = 0;

	List<TextChannel> channels;

	String username;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawDefaultBackground();
		this.buttonList.clear();
		int x = 0;
		int scroll = 0;

		for(Guild channel : guilds){
			if(guildScroll <= scroll && (scroll < guildScroll + 5)){
				this.buttonList.add(new GuiButton(scroll, 10, 15 + 20 * x, 100, 20,channel.getName()));
				x = x + 1;
			}

			scroll += 1;
		}
		this.buttonList.add(new GuiButton(122, 10, 5, 100, 10, "↑"));
		this.buttonList.add(new GuiButton(123, 10, 15 + 20 * 5, 100, 10, "↓"));
		if(guild != null){
			x = guilds.size();
			scroll = 0;
			for(TextChannel channel : channels){
				if(channelScroll <= scroll && (scroll < channelScroll + 5)){
					PermissionUtil manager = new PermissionUtil();
					User user = Main.jda.getUsersByName(Main.jda.getSelfInfo().getUsername()).get(0);
					if(manager.checkPermission(user, Permission.MESSAGE_HISTORY, channel)){
						this.buttonList.add(new GuiButton(scroll + guilds.size(), 130, 15 + 20 * (x - Main.jda.getGuilds().size()), 120, 20, "#" + channel.getName()));

						x = x + 1;
					}
				}
				scroll = scroll + 1;

			}
			this.buttonList.add(new GuiButton(120, 130, 5, 120, 10, "↑"));
			this.buttonList.add(new GuiButton(121, 130, 15 + 20 * 5, 120, 10, "↓"));


		}
		String onOff = "Off";
		if(Main.displayMessages){
			onOff = "On";
		}
		this.buttonList.add(new GuiButton(124, this.width - 160, 10, 125, 20, "Chat Messages: " + onOff));
		this.buttonList.add(new GuiButton(125, this.width - 160, this.height - 30, 150, 20, "Logout (" + this.username + ")"));
		this.buttonList.add(new GuiButton(126, 10, this.height - 30, 150, 20, "Private Message..."));
		
		String on = "Off";
		if(this.discordPrefix){
			on = "On";
		}
		
		this.buttonList.add(new GuiButton(127, this.width - 160, 40, 145, 20, "[Discord] Chat Prefix: " + on));
		
		on = "Off";
		if(this.serverPrefix){
			on = "On";
		}
		
		this.buttonList.add(new GuiButton(128, this.width - 160, 70, 145, 20, "[Server] Chat Prefix: " + on));
		
		on = "Off";
		if(this.channelPrefix){
			on = "On";
		}
		
		this.buttonList.add(new GuiButton(129, this.width - 160, 100, 145, 20, "[Channel] Chat Prefix: " + on));

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	protected void mouseClicked(int x, int y, int btn) {

		try {
			super.mouseClicked(x, y, btn);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	public void initGui()
	{

	}

	public void updateScreen()
	{
		super.updateScreen();

	}

	protected void keyTyped(char par1, int par2)
	{
		try {
			super.keyTyped(par1, par2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id < Main.jda.getGuilds().size()){
			this.guild = Main.jda.getGuilds().get(guibutton.id);
			this.channels = guild.getTextChannels();
			this.channelScroll = 0;
		}
		if(guild != null){
			if(guibutton.id < Main.jda.getGuilds().size() + guild.getTextChannels().size() && (guibutton.id >= Main.jda.getGuilds().size())){
				int id = guibutton.id - Main.jda.getGuilds().size();
				this.channel = guild.getTextChannels().get(id);
				mc.displayGuiScreen(new GuiSendMessage(channel, true));
				Main.messages = new MessageHistory(channel).retrieve();
				Main.isPrivateMessage = false;
			}
		}
		switch(guibutton.id){
		case 120:
			if(this.channelScroll != 0){
				this.channelScroll -= 1;
			}
			break;
		case 121:
			if(channelScroll != channels.size() - 5){
				this.channelScroll += 1;
			}
			break;
		case 122:
			if(this.guildScroll != 0){
				this.guildScroll -= 1;
			}
			break;
		case 123:
			if(guildScroll != guilds.size() - 5){
				this.guildScroll += 1;

			}
			break;
		case 124:
			Main.displayMessages = !Main.displayMessages;
			break;
		case 125:

			Main.isAuth = false;
			File file = new File("discord_credentials");
			try {
				BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"));
				writer.write("null:null", 0, "null:null".length());
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ScarabUtil.chat("Logged out of account " + username + ".");

			mc.thePlayer.closeScreen();
			break;
		case 126:
			mc.displayGuiScreen(new GuiPrivateMessage());
			break;
		case 127:
			this.discordPrefix = !this.discordPrefix;
			ScarabUtil.setSetting(Setting.DISCORD, this.discordPrefix);
			break;
		case 128:
			this.serverPrefix = !this.serverPrefix;
			ScarabUtil.setSetting(Setting.SERVER, this.serverPrefix);
			break;
		case 129:
			this.channelPrefix = !this.channelPrefix;
			ScarabUtil.setSetting(Setting.CHANNEL, this.channelPrefix);
			break;
		}

	}
}

